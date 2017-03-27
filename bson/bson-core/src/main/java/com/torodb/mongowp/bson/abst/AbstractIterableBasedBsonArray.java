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
package com.torodb.mongowp.bson.abst;

import com.google.common.collect.Iterables;
import com.torodb.mongowp.bson.BsonValue;

public abstract class AbstractIterableBasedBsonArray extends AbstractBsonArray {

  private int cachedSize = -1;

  @Override
  public BsonValue<?> get(int index) {
    return Iterables.get(this, index);
  }

  @Override
  public boolean contains(BsonValue<?> element) {
    return Iterables.contains(this, element);
  }

  @Override
  public int size() {
    if (cachedSize == -1) {
      cachedSize = Iterables.size(this);
    }
    assert cachedSize >= 0;
    return cachedSize;
  }

}
