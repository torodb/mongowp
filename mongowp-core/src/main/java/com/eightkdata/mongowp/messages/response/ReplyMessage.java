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

import com.eightkdata.mongowp.annotations.Ethereal;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.eightkdata.mongowp.messages.request.BsonContext;
import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import com.google.common.collect.Iterables;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 */
public class ReplyMessage implements AutoCloseable {

    private final int responseTo;
    private final boolean cursorNotFound;
    private final boolean queryFailure;
    private final boolean shardConfigStale;
    private final boolean awaitCapable;
    private final long cursorId;
    private final int startingFrom;
    @Nonnull 
    @Ethereal("dataContext")
    final private IterableDocumentProvider<? extends BsonDocument> documents;
    @Nonnull private final BsonContext dataContext;

    public ReplyMessage(
            BsonContext dataContext,
            int responseTo,
            boolean cursorNotFound,
            boolean queryFailure,
            boolean shardConfigStale,
            boolean awaitCapable,
            long cursorId,
            int startingFrom,
            @Ethereal("dataContext") IterableDocumentProvider<? extends BsonDocument> documents) {
        this.dataContext = dataContext;
        this.responseTo = responseTo;
        this.cursorNotFound = cursorNotFound;
        this.queryFailure = queryFailure;
        this.shardConfigStale = shardConfigStale;
        this.awaitCapable = awaitCapable;
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

    public boolean isCursorNotFound() {
        return cursorNotFound;
    }

    public boolean isQueryFailure() {
        return queryFailure;
    }

    public boolean isShardConfigStale() {
        return shardConfigStale;
    }

    public boolean isAwaitCapable() {
        return awaitCapable;
    }

    public int getResponseTo() {
        return responseTo;
    }

    @Ethereal("this")
    public IterableDocumentProvider<? extends BsonDocument> getDocuments() {
        return documents;
    }

    @Override
    public void close() throws Exception {
        dataContext.close();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ReplyMessage{responseTo=").append(responseTo)
                .append(", cursorNotFound=").append(cursorNotFound)
                .append(", queryFailure=").append(queryFailure)
                .append(", shardConfigStale=").append(shardConfigStale)
                .append(", awaitCapable=").append(awaitCapable)
                .append(", cursorId=").append(cursorId)
                .append(", startingFrom=").append(startingFrom);


        if (dataContext.isValid()) {
            //TODO: This must be changed to preserve privacy on logs
            int docsLimit = 10;
            sb.append(", documents (limited to ").append(docsLimit).append(")=")
                    .append(
                            Iterables.toString(
                                    documents.getIterable(AllocationType.HEAP).limit(docsLimit)
                            )
                    );
        }
        else {
            sb.append(", documents=<not available>");
        }
        return sb.append('}').toString();
    }

    @NotThreadSafe
    public static class Builder {
        @Nonnull
        private final BsonContext dataContext;
        private final int requestId;
        private final long cursorId;
        private final int startingFrom;
        @Nonnull final private IterableDocumentProvider<? extends BsonDocument> documents;
        
        private boolean cursorNotFound;
        private boolean queryFailure;
        private boolean shardConfigStale;
        private boolean awaitCapable;

        public Builder(
                @Nonnull BsonContext dataContext,
                int requestId,
                long cursorId,
                int startingFrom,
                @Ethereal("dataContext") IterableDocumentProvider<? extends BsonDocument> documents) {
            this.dataContext = dataContext;
            this.requestId = requestId;
            this.cursorId = cursorId;
            this.startingFrom = startingFrom;
            this.documents = documents;
        }

        public Builder(
                @Nonnull BsonContext dataContext,
                int requestId,
                long cursorId,
                int startingFrom,
                @Ethereal("dataContext") Iterable<? extends BsonDocument> documents) {
            this(dataContext, requestId, cursorId, startingFrom, IterableDocumentProvider.of(documents));
        }

        public Builder(
                @Nonnull BsonContext dataContext,
                int requestId,
                long cursorId,
                int startingFrom,
                @Ethereal("dataContext") BsonDocument document) {
            this(dataContext, requestId, cursorId, startingFrom, IterableDocumentProvider.of(document));
        }

        public Builder setCursorNotFound(boolean cursorNotFound) {
            this.cursorNotFound = cursorNotFound;
            return this;
        }

        public Builder setQueryFailure(boolean queryFailure) {
            this.queryFailure = queryFailure;
            return this;
        }

        public Builder setShardConfigStale(boolean shardConfigStale) {
            this.shardConfigStale = shardConfigStale;
            return this;
        }

        public Builder setAwaitCapable(boolean awaitCapable) {
            this.awaitCapable = awaitCapable;
            return this;
        }

        public ReplyMessage build() {
            return new ReplyMessage(
                    dataContext,
                    requestId,
                    cursorNotFound,
                    queryFailure,
                    shardConfigStale,
                    awaitCapable,
                    cursorId,
                    startingFrom,
                    documents
            );
        }
    }
}
