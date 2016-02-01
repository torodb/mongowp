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

    protected abstract void encodeLittleEndian(ChannelHandlerContext ctx, ReplyMessage msg, ByteBuf out)
    throws Exception;
}
