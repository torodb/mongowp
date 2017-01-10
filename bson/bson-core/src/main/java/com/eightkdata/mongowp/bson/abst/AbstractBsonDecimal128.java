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

import com.eightkdata.mongowp.bson.BsonDecimal128;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;

import java.math.BigDecimal;

public abstract class AbstractBsonDecimal128 extends AbstractBsonNumber<BigDecimal>
    implements BsonDecimal128 {

  @Override
  public Class<? extends BigDecimal> getValueClass() {
    return BigDecimal.class;
  }

  @Override
  public BsonType getType() {
    return BsonType.DECIMAL128;
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
  public double doubleValue() {
    return getValue().doubleValue();
  }
  
  @Override
  public BsonDecimal128 asDecimal128() {
    return this;
  }

  @Override
  public boolean isDecimal128() {
    return true;
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (null == obj) {
      return false;
    }
    if (!(obj instanceof BsonDecimal128)) {
      return false;
    }

    BsonDecimal128 dec = (BsonDecimal128) obj;

    if (getLow() != dec.getLow()) {
      return false;
    }

    if (getHigh() != dec.getHigh()) {
      return false;
    }

    return true;
  }

  @Override
  public final int hashCode() {
    return getValue().hashCode();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
