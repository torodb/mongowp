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

import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonUndefined;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;

public abstract class AbstractBsonUndefined extends AbstractBsonValue<BsonUndefined>
    implements BsonUndefined {

  @Override
  public Class<? extends BsonUndefined> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonUndefined getValue() {
    return this;
  }

  @Override
  public BsonType getType() {
    return BsonType.UNDEFINED;
  }

  @Override
  public BsonUndefined asUndefined() {
    return this;
  }

  @Override
  public boolean isUndefined() {
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

    if (obj.isNull()) {
      return 1;
    }

    if (obj.isDeprecated()) {
      return -1;
    }

    assert obj.isUndefined();
    return 0;
  }

  @Override
  public final boolean equals(Object obj) {
    return this == obj || obj != null && obj instanceof BsonUndefined;
  }

  @Override
  public final int hashCode() {
    return HASH;
  }

  @Override
  public String toString() {
    return "undefined";
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
