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

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.BsonValueVisitor;
import com.torodb.mongowp.bson.utils.BsonTypeComparator;
import com.torodb.mongowp.bson.utils.IntBaseHasher;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.annotation.Nonnull;

public abstract class AbstractBsonDocument extends CachedHashAbstractBsonValue<BsonDocument>
    implements BsonDocument {

  @Override
  public Class<? extends BsonDocument> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonDocument getValue() {
    return this;
  }

  @Override
  public BsonDocument asDocument() {
    return this;
  }

  @Override
  public Entry<?> getFirstEntry() throws NoSuchElementException {
    return iterator().next();
  }

  @Override
  public boolean isDocument() {
    return true;
  }

  @Override
  public BsonType getType() {
    return BsonType.DOCUMENT;
  }

  @Override
  public boolean isEmpty() {
    return size() == 0;
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

    assert obj.isDocument();
    BsonDocument other = obj.asDocument();
    // TODO: Check how MongoDB compares documents!

    diff = this.size() - other.size();
    if (diff != 0) {
      return diff;
    }

    Iterator<Entry<?>> myIt = this.iterator();
    Iterator<Entry<?>> otherIt = other.iterator();
    while (myIt.hasNext() && otherIt.hasNext()) {
      diff = myIt.next().getValue().compareTo(otherIt.next().getValue());
      if (diff != 0) {
        return diff;
      }
    }
    assert !myIt.hasNext() :
        "the other document has more entries than ourself!";
    assert !otherIt.hasNext() :
        "the other document has less entries than ourself!";

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
    if (!(obj instanceof BsonDocument)) {
      return false;
    }
    return Iterators.elementsEqual(this.iterator(), ((BsonDocument) obj)
        .iterator());
  }

  @Override
  int calculateHash() {
    return IntBaseHasher.hash(size());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(size() * 20);
    sb.append('{');

    for (Entry<?> entry : this) {
      sb.append(entry.getKey()).append(": ").append(entry.getValue());
      sb.append(", ");
    }
    if (!isEmpty()) {
      sb.delete(sb.length() - 2, sb.length());
    }

    sb.append('}');

    return sb.toString();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

  public static class SimpleEntry<V> implements Entry<V> {

    private static final long serialVersionUID = 5338375192630666767L;

    private final String key;
    private final BsonValue<V> value;

    public SimpleEntry(String key, BsonValue<V> value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String getKey() {
      return key;
    }

    @Override
    public BsonValue<V> getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 47 * hash + Objects.hashCode(this.key);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final SimpleEntry<?> other = (SimpleEntry<?>) obj;
      if (!Objects.equals(this.key, other.key)) {
        return false;
      }
      return Objects.equals(this.value, other.value);
    }

  }

  protected static class FromEntryMap
      implements Function<Map.Entry<String, BsonValue<?>>, Entry<?>> {

    public static final FromEntryMap INSTANCE = new FromEntryMap();

    private FromEntryMap() {
    }

    @Override
    public Entry<?> apply(@Nonnull Map.Entry<String, BsonValue<?>> input) {
      return new SimpleEntry<>(input.getKey(), input.getValue());
    }

  }
}
