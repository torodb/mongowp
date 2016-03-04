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
 * along with bson-netty. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.BinarySubtype;
import com.eightkdata.mongowp.bson.abst.AbstractBsonBinary;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import com.eightkdata.mongowp.bson.utils.NonIOByteSource;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public class NettyBsonBsonBinary extends AbstractBsonBinary {

    private static final long serialVersionUID = 6766481057628149423L;

    private final byte numericSubtype;
    private final int length;
    private final BinarySubtype subtype;
    private final NonIOByteSource byteSource;
    
    public NettyBsonBsonBinary(byte numericSubtype, BinarySubtype subtype, @Tight @ModifiesIndexes ByteBuf data) {
        this.numericSubtype = numericSubtype;
        this.subtype = subtype;
        length = data.readableBytes();
        byteSource = new NonIOByteSource(new ByteBufByteSource(data));
    }

    @Override
    public byte getNumericSubType() {
        return numericSubtype;
    }

    @Override
    public BinarySubtype getSubtype() {
        return subtype;
    }

    @Override
    public int size() {
        return length;
    }

    @Override
    public NonIOByteSource getByteSource() {
        return byteSource;
    }

}
