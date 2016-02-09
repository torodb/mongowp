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


package com.eightkdata.mongowp.server.wp;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.server.encoder.ReplyMessageEncoder;
import com.eightkdata.mongowp.server.util.ChannelLittleEndianEncoder;
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
    public ReplyMessageObjectHandler(RequestIdGenerator requestIdGenerator, ReplyMessageEncoder encoder) {
        this.requestIdGenerator = requestIdGenerator;
        this.encoder = encoder;
    }

    @Override
    protected void encodeLittleEndian(ChannelHandlerContext ctx, ReplyMessage message, ByteBuf out) throws Exception {
        encoder.encodeMessageHeader(out, message, requestIdGenerator.getNextRequestId());
        encoder.encodeMessageBody(out, message);
    }
}
