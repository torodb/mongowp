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

import com.google.common.primitives.Longs;
import com.torodb.mongowp.bson.BsonInt64;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValueVisitor;

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
