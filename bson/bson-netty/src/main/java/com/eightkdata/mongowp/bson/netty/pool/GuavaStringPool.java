/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
