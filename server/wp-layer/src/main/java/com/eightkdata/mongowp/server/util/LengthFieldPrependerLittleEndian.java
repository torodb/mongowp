/*
 * MongoWP - Mongo Server: Wire Protocol Layer
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
    public LengthFieldPrependerLittleEndian(int lengthFieldLength, boolean lengthIncludesLengthFieldLength) {
        super(lengthFieldLength, lengthIncludesLengthFieldLength);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        super.encode(ctx, msg, out.order(ByteOrder.LITTLE_ENDIAN));
    }
}
