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
package com.eightkdata.mongowp.server.wp;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This message replier writes and flush the reply to the given netty channel.
 */
public class NettyMessageReplier extends MessageReplier {
    private static final Logger LOGGER = LogManager.getLogger(NettyMessageReplier.class);
	/**
	 * The requestId must be unique for each request
	 */
    public static final AttributeKey<Integer> REQUEST_ID = AttributeKey.valueOf("requestId");

    private final ChannelHandlerContext channelHandlerContext;

    public NettyMessageReplier(@Nonnull ChannelHandlerContext channelHandlerContext) {
        Preconditions.checkNotNull(channelHandlerContext);

        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public int getRequestId() {
        return channelHandlerContext.attr(REQUEST_ID).get();
    }

    @Override
    public AttributeMap getAttributeMap() {
    	return channelHandlerContext;
    }

    @Override
    public void replyMessage(ReplyMessage replyMessage) {
        channelHandlerContext.writeAndFlush(replyMessage);
        LOGGER.debug("Replying " + replyMessage);
    }

}
