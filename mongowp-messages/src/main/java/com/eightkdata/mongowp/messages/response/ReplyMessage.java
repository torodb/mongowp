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

import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import com.eightkdata.mongowp.messages.utils.SimpleIterableDocumentsProvider;
import com.google.common.collect.FluentIterable;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import org.bson.BsonDocument;

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
    @Nonnull final private IterableDocumentProvider<? extends BsonDocument> documents;
    private boolean close;

    public ReplyMessage(
            int responseTo,
            boolean cursorNotFound,
            boolean queryFailure,
            boolean shardConfigStale,
            boolean awaitCapable,
            long cursorId,
            int startingFrom,
            IterableDocumentProvider<? extends BsonDocument> documents) {
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

    @Nonnull
    public FluentIterable<? extends BsonDocument> getDocuments() {
        return documents;
    }

    @Override
    public void close() throws Exception {
        if (!close) {
            close = true;
            documents.close();
        }
    }

    @Override
    public String toString() {
        return "ReplyMessage{" + "responseTo=" + responseTo + ", cursorNotFound=" +
                cursorNotFound + ", queryFailure=" + queryFailure +
                ", shardConfigStale=" + shardConfigStale + ", awaitCapable=" +
                awaitCapable + ", cursorId=" + cursorId + ", startingFrom=" +
                startingFrom + ", documents=" + documents + '}';
    }

    @NotThreadSafe
    public static class Builder {
        private final int requestId;
        private final long cursorId;
        private final int startingFrom;
        @Nonnull final private IterableDocumentProvider documents;
        
        private boolean cursorNotFound;
        private boolean queryFailure;
        private boolean shardConfigStale;
        private boolean awaitCapable;

        public Builder(
                int requestId,
                long cursorId,
                int startingFrom,
                IterableDocumentProvider<? extends BsonDocument> documents) {
            this.requestId = requestId;
            this.cursorId = cursorId;
            this.startingFrom = startingFrom;
            this.documents = documents;
        }

        public Builder(
                int requestId,
                long cursorId,
                int startingFrom,
                Iterable<? extends BsonDocument> documents) {
            this(requestId, cursorId, startingFrom, new SimpleIterableDocumentsProvider<>(documents));
        }

        public Builder(
                int requestId,
                long cursorId,
                int startingFrom,
                BsonDocument document) {
            this(requestId, cursorId, startingFrom, new SimpleIterableDocumentsProvider<>(Collections.singleton(document)));
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
