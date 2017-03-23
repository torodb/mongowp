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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.netty.buffer.ByteBuf;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Inject;
import javax.inject.Qualifier;

/**
 *
 */
public class ShortStringPoolPolicy extends StringPoolPolicy {

  final int sizeLimit;

  @Inject
  public ShortStringPoolPolicy(@SizeLimit int sizeLimit) {
    this.sizeLimit = sizeLimit;
  }

  @Override
  public boolean apply(boolean likelyCacheable, ByteBuf input) {
    return isShort(input);
  }

  protected final boolean isShort(ByteBuf input) {
    return input.readableBytes() < sizeLimit;
  }

  @Override
  public String toString() {
    return "size < " + sizeLimit;
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  public static @interface SizeLimit {
  }
}
