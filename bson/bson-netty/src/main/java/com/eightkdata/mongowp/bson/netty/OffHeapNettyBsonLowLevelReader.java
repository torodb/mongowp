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

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import io.netty.buffer.ByteBuf;
import javax.inject.Inject;

/**
 *
 */
public class OffHeapNettyBsonLowLevelReader extends OffHeapValuesNettyBsonLowLevelReader {

    @Inject
    public OffHeapNettyBsonLowLevelReader(NettyStringReader stringReader) {
        super(stringReader);
    }

    @Override
    BsonDocument readDocument(@Loose @ModifiesIndexes ByteBuf byteBuf)
            throws NettyBsonReaderException {
        int length = byteBuf.readInt();
        int significantLenght = length - 4; //the final 0x00 must be included

        ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

        assert byteBuf.getByte(byteBuf.readerIndex() - 1) == 0x00;

        return new IterableNettyBsonDocument(significantSlice, this);
    }

    @Override
    BsonArray readArray(@Loose @ModifiesIndexes ByteBuf byteBuf)
            throws NettyBsonReaderException {
        int length = byteBuf.readInt();
        int significantLenght = length - 4; //the final 0x00 must be included

        ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

        assert byteBuf.getByte(byteBuf.readerIndex() - 1) == 0x00;

        return new IterableNettyBsonArray(significantSlice, this);
    }
}
