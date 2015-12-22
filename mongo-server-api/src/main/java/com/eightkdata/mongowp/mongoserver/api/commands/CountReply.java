
package com.eightkdata.mongowp.mongoserver.api.commands;

import com.eightkdata.mongowp.mongoserver.callback.MessageReplier;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import javax.annotation.Nonnull;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;

/**
 *
 */
public class CountReply implements Reply {
    
    private final Double ok;
    private final int count;

    public CountReply(int count) {
        this.ok = MongoWP.OK;
        this.count = count;
    }
    
    public CountReply(@Nonnull Double ok) {
        assert ok != null;
        this.ok = ok;
        this.count = 0;
    }

    public Double getOk() {
        return ok;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void reply(MessageReplier replier) {
        BsonDocument result = new BsonDocument();
        if (ok.equals(MongoWP.OK)) {
            result.put("n", new BsonInt32(count));
        }
        result.put("ok", new BsonDouble(ok));
        
        replier.replyMessageNoCursor(result);
    }
}
