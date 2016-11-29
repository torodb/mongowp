/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
