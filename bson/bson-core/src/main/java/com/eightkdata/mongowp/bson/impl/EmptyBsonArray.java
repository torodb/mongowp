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
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Collections;

/**
 *
 */
public final class EmptyBsonArray extends AbstractBsonArray {

  private static final long serialVersionUID = -7588861924958681618L;

  private EmptyBsonArray() {
  }

  public static EmptyBsonArray getInstance() {
    return EmptyBsonArrayHolder.INSTANCE;
  }

  @Override
  public BsonValue<?> get(int index) {
    throw new IndexOutOfBoundsException(
        "Requested index '" + index + "' is higher than the array size (which is 0)");
  }

  @Override
  public boolean contains(BsonValue<?> element) {
    return false;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public UnmodifiableIterator<BsonValue<?>> iterator() {
    return Iterators.unmodifiableIterator(Collections.<BsonValue<?>>emptyIterator());
  }

  private static class EmptyBsonArrayHolder {

    private static final EmptyBsonArray INSTANCE = new EmptyBsonArray();
  }

  // @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
  private Object readResolve() {
    return EmptyBsonArray.getInstance();
  }
}
