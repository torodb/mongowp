
package com.eightkdata.mongowp.mongoserver.api.commands;

import com.eightkdata.mongowp.mongoserver.api.callback.MessageReplier;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.nettybson.mongodriver.MongoBSONDocument;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nonnull;

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
        Map<String, Object> keyValues = Maps.newHashMapWithExpectedSize(2);
        if (ok.equals(MongoWP.OK)) {
            keyValues.put("n", count);
        }
        keyValues.put("ok", ok);
        
        replier.replyMessageNoCursor(new MongoBSONDocument(keyValues));
    }
}
