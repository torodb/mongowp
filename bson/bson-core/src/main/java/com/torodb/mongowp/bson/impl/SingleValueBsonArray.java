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
import com.torodb.mongowp.bson.abst.AbstractBsonArray;

import java.util.Collections;

/**
 *
 */
public class SingleValueBsonArray extends AbstractBsonArray {

  private static final long serialVersionUID = -1073107257449881416L;

  private final BsonValue<?> child;

  public SingleValueBsonArray(BsonValue<?> child) {
    this.child = child;
  }

  @Override
  public BsonValue<?> get(int index) throws IndexOutOfBoundsException {
    if (index == 0) {
      return child;
    }
    throw new IndexOutOfBoundsException();
  }

  @Override
  public boolean contains(BsonValue<?> element) {
    return child.equals(element);
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public UnmodifiableIterator<BsonValue<?>> iterator() {
    return Iterators.unmodifiableIterator(Collections.<BsonValue<?>>singleton(child).iterator());
  }

}
