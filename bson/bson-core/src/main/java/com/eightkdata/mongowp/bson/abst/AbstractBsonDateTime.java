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

import com.eightkdata.mongowp.bson.BsonDateTime;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;

import java.time.Instant;

public abstract class AbstractBsonDateTime extends AbstractBsonValue<Instant>
    implements BsonDateTime {

  @Override
  public Class<? extends Instant> getValueClass() {
    return Instant.class;
  }

  @Override
  public BsonType getType() {
    return BsonType.DATETIME;
  }

  @Override
  public BsonDateTime asDateTime() {
    return this;
  }

  @Override
  public boolean isDateTime() {
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

    assert obj instanceof BsonDateTime;
    BsonDateTime other = obj.asDateTime();
    return this.getValue().compareTo(other.getValue());
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonDateTime)) {
      return false;
    }
    return this.getMillisFromUnix() == ((BsonDateTime) obj).getMillisFromUnix();
  }

  @Override
  public final int hashCode() {
    return getValue().hashCode();
  }

  @Override
  public String toString() {
    return "{$date: " + getValue() + "}";
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }
}
