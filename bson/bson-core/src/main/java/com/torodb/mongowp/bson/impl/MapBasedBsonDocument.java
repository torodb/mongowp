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
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.abst.AbstractBsonDocument;
import com.torodb.mongowp.bson.annotations.NotMutable;

import java.util.LinkedHashMap;

/**
 *
 */
public class MapBasedBsonDocument extends AbstractBsonDocument {

  private static final long serialVersionUID = 4020431717465865262L;

  private final LinkedHashMap<String, BsonValue<?>> map;

  public MapBasedBsonDocument(@NotMutable LinkedHashMap<String, BsonValue<?>> map) {
    this.map = map;
  }

  @Override
  public BsonValue<?> get(String key) {
    return map.get(key);
  }

  @Override
  public boolean containsKey(String key) {
    return map.containsKey(key);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Entry<?> getEntry(String key) {
    BsonValue<?> value = map.get(key);
    if (value == null) {
      return null;
    }
    return new SimpleEntry<>(key, value);
  }

  @Override
  public UnmodifiableIterator<Entry<?>> iterator() {
    return Iterators.unmodifiableIterator(
        Iterators.transform(
            map.entrySet().iterator(),
            AbstractBsonDocument.FromEntryMap.INSTANCE
        )
    );
  }
}
