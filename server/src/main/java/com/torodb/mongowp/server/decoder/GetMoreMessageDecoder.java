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

import com.torodb.mongowp.bson.netty.NettyBsonReaderException;
import com.torodb.mongowp.bson.netty.NettyStringReader;
import com.torodb.mongowp.exceptions.InvalidBsonException;
import com.torodb.mongowp.exceptions.InvalidNamespaceException;
import com.torodb.mongowp.messages.request.GetMoreMessage;
import com.torodb.mongowp.messages.request.RequestBaseMessage;
import io.netty.buffer.ByteBuf;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

/**
 *
 */
@ThreadSafe
public class GetMoreMessageDecoder extends AbstractMessageDecoder<GetMoreMessage> {

  private final NettyStringReader stringReader;

  @Inject
  public GetMoreMessageDecoder(NettyStringReader stringReader) {
    this.stringReader = stringReader;
  }

  @Override
  public GetMoreMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws
      InvalidNamespaceException, InvalidBsonException {
    try {
      buffer.skipBytes(4);
      String fullCollectionName = stringReader.readCString(buffer, true);
      int numberToReturn = buffer.readInt();
      long cursorId = buffer.readLong();

      //TODO: improve the way database and cache are pooled
      return new GetMoreMessage(
          requestBaseMessage,
          getDatabase(fullCollectionName).intern(),
          getCollection(fullCollectionName).intern(),
          numberToReturn,
          cursorId
      );
    } catch (NettyBsonReaderException ex) {
      throw new InvalidBsonException(ex);
    }
  }
}
