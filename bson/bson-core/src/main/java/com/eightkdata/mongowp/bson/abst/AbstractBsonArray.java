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

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

public abstract class AbstractBsonArray extends CachedHashAbstractBsonValue<BsonArray>
    implements BsonArray {

  @Override
  public Class<? extends BsonArray> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonType getType() {
    return BsonType.ARRAY;
  }

  @Override
  public BsonArray getValue() {
    return this;
  }

  @Override
  public BsonArray asArray() {
    return this;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public int compareTo(BsonValue<?> obj) {
    if (obj == this) {
      return 0;
    }
    int diff = BsonTypeComparator.INSTANCE.compare(getType(), obj.getType());
    if (diff != 0) {
      return 0;
    }

    assert obj instanceof BsonArray;
    BsonArray other = obj.asArray();

    diff = this.size() - other.size();
    if (diff != 0) {
      return diff;
    }

    UnmodifiableIterator<BsonValue<?>> myIt = this.iterator();
    UnmodifiableIterator<BsonValue<?>> otherIt = other.iterator();

    while (myIt.hasNext() && otherIt.hasNext()) {
      diff = myIt.next().compareTo(otherIt.next());
      if (diff != 0) {
        return diff;
      }
    }
    assert !myIt.hasNext() : "the other array has more entries than ourself!";
    assert !otherIt.hasNext() : "the other array has less entries than ourself!";

    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonArray)) {
      return false;
    }
    BsonArray other = (BsonArray) obj;
    if (this.size() != other.size()) {
      return false;
    }
    return Iterators.elementsEqual(this.iterator(), other.iterator());
  }

  @Override
  final int calculateHash() {
    return IntBaseHasher.hash(size());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(size() * 20);
    sb.append('[');

    for (BsonValue<?> value : this) {
      sb.append(value);
      sb.append(", ");
    }
    if (!isEmpty()) {
      sb.delete(sb.length() - 2, sb.length());
    }

    sb.append(']');

    return sb.toString();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
