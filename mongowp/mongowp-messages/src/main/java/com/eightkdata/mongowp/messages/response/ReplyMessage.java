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


package com.eightkdata.mongowp.messages.response;

import com.eightkdata.mongowp.messages.util.EnumBitFlags;
import com.eightkdata.nettybson.api.BSONDocument;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 *
 */
@Immutable
public class ReplyMessage {
    public enum Flag implements EnumBitFlags {
        CURSOR_NOT_FOUND(0),
        QUERY_FAILURE(1),
        SHARD_CONFIG_STALE(2),
        AWAIT_CAPABLE(3);

        @Nonnegative
        private final int flagBitPosition;

        private Flag(@Nonnegative int flagBitPosition) {
            this.flagBitPosition = flagBitPosition;
        }

        @Override
        public int getFlagBitPosition() {
            return flagBitPosition;
        }
    }

    @NotThreadSafe
    public static class Builder {
        private final int requestId;
        @Nullable private EnumSet<Flag> flags;
        private final long cursorId;
        private final int startingFrom;
        @Nonnull final private List<BSONDocument> documents = new ArrayList<BSONDocument>();

        public Builder(int requestId, long cursorId, int startingFrom) {
            this.requestId = requestId;
            this.cursorId = cursorId;
            this.startingFrom = startingFrom;
        }

        public Builder(int requestId, long cursorId, int startingFrom, BSONDocument firstDocument) {
            this.requestId = requestId;
            this.cursorId = cursorId;
            this.startingFrom = startingFrom;
            documents.add(firstDocument);
        }

        public Builder setFlags(EnumSet<Flag> flags) {
            this.flags = flags;

            return this;
        }

        public Builder addFlag(Flag flag) {
            if(null == flags) {
                flags = EnumSet.of(flag);
            } else {
                flags.add(flag);
            }

            return this;
        }

        public Builder addBSONDocument(BSONDocument document) {
            documents.add(document);

            return this;
        }

        public ReplyMessage build() {
            return new ReplyMessage(requestId, flags, cursorId, startingFrom, documents);
        }
    }

    private final int responseTo;
    @Nullable private final EnumSet<Flag> flags;
    private final long cursorId;
    private final int startingFrom;
    @Nonnull final private Collection<BSONDocument> documents;

    private ReplyMessage(
            int requestId, EnumSet<Flag> flags, long cursorId, int startingFrom,
            @Nonnull Collection<BSONDocument> documents
    ) {
        this.responseTo = requestId;
        this.flags = flags;
        this.cursorId = cursorId;
        this.startingFrom = startingFrom;
        this.documents = documents;
    }

    public long getCursorId() {
        return cursorId;
    }

    public int getStartingFrom() {
        return startingFrom;
    }

    @Nullable
    public EnumSet<Flag> getFlags() {
        return flags;
    }

    public int getResponseTo() {
        return responseTo;
    }

    @Nonnull
    public Collection<BSONDocument> getDocuments() {
        return documents;
    }
}
