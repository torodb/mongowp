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
package com.eightkdata.mongowp.server.util;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteOrder;

/**
 *
 */
public abstract class ChannelLittleEndianEncoder extends MessageToByteEncoder<ReplyMessage> {

  @Override
  protected void encode(ChannelHandlerContext ctx, ReplyMessage msg, ByteBuf out) throws Exception {
    encodeLittleEndian(ctx, msg, out.order(ByteOrder.LITTLE_ENDIAN));
  }

  protected abstract void encodeLittleEndian(ChannelHandlerContext ctx, ReplyMessage msg,
      ByteBuf out)
      throws Exception;
}
