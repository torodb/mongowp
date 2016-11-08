/*
 * MongoWP - MongoWP: Bson Netty
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.BsonBinary;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.impl.ListBasedBsonDocument;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 *
 */
public class OffHeapValuesNettyBsonLowLevelReader extends DefaultNettyBsonLowLevelReader {

    @Inject
    public OffHeapValuesNettyBsonLowLevelReader(NettyStringReader stringReader) {
        super(stringReader);
    }

    @Override
    BsonString readString(@Loose @ModifiesIndexes ByteBuf byteBuf) {
        return new NettyBsonString(getStringReader().readStringAsSlice(byteBuf));
    }

    @Override
    BsonDocument readDocument(@Loose @ModifiesIndexes ByteBuf byteBuf)
            throws NettyBsonReaderException {
        int length = byteBuf.readInt();
        int significantLenght = length - 4 - 1;

        ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

        byte b = byteBuf.readByte();
        assert b == 0x00;

        ArrayList<BsonDocument.Entry<?>> list = new ArrayList<>();
        while (significantSlice.readableBytes() > 0) {
            Entry<?> entry = readDocumentEntry(significantSlice);
            list.add(entry);
        }
        return new ListBasedBsonDocument(list);
    }

    @Override
    BsonBinary readBinary(@Loose @ModifiesIndexes ByteBuf byteBuf) {
        int length = byteBuf.readInt();
        byte subtype = byteBuf.readByte();

        ByteBuf content = byteBuf.readSlice(length);

        return new NettyBsonBsonBinary(subtype, ParsingTools.getBinarySubtype(subtype), content);
    }
}
