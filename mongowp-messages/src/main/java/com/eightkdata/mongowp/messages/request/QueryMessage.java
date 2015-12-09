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


package com.eightkdata.mongowp.messages.request;

import com.eightkdata.mongowp.messages.util.EnumBitFlags;
import com.eightkdata.mongowp.messages.util.EnumInt32FlagsUtil;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

/**
 *
 */
@Immutable
public class QueryMessage extends AbstractRequestMessageWithFlags<QueryMessage.Flag> implements RequestMessage {
    public enum Flag implements EnumBitFlags {
        TAILABLE_CURSOR(1),
        SLAVE_OK(2),
        OPLOG_REPLAY(3),
        NO_CURSOR_TIMEOUT(4),
        AWAIT_DATA(5),
        EXHAUST(6),
        PARTIAL(7);

        private static final int FLAG_INT32_MASK = EnumInt32FlagsUtil.getInt32AllFlagsMask(Flag.class);

        @Nonnegative private final int flagBitPosition;

        private Flag(@Nonnegative int flagBitPosition) {
            this.flagBitPosition = flagBitPosition;
        }

        @Override
        public int getFlagBitPosition() {
            return flagBitPosition;
        }
    }

    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_QUERY;

    @Override
    public RequestOpCode getOpCode() {
        return REQUEST_OP_CODE;
    }

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnegative private final int numberToSkip;
    @Nonnegative private final int numberToReturn;
    @Nonnull private final BsonDocument document;
    private final BsonDocument returnFieldsSelector;

    public QueryMessage(
            @Nonnull RequestBaseMessage requestBaseMessage, int flags, @Nonnull String fullCollectionName, int numberToSkip,
            int numberToReturn, @Nonnull BsonDocument document, BsonDocument returnFieldsSelector
    ) {
        super(requestBaseMessage, Flag.class, Flag.FLAG_INT32_MASK, flags);
        String[] splittedFullCollectionName = splitFullCollectionName(fullCollectionName);
        this.database = splittedFullCollectionName[0];
        this.collection = splittedFullCollectionName[1];
        this.numberToSkip = numberToSkip;
        this.numberToReturn = numberToReturn;
        this.document = document;
        this.returnFieldsSelector = returnFieldsSelector;
    }

    @Nonnull
    public String getDatabase() {
        return database;
    }

    @Nonnull
    public String getCollection() {
        return collection;
    }

    public int getNumberToSkip() {
        return numberToSkip;
    }

    public int getNumberToReturn() {
        return numberToReturn;
    }

    @Nonnull
    public BsonDocument getDocument() {
        return document;
    }

    public BsonDocument getReturnFieldsSelector() {
        return returnFieldsSelector;
    }

    @Override
    public String toString() {
        return "QueryMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", numberToSkip=" + numberToSkip +
                ", numberToReturn=" + numberToReturn +
                ", document=" + document +
                ", returnFieldsSelector=" + returnFieldsSelector +
                '}';
    }
}