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


package com.eightkdata.mongowp.mongoserver.decoder;

import com.eightkdata.mongowp.messages.request.DeleteMessage;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.mongoserver.util.EnumBitFlags;
import com.eightkdata.mongowp.mongoserver.util.EnumInt32FlagsUtil;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.mongoserver.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnegative;
import javax.inject.Singleton;
import org.bson.BsonDocument;

/**
 *
 */
@Singleton
public class DeleteMessageDecoder extends AbstractMessageDecoder<DeleteMessage> {
    @Override
    @Nonnegative
    public DeleteMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidNamespaceException {
    	buffer.skipBytes(4);
        String fullCollectionName = ByteBufUtil.readCString(buffer);
        int flags = buffer.readInt();
        BsonDocument document = ByteBufUtil.readBsonDocument(buffer);

        String database = getDatabase(fullCollectionName);
        String collection = getCollection(database);

        return new DeleteMessage(
                requestBaseMessage,
                database,
                collection,
                document,
                EnumInt32FlagsUtil.isActive(Flag.SINGLE_REMOVE, flags)
        );
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
