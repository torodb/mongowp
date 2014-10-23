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


package com.eightkdata.mongowp.mongoserver;

import com.eightkdata.mongowp.messages.request.*;
import com.eightkdata.mongowp.mongoserver.api.callback.MessageReplier;
import com.eightkdata.mongowp.mongoserver.api.callback.RequestProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

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
        RequestMessage requestMessage = (RequestMessage) msg;
    	ctx.attr(MessageReplier.REQUEST_ID).set(requestMessage.getBaseMessage().getRequestId());
        LOGGER.debug("Received message type: {}, data: {}", requestMessage.getOpCode(), requestMessage);

        MessageReplier messageReplier = new MessageReplier(ctx);
    	ctx.attr(REQUEST_OP_CODE).set(requestMessage.getOpCode());
        switch (requestMessage.getOpCode()) {
	        case OP_QUERY:
	            requestProcessor.queryMessage((QueryMessage) requestMessage, messageReplier);
	            break;
	        case OP_GET_MORE:
	            requestProcessor.getMore((GetMoreMessage) requestMessage, messageReplier);
	            break;
	        case OP_KILL_CURSORS:
	            requestProcessor.killCursors((KillCursorsMessage) requestMessage, messageReplier);
	            break;
	        case OP_INSERT:
	            requestProcessor.insert((InsertMessage) requestMessage, messageReplier);
	            break;
	        case OP_UPDATE:
	            requestProcessor.update((UpdateMessage) requestMessage, messageReplier);
	            break;
	        case OP_DELETE:
	            requestProcessor.delete((DeleteMessage) requestMessage, messageReplier);
	            break;
            // TODO: implement missing cases
            default:
                throw new UnsupportedOperationException(
                        "Message replier not implemented for " + requestMessage.getOpCode() + " opCode"
                );
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("Error while processing request", cause);

        MessageReplier messageReplier = new MessageReplier(ctx);
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
