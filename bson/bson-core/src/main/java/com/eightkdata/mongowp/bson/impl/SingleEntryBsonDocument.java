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
import com.eightkdata.mongowp.bson.abst.AbstractBsonDocument;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

/**
 *
 */
public class SingleEntryBsonDocument extends AbstractBsonDocument {

  private static final long serialVersionUID = -2025309751059819455L;

  private final SimpleEntry<?> entry;

  public SingleEntryBsonDocument(String key, BsonValue<?> value) {
    entry = new SimpleEntry<>(key, value);
  }

  @Override
  public BsonValue<?> get(String key) {
    if (key.equals(entry.getKey())) {
      return entry.getValue();
    }
    return null;
  }

  @Override
  public boolean containsKey(String key) {
    return key.equals(entry.getKey());
  }

  @Override
  public Entry<?> getEntry(String key) {
    if (!key.equals(entry.getKey())) {
      return null;
    }
    return entry;
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public UnmodifiableIterator<Entry<?>> iterator() {
    return Iterators.<Entry<?>>singletonIterator(entry);
  }

  @Override
  public String toString() {
    return "{" + entry.getKey() + ": " + entry.getValue() + '}';
  }

}
