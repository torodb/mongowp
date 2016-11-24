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
