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
package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonDouble;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.google.common.primitives.Doubles;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class AbstractBsonDouble extends AbstractBsonNumber<Double> implements BsonDouble {

  @Override
  public Class<? extends Double> getValueClass() {
    return Double.class;
  }

  @Override
  public BsonType getType() {
    return BsonType.DOUBLE;
  }

  @Override
  public Double getValue() {
    return doubleValue();
  }

  @Override
  public int intValue() {
    return (int) doubleValue();
  }

  @Override
  public long longValue() {
    return (long) doubleValue();
  }

  @Override
  public BsonDouble asDouble() {
    return this;
  }

  @Override
  public boolean isDouble() {
    return true;
  }

  @Override
  public boolean simmilar(BsonDouble other, double delta) {
    return Math.abs(this.doubleValue() - other.doubleValue()) < delta;
  }

  @SuppressFBWarnings("FE_FLOATING_POINT_EQUALITY")
  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonDouble)) {
      return false;
    }
    return this.doubleValue() == ((BsonDouble) obj).doubleValue();
  }

  @Override
  public final int hashCode() {
    return Doubles.hashCode(doubleValue());
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
