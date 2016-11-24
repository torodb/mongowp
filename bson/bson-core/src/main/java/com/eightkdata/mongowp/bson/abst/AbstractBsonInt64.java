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

import com.eightkdata.mongowp.bson.BsonInt64;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.google.common.primitives.Longs;

public abstract class AbstractBsonInt64 extends AbstractBsonNumber<Long> implements BsonInt64 {

  @Override
  public Class<? extends Long> getValueClass() {
    return Long.class;
  }

  @Override
  public BsonType getType() {
    return BsonType.INT64;
  }

  @Override
  public int intValue() {
    return (int) longValue();
  }

  @Override
  public double doubleValue() {
    return longValue();
  }

  @Override
  public BsonInt64 asInt64() {
    return this;
  }

  @Override
  public boolean isInt64() {
    return true;
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonInt64)) {
      return false;
    }
    return longValue() == ((BsonInt64) obj).longValue();
  }

  @Override
  public final int hashCode() {
    return Longs.hashCode(longValue());
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
