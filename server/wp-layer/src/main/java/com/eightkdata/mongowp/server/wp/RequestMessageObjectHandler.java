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

import com.eightkdata.mongowp.messages.request.*;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import com.eightkdata.mongowp.server.wp.NettyMessageReplier;
import com.eightkdata.mongowp.server.callback.RequestProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RequestMessageObjectHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestMessageObjectHandler.class);
    
    public static final AttributeKey<RequestOpCode> REQUEST_OP_CODE = AttributeKey.valueOf("requestOpCode");

    private final RequestProcessor requestProcessor;

    @Inject
    public RequestMessageObjectHandler(RequestProcessor requestProcessor) {
        this.requestProcessor = requestProcessor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try (RequestMessage requestMessage = (RequestMessage) msg) {
            ctx.attr(NettyMessageReplier.REQUEST_ID).set(requestMessage.getBaseMessage().getRequestId());
            LOGGER.debug("Received message type: {}, data: {}", requestMessage.getOpCode(), requestMessage);

            MessageReplier messageReplier = new NettyMessageReplier(ctx);
            ctx.attr(REQUEST_OP_CODE).set(requestMessage.getOpCode());
            switch (requestMessage.getOpCode()) {
                case OP_QUERY:
                    assert requestMessage instanceof QueryMessage;
                    requestProcessor.queryMessage((QueryMessage) requestMessage, messageReplier);
                    break;
                case OP_GET_MORE:
                    assert requestMessage instanceof GetMoreMessage;
                    requestProcessor.getMore((GetMoreMessage) requestMessage, messageReplier);
                    break;
                case OP_KILL_CURSORS:
                    assert requestMessage instanceof KillCursorsMessage;
                    requestProcessor.killCursors((KillCursorsMessage) requestMessage, messageReplier);
                    break;
                case OP_INSERT:
                    assert requestMessage instanceof InsertMessage;
                    requestProcessor.insert((InsertMessage) requestMessage, messageReplier);
                    break;
                case OP_UPDATE:
                    assert requestMessage instanceof UpdateMessage;
                    requestProcessor.update((UpdateMessage) requestMessage, messageReplier);
                    break;
                case OP_DELETE:
                    assert requestMessage instanceof DeleteMessage;
                    requestProcessor.delete((DeleteMessage) requestMessage, messageReplier);
                    break;
                default:
                    throw new UnsupportedOperationException(
                            "Message replier not implemented for "
                            + requestMessage.getOpCode() + " opCode"
                    );
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Error while processing request", cause);

        MessageReplier messageReplier = new NettyMessageReplier(ctx);
        requestProcessor.handleError(ctx.attr(REQUEST_OP_CODE).get(), messageReplier, cause);
    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		requestProcessor.onChannelActive(ctx);
		
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		
		requestProcessor.onChannelInactive(ctx);
	}
}
