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
package com.torodb.mongowp.bson.netty.pool;

import com.google.common.base.Charsets;
import com.torodb.mongowp.bson.netty.annotations.ConservesIndexes;
import com.torodb.mongowp.bson.netty.annotations.Tight;
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
