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
import com.eightkdata.mongowp.messages.request.EmptyBsonContext;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.messages.request.UpdateMessage;
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
public class UpdateMessageDecoder extends AbstractMessageDecoder<UpdateMessage> {

    private final NettyStringReader stringReader;
    private final NettyBsonDocumentReader docReader;

    @Inject
    public UpdateMessageDecoder(NettyStringReader stringReader, NettyBsonDocumentReader docReader) {
        this.stringReader = stringReader;
        this.docReader = docReader;
    }

    @Override
    public @Nonnegative
    UpdateMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidNamespaceException, InvalidBsonException {
        try {
            buffer.skipBytes(4);
            String fullCollectionName = stringReader.readCString(buffer, true);
            int flags = buffer.readInt();
            BsonDocument selector = docReader.readDocument(HEAP, buffer);
            BsonDocument update = docReader.readDocument(HEAP, buffer);
            
            //TODO: improve the way database and cache are pooled
            return new UpdateMessage(
                    requestBaseMessage,
                    EmptyBsonContext.getInstance(),
                    getDatabase(fullCollectionName).intern(),
                    getCollection(fullCollectionName).intern(),
                    selector,
                    update,
                    EnumInt32FlagsUtil.isActive(Flag.UPSERT, flags),
                    EnumInt32FlagsUtil.isActive(Flag.MULTI_UPDATE, flags)
            );
        } catch (NettyBsonReaderException ex) {
            throw new InvalidBsonException(ex);
        }
    }
    
    private enum Flag implements EnumBitFlags {
        UPSERT(0),
        MULTI_UPDATE(1);

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
