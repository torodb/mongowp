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

import io.netty.buffer.ByteBuf;

/**
 *
 */
public class AndStringPoolPolicy extends StringPoolPolicy {

  private final StringPoolPolicy policy1;
  private final StringPoolPolicy policy2;

  public AndStringPoolPolicy(StringPoolPolicy policy1, StringPoolPolicy policy2) {
    this.policy1 = policy1;
    this.policy2 = policy2;
  }

  @Override
  public boolean apply(boolean likelyCacheable, ByteBuf input) {
    return policy1.apply(likelyCacheable, input) && policy2.apply(likelyCacheable, input);
  }

  @Override
  public String toString() {
    return policy1 + " and " + policy2;
  }
}
