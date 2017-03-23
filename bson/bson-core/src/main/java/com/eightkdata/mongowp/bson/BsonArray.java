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

import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 *
 */
public interface BsonArray extends BsonValue<BsonArray>, Iterable<BsonValue<?>> {

  @Nonnull
  BsonValue<?> get(int index) throws IndexOutOfBoundsException;

  boolean contains(BsonValue<?> element);

  boolean isEmpty();

  int size();

  /**
   * @return {@link IntBaseHasher#hash(int) IntBaseHasher.hash(this.size())}
   */
  @Override
  public int hashCode();

  /**
   * Two BsonArray values are equal if their contains equal elements in the same position.
   * <p>
   * An easy way to implement that is to delegate on
   * {@link Iterators#elementsEqual(java.lang.Iterator, java.lang.Iterator) }
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);

  @Override
  UnmodifiableIterator<BsonValue<?>> iterator();

  default List<BsonValue<?>> asList() {
    return Lists.newArrayList(iterator());
  }

  default Stream<BsonValue<?>> stream() {
    return asList().stream();
  }
}
