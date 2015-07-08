
package com.eightkdata.mongowp.mongoserver.callback;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DefaultMessageReplier extends MessageReplier {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(DefaultMessageReplier.class);
	/**
	 * The connectionId must be unique for each connection
	 */
    public static final AttributeKey<Integer> CONNECTION_ID = AttributeKey.valueOf("connectionId");
	/**
	 * The requestId must be unique for each request
	 */
    public static final AttributeKey<Integer> REQUEST_ID = AttributeKey.valueOf("requestId");

    private final ChannelHandlerContext channelHandlerContext;

    public DefaultMessageReplier(@Nonnull ChannelHandlerContext channelHandlerContext) {
        Preconditions.checkNotNull(channelHandlerContext);

        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public int getRequestId() {
        return channelHandlerContext.attr(REQUEST_ID).get();
    }

    @Override
    public int getConnectionId() {
    	return channelHandlerContext.attr(CONNECTION_ID).get();
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
