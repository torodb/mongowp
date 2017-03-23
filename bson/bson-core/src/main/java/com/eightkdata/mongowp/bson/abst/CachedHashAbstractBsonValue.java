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

abstract class CachedHashAbstractBsonValue<V> extends AbstractBsonValue<V> {

  int hash = 0;

  /**
   * Calculates the hash of this object.
   *
   * <p>
   * The hash must be different than 0.
   */
  abstract int calculateHash();

  @Override
  public abstract boolean equals(Object obj);

  @Override
  public final int hashCode() {
    if (hash == 0) {
      hash = calculateHash();
      assert hash != 0;
    }
    return hash;
  }

}
