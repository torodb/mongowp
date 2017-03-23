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

import com.eightkdata.mongowp.bson.BsonRegex;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class AbstractBsonRegex extends AbstractBsonValue<BsonRegex> implements BsonRegex {

  @Override
  public Class<? extends BsonRegex> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonRegex getValue() {
    return this;
  }

  @Override
  public BsonType getType() {
    return BsonType.REGEX;
  }

  @Override
  public BsonRegex asRegex() {
    return this;
  }

  @Override
  public boolean isRegex() {
    return true;
  }

  @Override
  public Set<Options> getOptions() {
    return null;
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

    assert obj.isRegex();
    BsonRegex other = obj.asRegex();

    // TODO: Check how MongoDB compares documents!
    diff = this.getPattern().compareTo(other.getPattern());
    if (diff != 0) {
      return diff;
    }
    return this.getOptionsAsText().compareTo(other.getOptionsAsText());
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonRegex)) {
      return false;
    }
    BsonRegex other = (BsonRegex) obj;
    //We are comparing strings while the sets are ignored
    if (this.getOptionsAsText().compareTo(other.getOptionsAsText()) != 0) {
      return false;
    }
    return this.getPattern().equals(other.getPattern());
  }

  @Override
  public final int hashCode() {
    return getPattern().hashCode();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }
}
