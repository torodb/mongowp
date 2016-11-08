/*
 * MongoWP - Mongo Server: Core
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
