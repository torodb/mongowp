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
public class NeverStringPoolPolicy extends StringPoolPolicy {

  private NeverStringPoolPolicy() {
  }

  @Override
  public boolean apply(boolean likelyCacheable, ByteBuf input) {
    return false;
  }

  @Override
  public StringPoolPolicy or(StringPoolPolicy other) {
    return other;
  }

  @Override
  public StringPoolPolicy and(StringPoolPolicy other) {
    return this;
  }

  @Override
  public String toString() {
    return "never";
  }

  public static NeverStringPoolPolicy getInstance() {
    return NeverStringPoolPolicyHolder.INSTANCE;
  }

  private static class NeverStringPoolPolicyHolder {

    private static final NeverStringPoolPolicy INSTANCE = new NeverStringPoolPolicy();
  }

  // @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
  private Object readResolve() {
    return NeverStringPoolPolicy.getInstance();
  }
}
