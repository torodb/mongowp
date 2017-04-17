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

import com.torodb.mongowp.bson.BsonInt32;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValueVisitor;

public abstract class AbstractBsonInt32 extends AbstractBsonNumber<Integer> implements BsonInt32 {

  @Override
  public Class<? extends Integer> getValueClass() {
    return Integer.class;
  }

  @Override
  public BsonType getType() {
    return BsonType.INT32;
  }

  @Override
  public long longValue() {
    return intValue();
  }

  @Override
  public double doubleValue() {
    return intValue();
  }

  @Override
  public BsonInt32 asInt32() {
    return this;
  }

  @Override
  public boolean isInt32() {
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
    if (!(obj instanceof BsonInt32)) {
      return false;
    }
    return intValue() == ((BsonInt32) obj).intValue();
  }

  @Override
  public final int hashCode() {
    return intValue();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
