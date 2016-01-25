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

import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOption;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.mongoserver.util.ByteBufUtil;
import com.eightkdata.mongowp.mongoserver.util.EnumBitFlags;
import com.eightkdata.mongowp.mongoserver.util.EnumInt32FlagsUtil;
import io.netty.buffer.ByteBuf;
import java.util.EnumSet;
import javax.annotation.Nonnegative;
import javax.inject.Singleton;
import org.bson.BsonDocument;

/**
 *
 */
@Singleton
public class QueryMessageDecoder extends AbstractMessageDecoder<QueryMessage> {
    @Override
    public @Nonnegative
    QueryMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidNamespaceException {
        int flags = buffer.readInt();
        String fullCollectionName = ByteBufUtil.readCString(buffer);
        int numberToSkip = buffer.readInt();
        int numberToReturn = buffer.readInt();
        BsonDocument document = ByteBufUtil.readBsonDocument(buffer);
        BsonDocument returnFieldsSelector = buffer.readableBytes() > 0 ? ByteBufUtil.readBsonDocument(buffer) : null;

        EnumSet<QueryOption> qoSet = EnumSet.noneOf(QueryOption.class);
        if (EnumInt32FlagsUtil.isActive(Flag.TAILABLE_CURSOR, flags)) {
            qoSet.add(QueryOption.TAILABLE_CURSOR);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.SLAVE_OK, flags)) {
            qoSet.add(QueryOption.SLAVE_OK);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.OPLOG_REPLAY, flags)) {
            qoSet.add(QueryOption.OPLOG_REPLAY);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.NO_CURSOR_TIMEOUT, flags)) {
            qoSet.add(QueryOption.NO_CURSOR_TIMEOUT);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.AWAIT_DATA, flags)) {
            qoSet.add(QueryOption.AWAIT_DATA);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.EXHAUST, flags)) {
            qoSet.add(QueryOption.EXHAUST);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.PARTIAL, flags)) {
            qoSet.add(QueryOption.PARTIAL);
        }

        return new QueryMessage(
                requestBaseMessage, 
                getDatabase(fullCollectionName),
                getCollection(fullCollectionName),
                numberToSkip,
                numberToReturn,
                new QueryOptions(qoSet),
                document,
                returnFieldsSelector
        );
    }

    private enum Flag implements EnumBitFlags {
        TAILABLE_CURSOR(1),
        SLAVE_OK(2),
        OPLOG_REPLAY(3),
        NO_CURSOR_TIMEOUT(4),
        AWAIT_DATA(5),
        EXHAUST(6),
        PARTIAL(7);

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
