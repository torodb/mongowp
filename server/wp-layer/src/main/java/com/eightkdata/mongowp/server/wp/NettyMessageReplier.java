
package com.eightkdata.mongowp.server.wp;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This message replier writes and flush the reply to the given netty channel.
 */
public class NettyMessageReplier extends MessageReplier {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(NettyMessageReplier.class);
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
