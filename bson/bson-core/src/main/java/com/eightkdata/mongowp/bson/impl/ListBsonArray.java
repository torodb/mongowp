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
package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.abst.AbstractBsonArray;
import com.eightkdata.mongowp.bson.annotations.NotMutable;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ListBsonArray extends AbstractBsonArray {

  private static final long serialVersionUID = 6400879352530123227L;

  private final List<BsonValue<?>> list;

  public ListBsonArray(@NotMutable List<BsonValue<?>> list) {
    this.list = list;
  }

  @Override
  public BsonValue<?> get(int index) {
    return list.get(index);
  }

  @Override
  public boolean contains(BsonValue<?> element) {
    return list.contains(element);
  }

  @Override
  public int size() {
    return list.size();
  }

  public boolean containsAll(Iterable<BsonValue<?>> iterable) {
    for (BsonValue<?> bsonValue : iterable) {
      if (!contains(bsonValue)) {
        return false;
      }
    }
    return true;
  }

  public boolean containsAll(Collection<BsonValue<?>> iterable) {
    return list.containsAll(iterable);
  }

  @Override
  public UnmodifiableIterator<BsonValue<?>> iterator() {
    return Iterators.unmodifiableIterator(list.listIterator());
  }
}
