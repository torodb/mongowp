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

import com.torodb.mongowp.bson.netty.annotations.ConservesIndexes;
import com.torodb.mongowp.bson.netty.annotations.Tight;
import io.netty.buffer.ByteBuf;

import javax.annotation.concurrent.ThreadSafe;

/**
 *
 */
@ThreadSafe
public abstract class StringPoolPolicy {

  public abstract boolean apply(boolean likelyCacheable, @Tight
      @ConservesIndexes ByteBuf input);

  public StringPoolPolicy and(StringPoolPolicy other) {
    return new AndStringPoolPolicy(this, other);
  }

  public StringPoolPolicy or(StringPoolPolicy other) {
    return new OrStringPoolPolicy(this, other);
  }
}
