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

import com.torodb.mongowp.bson.BsonDeprecated;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.BsonValueVisitor;
import com.torodb.mongowp.bson.utils.BsonTypeComparator;

public abstract class AbstractBsonDeprecated extends AbstractBsonValue<String>
    implements BsonDeprecated {

  @Override
  public Class<? extends String> getValueClass() {
    return String.class;
  }

  @Override
  public BsonType getType() {
    return BsonType.DEPRECATED;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonDeprecated)) {
      return false;
    }
    return this.getValue().equals(((BsonDeprecated) obj).getValue());
  }

  @Override
  public BsonDeprecated asDeprecated() {
    return this;
  }

  @Override
  public boolean isDeprecated() {
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

    if (obj.isUndefined()) {
      return 1;
    }

    assert obj.isDeprecated();
    return 0;
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
