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

import com.google.common.base.Preconditions;
import com.torodb.mongowp.messages.response.ReplyMessage;
import com.torodb.mongowp.server.callback.MessageReplier;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

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
