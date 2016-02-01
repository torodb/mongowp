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

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.BinarySubtype;
import com.eightkdata.mongowp.bson.abst.AbstractBsonBinary;
import com.eightkdata.mongowp.bson.utils.NonIOByteSource;
import com.google.common.io.ByteSource;
import java.util.Arrays;

/**
 *
 */
public class ByteArrayBsonBinary extends AbstractBsonBinary {

    private static final long serialVersionUID = 6462169816370072534L;

    private final byte numericSubType;
    private final BinarySubtype subtype;
    private final NonIOByteSource byteSource;

    public ByteArrayBsonBinary(BinarySubtype subtype, byte numericSubType, byte[] array) {
        this.subtype = subtype;
        this.numericSubType = numericSubType;
        this.byteSource = new NonIOByteSource(ByteSource.wrap(Arrays.copyOf(array, array.length)));
    }

    @Override
    public byte getNumericSubType() {
        return numericSubType;
    }

    @Override
    public BinarySubtype getSubtype() {
        return subtype;
    }

    @Override
    public int size() {
        long size = byteSource.size();
        assert size <= Integer.MAX_VALUE;
        return (int) size;
    }

    @Override
    public NonIOByteSource getByteSource() {
        return byteSource;
    }

}
