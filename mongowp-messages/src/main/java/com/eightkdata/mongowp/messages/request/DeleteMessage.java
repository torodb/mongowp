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
import java.util.EnumSet;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

/**
 *
 */
@Immutable
public class DeleteMessage extends AbstractRequestMessageWithFlags<DeleteMessage.Flag> implements RequestMessage {
    public enum Flag implements EnumBitFlags {
        SINGLE_REMOVE(0);

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

    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_DELETE;

    @Override
    public RequestOpCode getOpCode() {
        return REQUEST_OP_CODE;
    }

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnull private final BsonDocument document;

    public DeleteMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            EnumSet<DeleteMessage.Flag> flags,
            @Nonnull String fullCollectionName,
            @Nonnull BsonDocument document) {
        super(requestBaseMessage, flags);
        String[] splittedFullCollectionName = splitFullCollectionName(fullCollectionName);
        this.database = splittedFullCollectionName[0];
        this.collection = splittedFullCollectionName[1];
        this.document = document;
    }

    public DeleteMessage(
            @Nonnull RequestBaseMessage requestBaseMessage, int flags, @Nonnull String fullCollectionName, 
            @Nonnull BsonDocument document
    ) {
        super(requestBaseMessage, Flag.class, Flag.FLAG_INT32_MASK, flags);
        String[] splittedFullCollectionName = splitFullCollectionName(fullCollectionName);
        this.database = splittedFullCollectionName[0];
        this.collection = splittedFullCollectionName[1];
        this.document = document;
    }

    @Nonnull
    public String getDatabase() {
        return database;
    }

    @Nonnull
    public String getCollection() {
        return collection;
    }

    @Nonnull
    public BsonDocument getDocument() {
        return document;
    }

    @Override
    public String toString() {
        return "DeleteMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", document=" + document +
                '}';
    }
}