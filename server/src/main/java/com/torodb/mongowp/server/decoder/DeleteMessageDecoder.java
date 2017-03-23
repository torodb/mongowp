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

import static com.torodb.mongowp.bson.utils.BsonDocumentReader.AllocationType.HEAP;

import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.netty.NettyBsonDocumentReader;
import com.torodb.mongowp.bson.netty.NettyBsonReaderException;
import com.torodb.mongowp.bson.netty.NettyStringReader;
import com.torodb.mongowp.exceptions.InvalidBsonException;
import com.torodb.mongowp.exceptions.InvalidNamespaceException;
import com.torodb.mongowp.messages.request.DeleteMessage;
import com.torodb.mongowp.messages.request.RequestBaseMessage;
import com.torodb.mongowp.server.util.EnumBitFlags;
import com.torodb.mongowp.server.util.EnumInt32FlagsUtil;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

/**
 *
 */
@ThreadSafe
public class DeleteMessageDecoder extends AbstractMessageDecoder<DeleteMessage> {

  private final NettyStringReader stringReader;
  private final NettyBsonDocumentReader docReader;

  @Inject
  public DeleteMessageDecoder(NettyStringReader stringReader, NettyBsonDocumentReader docReader) {
    this.stringReader = stringReader;
    this.docReader = docReader;
  }

  @Override
  @Nonnegative
  public DeleteMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws
      InvalidNamespaceException, InvalidBsonException {
    try {
      MyBsonContext context = new MyBsonContext(buffer);

      buffer.skipBytes(4);
      String fullCollectionName = stringReader.readCString(buffer, true);
      int flags = buffer.readInt();

      BsonDocument document = docReader.readDocument(HEAP, buffer);

      //TODO: improve the way database and cache are pooled
      String database = getDatabase(fullCollectionName).intern();
      String collection = getCollection(fullCollectionName).intern();

      return new DeleteMessage(
          requestBaseMessage,
          context,
          database,
          collection,
          document,
          EnumInt32FlagsUtil.isActive(Flag.SINGLE_REMOVE, flags)
      );
    } catch (NettyBsonReaderException ex) {
      throw new InvalidBsonException(ex);
    }
  }

  private enum Flag implements EnumBitFlags {
    SINGLE_REMOVE(0);

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
