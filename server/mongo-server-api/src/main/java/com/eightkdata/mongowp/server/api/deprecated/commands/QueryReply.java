
package com.eightkdata.mongowp.server.api.deprecated.commands;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.messages.request.BsonContext;
import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import javax.annotation.Nonnull;

/**
 *
 */
public class QueryReply implements Reply {

    private final BsonContext dataContext;
    private final long cursorId;
    private final int startingFrom;
    private final @Nonnull Iterable<BsonDocument> documents;
    private final boolean cursorNotFound;
    private final boolean queryFailure;
    private final boolean shardConfigStale;
    private final boolean awaitCapable;

    public QueryReply(
            BsonContext dataContext,
            long cursorId,
            int startingFrom,
            Iterable<BsonDocument> documents,
            boolean cursorNotFound,
            boolean queryFailure,
            boolean shardConfigStale,
            boolean awaitCapable) {
        this.dataContext = dataContext;
        this.cursorId = cursorId;
        this.startingFrom = startingFrom;
        this.documents = documents;
        this.cursorNotFound = cursorNotFound;
        this.queryFailure = queryFailure;
        this.shardConfigStale = shardConfigStale;
        this.awaitCapable = awaitCapable;
    }

    public long getCursorId() {
        return cursorId;
    }

    public int getStartingFrom() {
        return startingFrom;
    }

    public Iterable<BsonDocument> getDocuments() {
        return documents;
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

    @Override
    public void reply(MessageReplier replier) {
        replier.replyMessage(new ReplyMessage(
                        dataContext,
                        startingFrom,
                        cursorNotFound,
                        queryFailure,
                        shardConfigStale,
                        awaitCapable,
                        cursorId,
                        startingFrom,
                        IterableDocumentProvider.of(documents)
                )
        );
    }
    
    public static class Builder {
        private final BsonContext dataContext;
        private long cursorId;
        private int startingFrom;
        private Iterable<BsonDocument> documents;

        public Builder(BsonContext dataContext) {
            this.dataContext = dataContext;
        }

        public long getCursorId() {
            return cursorId;
        }

        public Builder setCursorId(long cursorId) {
            this.cursorId = cursorId;
            return this;
        }

        public int getStartingFrom() {
            return startingFrom;
        }

        public Builder setStartingFrom(int startingFrom) {
            this.startingFrom = startingFrom;
            return this;
        }

        public Iterable<BsonDocument> getDocuments() {
            return documents;
        }

        public Builder setDocuments(Iterable<BsonDocument> documents) {
            this.documents = documents;
            return this;
        }
        
        public QueryReply build() {
            return new QueryReply(
                    dataContext,
                    cursorId, 
                    startingFrom, 
                    documents, 
                    false,
                    false,
                    false,
                    false
            );
        }
    }
}
