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

import com.torodb.mongowp.bson.BsonDecimal128;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValueVisitor;

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
