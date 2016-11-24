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

package com.eightkdata.mongowp.bson;

import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.eightkdata.mongowp.bson.utils.NonIoByteSource;
import com.google.common.io.ByteSource;

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
