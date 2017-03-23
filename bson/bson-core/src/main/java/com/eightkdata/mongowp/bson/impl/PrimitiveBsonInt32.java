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

import com.eightkdata.mongowp.bson.abst.AbstractBsonInt32;

/**
 *
 */
public class PrimitiveBsonInt32 extends AbstractBsonInt32 {

  private static final long serialVersionUID = -7671326038453610325L;

  private static final PrimitiveBsonInt32 ZERO = new PrimitiveBsonInt32(0);
  private static final PrimitiveBsonInt32 ONE = new PrimitiveBsonInt32(1);
  private static final PrimitiveBsonInt32 MINUS_ONE = new PrimitiveBsonInt32(-1);

  private final int value;

  PrimitiveBsonInt32(int value) {
    this.value = value;
  }

  /**
   * Returns a {@link PrimitiveBsonInt32} instance with the given value.
   */
  public static PrimitiveBsonInt32 newInstance(int value) {
    switch (value) {
      case 0:
        return ZERO;
      case 1:
        return ONE;
      case -1:
        return MINUS_ONE;
      default:
        return new PrimitiveBsonInt32(value);
    }
  }

  @Override
  public int intValue() {
    return value;
  }

  @Override
  public Integer getValue() {
    return value;
  }
}
