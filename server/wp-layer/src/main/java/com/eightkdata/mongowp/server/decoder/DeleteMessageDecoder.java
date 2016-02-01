/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */


package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.netty.NettyBsonDocumentReader;
import com.eightkdata.mongowp.bson.netty.NettyBsonReaderException;
import com.eightkdata.mongowp.bson.netty.NettyStringReader;
import com.eightkdata.mongowp.exceptions.InvalidBsonException;
import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.messages.request.DeleteMessage;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.server.util.EnumBitFlags;
import com.eightkdata.mongowp.server.util.EnumInt32FlagsUtil;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnegative;
import javax.inject.Inject;
import javax.inject.Singleton;

import static com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType.*;

/**
 *
 */
@Singleton
public class DeleteMessageDecoder extends AbstractMessageDecoder<DeleteMessage> {

    private final NettyStringReader stringReader;
    private final NettyBsonDocumentReader docReader;

    @Inject
    public DeleteMessageDecoder(NettyStringReader stringReader, NettyBsonDocumentReader docReader) {
        this.stringReader = stringReader;
        this.docReader = docReader;
    }

    @Override
    @Nonnegative
    public DeleteMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidNamespaceException, InvalidBsonException {
        try {
            MyBsonContext context = new MyBsonContext(buffer);

            buffer.skipBytes(4);
            String fullCollectionName = stringReader.readCString(buffer, true);
            int flags = buffer.readInt();

            BsonDocument document = docReader.readDocument(HEAP, buffer);

            //TODO: improve the way database and cache are pooled
            String database = getDatabase(fullCollectionName).intern();
            String collection = getCollection(fullCollectionName).intern();
            
            return new DeleteMessage(
                    requestBaseMessage,
                    context,
                    database,
                    collection,
                    document,
                    EnumInt32FlagsUtil.isActive(Flag.SINGLE_REMOVE, flags)
            );
        } catch (NettyBsonReaderException ex) {
            throw new InvalidBsonException(ex);
        }
    }

    private enum Flag implements EnumBitFlags {
        SINGLE_REMOVE(0);

        @Nonnegative private final int flagBitPosition;

        private Flag(@Nonnegative int flagBitPosition) {
            this.flagBitPosition = flagBitPosition;
        }

        @Override
        public int getFlagBitPosition() {
            return flagBitPosition;
        }
    }
}
