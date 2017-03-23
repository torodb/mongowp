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
package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonInt64;

/**
 *
 */
public class PrimitiveBsonInt64 extends AbstractBsonInt64 {

  private static final long serialVersionUID = 2881925179255803046L;

  private static final PrimitiveBsonInt64 ZERO = new PrimitiveBsonInt64(0);
  private static final PrimitiveBsonInt64 ONE = new PrimitiveBsonInt64(1);

  private final long value;

  private PrimitiveBsonInt64(long value) {
    this.value = value;
  }

  public static PrimitiveBsonInt64 newInstance(long value) {
    if (value == 0) {
      return ZERO;
    }
    if (value == 1) {
      return ONE;
    }
    return new PrimitiveBsonInt64(value);
  }

  @Override
  public long longValue() {
    return value;
  }

  @Override
  public Long getValue() {
    return value;
  }
}
