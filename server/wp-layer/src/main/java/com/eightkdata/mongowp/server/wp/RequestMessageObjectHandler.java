/*
 * MongoWP
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.server.wp;

import static com.eightkdata.mongowp.messages.request.RequestOpCode.OP_DELETE;
import static com.eightkdata.mongowp.messages.request.RequestOpCode.OP_GET_MORE;
import static com.eightkdata.mongowp.messages.request.RequestOpCode.OP_INSERT;
import static com.eightkdata.mongowp.messages.request.RequestOpCode.OP_KILL_CURSORS;
import static com.eightkdata.mongowp.messages.request.RequestOpCode.OP_QUERY;
import static com.eightkdata.mongowp.messages.request.RequestOpCode.OP_UPDATE;

import com.eightkdata.mongowp.messages.request.DeleteMessage;
import com.eightkdata.mongowp.messages.request.GetMoreMessage;
import com.eightkdata.mongowp.messages.request.InsertMessage;
import com.eightkdata.mongowp.messages.request.KillCursorsMessage;
import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.messages.request.RequestMessage;
import com.eightkdata.mongowp.messages.request.RequestOpCode;
import com.eightkdata.mongowp.messages.request.UpdateMessage;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import com.eightkdata.mongowp.server.callback.RequestProcessor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 *
 */
@ChannelHandler.Sharable
public class RequestMessageObjectHandler extends ChannelInboundHandlerAdapter {

  private static final Logger LOGGER = LogManager.getLogger(RequestMessageObjectHandler.class);

  public static final AttributeKey<RequestOpCode> REQUEST_OP_CODE = AttributeKey.valueOf(
      "requestOpCode");

  private final RequestProcessor requestProcessor;

  @Inject
  public RequestMessageObjectHandler(RequestProcessor requestProcessor) {
    this.requestProcessor = requestProcessor;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    try (RequestMessage requestMessage = (RequestMessage) msg) {
      ctx.attr(NettyMessageReplier.REQUEST_ID).set(requestMessage.getBaseMessage().getRequestId());
      LOGGER
          .debug("Received message type: {}, data: {}", requestMessage.getOpCode(), requestMessage);

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
    logCaughtException(cause);

    MessageReplier messageReplier = new NettyMessageReplier(ctx);
    requestProcessor.handleError(ctx.attr(REQUEST_OP_CODE).get(), messageReplier, cause);
  }

  private void logCaughtException(Throwable cause) {
    String message = "Error while processing request";
    if (cause.getMessage() != null) {
      message += " (" + cause.getMessage() + ")";
    }

    LOGGER.error(message, cause);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    try {
      LOGGER.info("Connection established from " + ctx.channel().remoteAddress());
    } catch (Exception e) {
      LOGGER.debug("Exception raised while logging connection", e);
    }

    requestProcessor.onChannelActive(ctx);

    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    try {
      LOGGER.info("Connection finished from " + ctx.channel().remoteAddress());
    } catch (Exception e) {
      LOGGER.debug("Exception raised while logging disconnection", e);
    }

    super.channelInactive(ctx);

    requestProcessor.onChannelInactive(ctx);
  }
}
