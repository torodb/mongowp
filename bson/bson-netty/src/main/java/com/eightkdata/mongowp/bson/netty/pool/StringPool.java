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
import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

/**
 *
 */
@ThreadSafe
public abstract class StringPool {

  private final StringPoolPolicy heuristic;

  @Inject
  public StringPool(StringPoolPolicy heuristic) {
    this.heuristic = heuristic;
  }

  protected static String getString(@Tight @ConservesIndexes ByteBuf stringBuf) {
    return stringBuf.toString(Charsets.UTF_8);
  }

  public String fromPool(boolean likelyCacheable, @Tight @ConservesIndexes ByteBuf stringBuf) {
    if (!heuristic.apply(likelyCacheable, stringBuf)) {
      return getString(stringBuf);
    }
    return retrieveFromPool(stringBuf);
  }

  protected abstract String retrieveFromPool(@Tight @ConservesIndexes ByteBuf stringBuf);

}
