/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.server.callback;

import com.torodb.mongowp.messages.response.ReplyMessage;
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
