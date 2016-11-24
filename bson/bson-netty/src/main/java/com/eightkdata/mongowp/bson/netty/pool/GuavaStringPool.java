/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.bson.netty.pool;

import com.eightkdata.mongowp.bson.netty.annotations.ConservesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 *
 */
public class GuavaStringPool extends StringPool {

  private static final Logger LOGGER = LogManager.getLogger(GuavaStringPool.class);
  private final LoadingCache<ByteBuf, String> cache;

  @Inject
  public GuavaStringPool(StringPoolPolicy heuristic, CacheBuilder<Object, Object> cacheBuilder) {
    super(heuristic);
    cache = cacheBuilder.removalListener(new MyRemovalListener()).build(new MyCacheLoader());
  }

  @Override
  protected String retrieveFromPool(@Tight @ConservesIndexes ByteBuf stringBuf) {
    String result = cache.getIfPresent(stringBuf);
    if (result == null) {
      result = cache.getUnchecked(stringBuf.copy());
    }
    return result;
  }

  private static class MyCacheLoader extends CacheLoader<ByteBuf, String> {

    @Override
    public String load(@Nonnull ByteBuf key) {
      return StringPool.getString(key);
    }

  }

  private static class MyRemovalListener implements RemovalListener<ByteBuf, String> {

    @Override
    public void onRemoval(RemovalNotification<ByteBuf, String> notification) {
      ByteBuf key = notification.getKey();
      if (key == null) {
        throw new IllegalStateException("Unexpected null key");
      }
      boolean release = key.release();
      if (!release) {
        LOGGER.warn("The cached string ByteBuf with value {} was "
            + "removed but it had more references than expected", notification.getValue());
      }
    }

  }

}
