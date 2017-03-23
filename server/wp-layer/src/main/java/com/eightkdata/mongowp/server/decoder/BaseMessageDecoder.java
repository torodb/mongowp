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
package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 *
 */
public class BaseMessageDecoder {

  /**
   * Method that constructs a RequestBaseMessage object based on a correctly-positioned ByteBuf.
   * This method modifies the internal state of the ByteBuf. It expects the ByteBuf to be positioned
   * just before the start of the requestId field. After running, the method will leave the ByteBuf
   * positioned just after the responseTo field, i.e., just before reading the opCode.
   *
   * @param channelHandlerContext
   * @param byteBuf
   * @return
   */
  public static RequestBaseMessage decode(ChannelHandlerContext channelHandlerContext,
      ByteBuf byteBuf) {
    InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel()
        .remoteAddress();

    return new RequestBaseMessage(socketAddress.getAddress(), socketAddress.getPort(), byteBuf
        .readInt());
  }
}
