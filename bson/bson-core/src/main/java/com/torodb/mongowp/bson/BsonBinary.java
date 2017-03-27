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
package com.torodb.mongowp.bson;

import com.google.common.io.ByteSource;
import com.torodb.mongowp.bson.utils.IntBaseHasher;
import com.torodb.mongowp.bson.utils.NonIoByteSource;

public interface BsonBinary extends BsonValue<BsonBinary> {

  byte getNumericSubType();

  BinarySubtype getSubtype();

  int size();

  NonIoByteSource getByteSource();

  /**
   * @return {@link IntBaseHasher#hash(int) IntBaseHasher.hash(this.size())}
   */
  @Override
  public int hashCode();

  /**
   * Two BsonBinary values are equal if their subtypes are the same and their contain the same
   * bytes.
   *
   * <p>
   * An easy way to implement that is to check the sub types and delegate on
   * {@link ByteSource#contentEquals(com.google.common.io.ByteSource) }
   */
  @Override
  public boolean equals(Object obj);
}
