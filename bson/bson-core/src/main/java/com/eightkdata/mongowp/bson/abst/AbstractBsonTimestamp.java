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

package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonTimestamp;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;
import com.google.common.primitives.UnsignedInts;

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
