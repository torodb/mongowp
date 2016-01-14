
package com.eightkdata.mongowp.mongoserver.api.commands;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.messages.utils.SimpleIterableDocumentsProvider;
import com.eightkdata.mongowp.mongoserver.callback.MessageReplier;
import javax.annotation.Nonnull;
import org.bson.BsonDocument;

/**
 *
 */
public class QueryReply implements Reply {

    private final long cursorId;
    private final int startingFrom;
    private final @Nonnull Iterable<BsonDocument> documents;
    private final boolean cursorNotFound;
    private final boolean queryFailure;
    private final boolean shardConfigStale;
    private final boolean awaitCapable;

    public QueryReply(
            long cursorId,
            int startingFrom,
            Iterable<BsonDocument> documents,
            boolean cursorNotFound,
            boolean queryFailure,
            boolean shardConfigStale,
            boolean awaitCapable) {
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
        replier.replyMessage(
                new ReplyMessage(
                        startingFrom,
                        cursorNotFound,
                        queryFailure,
                        shardConfigStale,
                        awaitCapable,
                        cursorId,
                        startingFrom,
                        new SimpleIterableDocumentsProvider<>(documents)
                )
        );
    }
    
    public static class Builder {
        private long cursorId;
        private int startingFrom;
        private Iterable<BsonDocument> documents;

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
