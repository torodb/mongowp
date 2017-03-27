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
import com.torodb.mongowp.messages.request.EmptyBsonContext;
import com.torodb.mongowp.messages.request.RequestBaseMessage;
import com.torodb.mongowp.messages.request.UpdateMessage;
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
public class UpdateMessageDecoder extends AbstractMessageDecoder<UpdateMessage> {

  private final NettyStringReader stringReader;
  private final NettyBsonDocumentReader docReader;

  @Inject
  public UpdateMessageDecoder(NettyStringReader stringReader, NettyBsonDocumentReader docReader) {
    this.stringReader = stringReader;
    this.docReader = docReader;
  }

  @Override
  public UpdateMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws
      InvalidNamespaceException, InvalidBsonException {
    try {
      buffer.skipBytes(4);
      String fullCollectionName = stringReader.readCString(buffer, true);
      int flags = buffer.readInt();
      BsonDocument selector = docReader.readDocument(HEAP, buffer);
      BsonDocument update = docReader.readDocument(HEAP, buffer);

      //TODO: improve the way database and cache are pooled
      return new UpdateMessage(
          requestBaseMessage,
          EmptyBsonContext.getInstance(),
          getDatabase(fullCollectionName).intern(),
          getCollection(fullCollectionName).intern(),
          selector,
          update,
          EnumInt32FlagsUtil.isActive(Flag.UPSERT, flags),
          EnumInt32FlagsUtil.isActive(Flag.MULTI_UPDATE, flags)
      );
    } catch (NettyBsonReaderException ex) {
      throw new InvalidBsonException(ex);
    }
  }

  private enum Flag implements EnumBitFlags {
    UPSERT(0),
    MULTI_UPDATE(1);

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
