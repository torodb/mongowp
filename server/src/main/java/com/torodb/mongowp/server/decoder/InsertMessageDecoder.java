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
package com.torodb.mongowp.server.decoder;

import com.torodb.mongowp.bson.netty.NettyBsonDocumentReader;
import com.torodb.mongowp.bson.netty.NettyBsonReaderException;
import com.torodb.mongowp.bson.netty.NettyStringReader;
import com.torodb.mongowp.exceptions.InvalidBsonException;
import com.torodb.mongowp.exceptions.InvalidNamespaceException;
import com.torodb.mongowp.messages.request.InsertMessage;
import com.torodb.mongowp.messages.request.RequestBaseMessage;
import com.torodb.mongowp.server.util.ByteBufIterableDocumentProvider;
import com.torodb.mongowp.server.util.EnumBitFlags;
import com.torodb.mongowp.server.util.EnumInt32FlagsUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

/**
 *
 */
@ThreadSafe
public class InsertMessageDecoder extends AbstractMessageDecoder<InsertMessage> {

  private final NettyStringReader stringReader;
  private final NettyBsonDocumentReader docReader;

  @Inject
  public InsertMessageDecoder(NettyStringReader stringReader, NettyBsonDocumentReader docReader) {
    this.stringReader = stringReader;
    this.docReader = docReader;
  }

  @Override
  @SuppressFBWarnings(value = {"RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"},
      justification = "Findbugs thinks ByteBuf#readerIndex(...) has no"
      + "side effect")
  public InsertMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws
      InvalidNamespaceException, InvalidBsonException {
    try {
      MyBsonContext context = new MyBsonContext(buffer);

      int flags = buffer.readInt();
      String fullCollectionName = stringReader.readCString(buffer, true);

      ByteBuf docBuf = buffer.slice(buffer.readerIndex(), buffer.readableBytes());
      docBuf.retain();

      buffer.readerIndex(buffer.writerIndex());

      ByteBufIterableDocumentProvider documents = new ByteBufIterableDocumentProvider(docBuf,
          docReader);

      //TODO: improve the way database and cache are pooled
      return new InsertMessage(
          requestBaseMessage,
          context,
          getDatabase(fullCollectionName).intern(),
          getCollection(fullCollectionName).intern(),
          EnumInt32FlagsUtil.isActive(Flag.CONTINUE_ON_ERROR, flags),
          documents
      );
    } catch (NettyBsonReaderException ex) {
      throw new InvalidBsonException(ex);
    }
  }

  private enum Flag implements EnumBitFlags {
    CONTINUE_ON_ERROR(0);

    @Nonnegative
    private final int flagBitPosition;

    private Flag(@Nonnegative int flagBitPosition) {
      this.flagBitPosition = flagBitPosition;
    }

    @Override
    public int getFlagBitPosition() {
      return flagBitPosition;
    }
  }
}
