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
package com.torodb.mongowp.server.wp;

import com.torodb.mongowp.messages.response.ReplyMessage;
import com.torodb.mongowp.server.encoder.ReplyMessageEncoder;
import com.torodb.mongowp.server.util.ChannelLittleEndianEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import javax.inject.Inject;

/**
 *
 */
public class ReplyMessageObjectHandler extends ChannelLittleEndianEncoder {

  private final RequestIdGenerator requestIdGenerator;

  private final ReplyMessageEncoder encoder;

  @Inject
  public ReplyMessageObjectHandler(RequestIdGenerator requestIdGenerator,
      ReplyMessageEncoder encoder) {
    this.requestIdGenerator = requestIdGenerator;
    this.encoder = encoder;
  }

  @Override
  protected void encodeLittleEndian(ChannelHandlerContext ctx, ReplyMessage message, ByteBuf out)
      throws Exception {
    encoder.encodeMessageHeader(out, message, requestIdGenerator.getNextRequestId());
    encoder.encodeMessageBody(out, message);
  }
}
