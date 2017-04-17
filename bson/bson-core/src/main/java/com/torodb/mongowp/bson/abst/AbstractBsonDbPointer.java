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

import com.torodb.mongowp.bson.BsonDbPointer;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.BsonValueVisitor;
import com.torodb.mongowp.bson.utils.BsonTypeComparator;

import java.util.Objects;

public abstract class AbstractBsonDbPointer extends AbstractBsonValue<BsonDbPointer>
    implements BsonDbPointer {

  @Override
  public Class<? extends BsonDbPointer> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonDbPointer getValue() {
    return this;
  }

  @Override
  public BsonDbPointer asDbPointer() {
    return this;
  }

  @Override
  public boolean isDbPointer() {
    return true;
  }

  @Override
  public BsonType getType() {
    return BsonType.DB_POINTER;
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

    if (obj.isObjectId()) {
      return 1;
    }

    assert obj instanceof BsonDbPointer;
    BsonDbPointer other = (BsonDbPointer) obj;
    // TODO: Check how MongoDB compares pointers!

    diff = this.getNamespace().compareTo(other.getNamespace());
    if (diff != 0) {
      return diff;
    }
    return this.getId().compareTo(other.getId());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonDbPointer)) {
      return false;
    }
    BsonDbPointer other = (BsonDbPointer) obj;
    if (!Objects.equals(this.getNamespace(), other.getNamespace())) {
      return false;
    }
    return this.getId().equals(other.getId());
  }

  @Override
  public final int hashCode() {
    return getId().hashCode();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
