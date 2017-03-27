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
package com.torodb.mongowp.bson.abst;

import com.google.common.primitives.UnsignedInts;
import com.torodb.mongowp.bson.BsonTimestamp;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.BsonValueVisitor;
import com.torodb.mongowp.bson.utils.BsonTypeComparator;

public abstract class AbstractBsonTimestamp extends AbstractBsonValue<BsonTimestamp>
    implements BsonTimestamp {

  @Override
  public Class<? extends BsonTimestamp> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonTimestamp getValue() {
    return this;
  }

  @Override
  public BsonType getType() {
    return BsonType.TIMESTAMP;
  }

  @Override
  public BsonTimestamp asTimestamp() {
    return this;
  }

  @Override
  public boolean isTimestamp() {
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

    assert obj.isTimestamp();
    return compareTo(obj.asTimestamp());
  }

  public int compareTo(BsonTimestamp obj) {
    int diff = this.getSecondsSinceEpoch() - obj.getSecondsSinceEpoch();
    if (diff != 0) {
      return diff;
    }
    return this.getOrdinal() - obj.getOrdinal();
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonTimestamp)) {
      return false;
    }
    BsonTimestamp other = (BsonTimestamp) obj;
    if (this.getOrdinal() != other.getOrdinal()) {
      return false;
    }
    return this.getSecondsSinceEpoch() == other.getSecondsSinceEpoch();
  }

  @Override
  public final int hashCode() {
    return getSecondsSinceEpoch() << 4 | (getOrdinal() & 0xF);
  }

  @Override
  public String toString() {
    return "{ \"$timestamp\": { \"t\": " + UnsignedInts.toString(getSecondsSinceEpoch())
        + ", \"i\": " + UnsignedInts.toString(getOrdinal()) + "} }";
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
