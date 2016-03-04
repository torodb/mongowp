/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bson. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson;

import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.eightkdata.mongowp.bson.utils.NonIOByteSource;
import com.google.common.io.ByteSource;

/**
 *
 */
public interface BsonBinary extends BsonValue<BsonBinary>{

    byte getNumericSubType();

    BinarySubtype getSubtype();

    int size();

    NonIOByteSource getByteSource();

    /**
     * @return {@link IntBaseHasher#hash(int) IntBaseHasher.hash(this.size())}
     */
    @Override
    public int hashCode();

    /**
     * Two BsonBinary values are equal if their subtypes are the same and
     * their contain the same bytes.
     *
     * An easy way to implement that is to check the sub types and delegate on
     * {@link ByteSource#contentEquals(com.google.common.io.ByteSource) }
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj);
}
