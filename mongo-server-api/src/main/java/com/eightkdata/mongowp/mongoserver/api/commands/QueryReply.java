
package com.eightkdata.mongowp.mongoserver.api.commands;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.mongoserver.callback.MessageReplier;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import org.bson.BsonDocument;

/**
 *
 */
public class QueryReply implements Reply {

    private final long cursorId;
    private final int startingFrom;
    private final @Nonnull Iterable<BsonDocument> documents;
    private final @Nonnull EnumSet<ReplyMessage.Flag> flags;

    private QueryReply(
            long cursorId, 
            int startingFrom, 
            Iterable<BsonDocument> documents,
            EnumSet<ReplyMessage.Flag> flags) {
        this.cursorId = cursorId;
        this.startingFrom = startingFrom;
        this.documents = documents;
        this.flags = flags;
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

    public EnumSet<ReplyMessage.Flag> getFlags() {
        return flags;
    }
    
    @Override
    public void reply(MessageReplier replier) {
        replier.replyMessageMultipleDocuments(
                getCursorId(), 
                getStartingFrom(), 
                getDocuments()
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
                    EnumSet.noneOf(ReplyMessage.Flag.class)
            );
        }
    }
}
