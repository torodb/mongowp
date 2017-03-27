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

import com.google.common.primitives.Doubles;
import com.torodb.mongowp.bson.BsonDouble;
import com.torodb.mongowp.bson.BsonInt32;
import com.torodb.mongowp.bson.BsonInt64;
import com.torodb.mongowp.bson.BsonNumber;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.impl.PrimitiveBsonDouble;
import com.torodb.mongowp.bson.impl.PrimitiveBsonInt32;
import com.torodb.mongowp.bson.impl.PrimitiveBsonInt64;
import com.torodb.mongowp.bson.utils.BsonTypeComparator;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = "EQ_COMPARETO_USE_OBJECT_EQUALS",
    justification = "Sub-classes must implement equals and hash code properly")
public abstract class AbstractBsonNumber<V extends Number> extends AbstractBsonValue<V>
    implements BsonNumber<V> {

  @Override
  public abstract boolean equals(Object other);

  @Override
  public abstract int hashCode();

  @Override
  public BsonInt64 asInt64() {
    return PrimitiveBsonInt64.newInstance(longValue());
  }

  @Override
  public BsonInt32 asInt32() {
    return PrimitiveBsonInt32.newInstance(intValue());
  }

  @Override
  public BsonDouble asDouble() {
    return PrimitiveBsonDouble.newInstance(doubleValue());
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

    assert obj instanceof BsonNumber;
    BsonNumber<?> other = obj.asNumber();
    return Doubles.compare(this.doubleValue(), other.doubleValue());
  }

  @Override
  public boolean isNumber() {
    return true;
  }

  @Override
  public BsonNumber asNumber() throws UnsupportedOperationException {
    return this;
  }

  @Override
  public String toString() {
    return getValue().toString();
  }
}
