
package com.eightkdata.mongowp.mongoserver.api.commands;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.mongoserver.api.callback.MessageReplier;
import com.eightkdata.nettybson.api.BSONDocument;
import java.util.EnumSet;
import javax.annotation.Nonnull;

/**
 *
 */
public class QueryReply implements Reply {

    private final long cursorId;
    private final int startingFrom;
    private final @Nonnull Iterable<BSONDocument> documents;
    private final @Nonnull EnumSet<ReplyMessage.Flag> flags;

    private QueryReply(
            long cursorId, 
            int startingFrom, 
            Iterable<BSONDocument> documents, 
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

    public Iterable<BSONDocument> getDocuments() {
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
        private Iterable<BSONDocument> documents;

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

        public Iterable<BSONDocument> getDocuments() {
            return documents;
        }

        public Builder setDocuments(Iterable<BSONDocument> documents) {
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
