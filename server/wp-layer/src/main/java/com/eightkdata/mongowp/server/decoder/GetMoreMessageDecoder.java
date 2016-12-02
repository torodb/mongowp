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

package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.bson.netty.NettyBsonReaderException;
import com.eightkdata.mongowp.bson.netty.NettyStringReader;
import com.eightkdata.mongowp.exceptions.InvalidBsonException;
import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.messages.request.GetMoreMessage;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
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
