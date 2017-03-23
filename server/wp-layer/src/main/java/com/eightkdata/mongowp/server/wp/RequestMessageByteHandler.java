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
package com.eightkdata.mongowp.server.wp;

import com.eightkdata.mongowp.exceptions.IllegalOperationException;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.messages.request.RequestOpCode;
import com.eightkdata.mongowp.server.decoder.BaseMessageDecoder;
import com.eightkdata.mongowp.server.decoder.MessageDecoder;
import com.eightkdata.mongowp.server.decoder.MessageDecoderLocator;
import com.eightkdata.mongowp.server.util.ChannelLittleEndianHandler;
import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.inject.Inject;

/**
 *
 */
public class RequestMessageByteHandler extends ChannelLittleEndianHandler {

  private static final Logger LOGGER = LogManager.getLogger(RequestMessageByteHandler.class);
  private static final String INVALID_OPCODE_MESSAGE = "Received and invalid message with opCode ";
  private static final String OPERATION_NOT_IMPLEMENTED =
      "Message decoder not implemented for opCode ";

  private final MessageDecoderLocator decoderLocator;

  @Inject
  public RequestMessageByteHandler(MessageDecoderLocator decoderLocator) {
    this.decoderLocator = decoderLocator;
  }

  @Override
  protected void decodeLittleEndian(
      ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> objects
  ) throws Exception {
    if (byteBuf instanceof EmptyByteBuf) {
      //TODO: This is a workaround. Check how to prevent calling decode on channel inactive
      return;
    }

    // Header
    final RequestBaseMessage requestBaseMessage = BaseMessageDecoder.decode(
        channelHandlerContext, byteBuf);
    byteBuf.skipBytes(Ints.BYTES);  // Ignore responseTo field in header
    int requestOpCodeInt = byteBuf.readInt();
    RequestOpCode requestOpCode = RequestOpCode.getByOpcode(requestOpCodeInt);
    if (null == requestOpCode) {
      LOGGER.warn(INVALID_OPCODE_MESSAGE + requestOpCodeInt);
      throw new IllegalOperationException(requestOpCodeInt);
    }

    // Body
    MessageDecoder<?> messageDecoder = decoderLocator.getByOpCode(requestOpCode);
    if (null == messageDecoder) {
      LOGGER.error(OPERATION_NOT_IMPLEMENTED + requestOpCode);
      throw new UnsupportedOperationException(OPERATION_NOT_IMPLEMENTED + requestOpCode);
    }

    objects.add(messageDecoder.decode(byteBuf, requestBaseMessage));
  }
}
