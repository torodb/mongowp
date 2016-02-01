
package com.eightkdata.mongowp.server.callback;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import io.netty.util.AttributeMap;

/**
 * This message replier stores the recived reply to be consumed later.
 */
public class PojoMessageReplier extends MessageReplier {
    private final int requestId;
    private final AttributeMap attributeMap;
    private ReplyMessage reply;

    public PojoMessageReplier(int requestId, AttributeMap attributeMap) {
        this.requestId = requestId;
        this.attributeMap = attributeMap;
    }

    public ReplyMessage getReply() {
        return reply;
    }

    @Override
    public void replyMessage(ReplyMessage replyMessage) {
        reply = replyMessage;
    }

    @Override
    public int getRequestId() {
        return requestId;
    }

    @Override
    public AttributeMap getAttributeMap() {
        return attributeMap;
    }

}
