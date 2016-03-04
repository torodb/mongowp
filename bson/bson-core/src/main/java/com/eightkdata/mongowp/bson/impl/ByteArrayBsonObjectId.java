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
 * along with bson-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonObjectId;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInteger;
import java.util.Arrays;

/**
 *
 */
public class ByteArrayBsonObjectId extends AbstractBsonObjectId {

    private static final long serialVersionUID = -8871610748330532954L;

    private final byte[] bytes;

    public ByteArrayBsonObjectId(byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 12, "The given array of bytes is too short");
        this.bytes = Arrays.copyOf(bytes, 12);
    }

    @Override
    protected byte[] getBytesUnsafe() {
        return bytes;
    }

    @Override
    public byte[] toByteArray() {
        return Arrays.copyOf(bytes, 12);
    }

    @Override
    public UnsignedInteger getUnsignedTimestamp() {
        return UnsignedInteger.fromIntBits(getIntTimestamp());
    }

    private int getIntTimestamp() {
        return bytesAsInt(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    @Override
    public int getMachineIdentifier() {
        return bytesAsInt((byte) 0, bytes[4], bytes[5], bytes[6]);
    }

    @Override
    public int getProcessId() {
        return bytesAsInt((byte) 0, (byte) 0, bytes[7], bytes[8]);
    }

    @Override
    public int getCounter() {
        return bytesAsInt((byte) 0, bytes[9], bytes[10], bytes[11]);
    }

}
