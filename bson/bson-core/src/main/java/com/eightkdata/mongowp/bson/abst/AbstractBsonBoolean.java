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

import com.eightkdata.mongowp.bson.BsonBoolean;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;
import com.google.common.primitives.Booleans;

public abstract class AbstractBsonBoolean extends AbstractBsonValue<Boolean>
    implements BsonBoolean {

  @Override
  public Class<? extends Boolean> getValueClass() {
    return Boolean.class;
  }

  @Override
  public Boolean getValue() {
    return getPrimitiveValue();
  }

  @Override
  public BsonType getType() {
    return BsonType.BOOLEAN;
  }

  @Override
  public BsonBoolean asBoolean() {
    return this;
  }

  @Override
  public boolean isBoolean() {
    return true;
  }

  @Override
  public int compareTo(BsonValue<?> obj) {
    if (obj == this) {
      return 0;
    }
    int diff = BsonTypeComparator.INSTANCE.compare(getType(), obj.getType());
    if (diff != 0) {
      return diff;
    }

    assert obj instanceof BsonBoolean;
    BsonBoolean other = obj.asBoolean();
    // TODO: Check how MongoDB compares booleans!
    return this.getValue().compareTo(other.getValue());
  }

  @Override
  public final boolean equals(Object obj) {
    return this == obj || obj != null && obj instanceof BsonBoolean
        && ((BsonBoolean) obj).getPrimitiveValue() == getPrimitiveValue();
  }

  @Override
  public final int hashCode() {
    return Booleans.hashCode(getPrimitiveValue());
  }

  @Override
  public String toString() {
    return getValue().toString();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
