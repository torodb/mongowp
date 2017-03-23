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

import com.eightkdata.mongowp.bson.abst.AbstractBsonDouble;

/**
 *
 */
public class PrimitiveBsonDouble extends AbstractBsonDouble {

  private static final long serialVersionUID = -8649710470577957984L;

  private static final PrimitiveBsonDouble ZERO = new PrimitiveBsonDouble(0);
  private static final PrimitiveBsonDouble ONE = new PrimitiveBsonDouble(1);

  private final double value;

  private PrimitiveBsonDouble(double value) {
    this.value = value;
  }

  public static PrimitiveBsonDouble newInstance(double value) {
    if (value == 0) {
      return ZERO;
    }
    if (value == 1) {
      return ONE;
    }
    return new PrimitiveBsonDouble(value);
  }

  @Override
  public double doubleValue() {
    return value;
  }

  @Override
  public Double getValue() {
    return value;
  }

}
