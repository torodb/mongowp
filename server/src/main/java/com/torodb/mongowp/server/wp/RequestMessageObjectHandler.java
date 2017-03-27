/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.server.wp;

import static com.torodb.mongowp.messages.request.RequestOpCode.OP_DELETE;
import static com.torodb.mongowp.messages.request.RequestOpCode.OP_GET_MORE;
import static com.torodb.mongowp.messages.request.RequestOpCode.OP_INSERT;
import static com.torodb.mongowp.messages.request.RequestOpCode.OP_KILL_CURSORS;
import static com.torodb.mongowp.messages.request.RequestOpCode.OP_QUERY;
import static com.torodb.mongowp.messages.request.RequestOpCode.OP_UPDATE;

import com.torodb.mongowp.messages.request.DeleteMessage;
import com.torodb.mongowp.messages.request.GetMoreMessage;
import com.torodb.mongowp.messages.request.InsertMessage;
import com.torodb.mongowp.messages.request.KillCursorsMessage;
import com.torodb.mongowp.messages.request.QueryMessage;
import com.torodb.mongowp.messages.request.RequestMessage;
import com.torodb.mongowp.messages.request.RequestOpCode;
import com.torodb.mongowp.messages.request.UpdateMessage;
import com.torodb.mongowp.server.callback.MessageReplier;
import com.torodb.mongowp.server.callback.RequestProcessor;
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
      LOGGER.debug("Received message type: {}, data: {}", 
          requestMessage.getOpCode(), requestMessage);

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
