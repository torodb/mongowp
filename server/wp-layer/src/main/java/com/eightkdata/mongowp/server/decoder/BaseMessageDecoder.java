/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
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
     * This method modifies the internal state of the ByteBuf.
     * It expects the ByteBuf to be positioned just before the start of the requestId field.
     * After running, the method will leave the ByteBuf positioned just after the responseTo field,
     * i.e., just before reading the opCode.
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @return
     */
    public static RequestBaseMessage decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        InetSocketAddress socketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();

        return new RequestBaseMessage(socketAddress.getAddress(), socketAddress.getPort(), byteBuf.readInt());
    }
}
