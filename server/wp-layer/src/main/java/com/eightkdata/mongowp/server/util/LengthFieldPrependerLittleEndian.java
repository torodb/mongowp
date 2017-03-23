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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.ByteOrder;

/**
 *
 */
@ChannelHandler.Sharable
public class LengthFieldPrependerLittleEndian extends LengthFieldPrepender {

  public LengthFieldPrependerLittleEndian(int lengthFieldLength,
      boolean lengthIncludesLengthFieldLength) {
    super(lengthFieldLength, lengthIncludesLengthFieldLength);
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
    super.encode(ctx, msg, out.order(ByteOrder.LITTLE_ENDIAN));
  }
}
