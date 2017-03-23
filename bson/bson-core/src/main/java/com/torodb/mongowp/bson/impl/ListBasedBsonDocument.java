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
package com.torodb.mongowp.bson.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.abst.AbstractIterableBasedBsonDocument;
import com.torodb.mongowp.bson.annotations.NotMutable;

import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 */
public class ListBasedBsonDocument extends AbstractIterableBasedBsonDocument {

  private static final long serialVersionUID = -6475758693810996556L;

  private final List<BsonDocument.Entry<?>> entries;

  public ListBasedBsonDocument(@NotMutable List<Entry<?>> entries) {
    this.entries = entries;
  }

  @Override
  public int size() {
    return entries.size();
  }

  @Override
  public Entry<?> getFirstEntry() throws NoSuchElementException {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return entries.get(0);
  }

  @Override
  public UnmodifiableIterator<Entry<?>> iterator() {
    return Iterators.unmodifiableIterator(entries.iterator());
  }

}
