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

import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.messages.request.KillCursorsMessage;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import io.netty.buffer.ByteBuf;

import javax.annotation.concurrent.ThreadSafe;

/**
 *
 */
@ThreadSafe
public class KillCursorsMessageDecoder implements MessageDecoder<KillCursorsMessage> {

  @Override
  public KillCursorsMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws
      InvalidNamespaceException {
    buffer.skipBytes(4);
    int numberOfCursors = buffer.readInt();
    long[] cursorIds = new long[numberOfCursors];
    for (int index = 0; index < numberOfCursors; index++) {
      cursorIds[index] = buffer.readLong();
    }

    return new KillCursorsMessage(
        requestBaseMessage, numberOfCursors, cursorIds
    );
  }
}
