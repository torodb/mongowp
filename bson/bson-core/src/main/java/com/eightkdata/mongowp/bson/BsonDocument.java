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
package com.eightkdata.mongowp.bson;

import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.collect.UnmodifiableIterator;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public interface BsonDocument extends BsonValue<BsonDocument>, Iterable<Entry<?>> {

  @Nullable
  BsonValue<?> get(String key);

  boolean containsKey(String key);

  boolean isEmpty();

  int size();

  /**
   * @return the first entry of this document
   * @throws NoSuchElementException if it is {@linkplain #isEmpty() empty}
   */
  @Nonnull
  Entry<?> getFirstEntry() throws NoSuchElementException;

  @Nullable
  public Entry<?> getEntry(String key);

  @Override
  public UnmodifiableIterator<Entry<?>> iterator();

  public default Stream<Entry<?>> stream() {
    return StreamSupport.stream(this.spliterator(), false);
  }

  public default Optional<Entry<?>> getOptionalEntry(String key) {
    return Optional.ofNullable(getEntry(key));
  }

  public default Optional<BsonValue> getOptional(String key) {
    return Optional.ofNullable(get(key));
  }

  /**
   * Two documents are equal if they contain the same entries in the same order.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);

  /**
   * The hashCode of a BsonDocument is calculated by calling {@linkplain IntBaseHasher#hash(int)
   * IntBaseHasher.hash(this.size())}.
   *
   * @return
   */
  @Override
  public int hashCode();

  public static interface Entry<V> extends Serializable {

    @Nonnull
    public String getKey();

    @Nonnull
    public BsonValue<V> getValue();

    /**
     * Two entries are equals if their keys and values are equal.
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o);

    /**
     * The hashCode of a entry is the hash of its key.
     *
     * @return
     */
    @Override
    public int hashCode();
  }
}
