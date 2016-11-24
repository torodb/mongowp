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

import com.eightkdata.mongowp.bson.netty.annotations.ConservesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.Retains;
import com.eightkdata.mongowp.messages.request.BsonContext;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public class MyBsonContext implements BsonContext {

  private boolean closed = false;
  private final ByteBuf byteBuf;

  public MyBsonContext(@Loose @Retains @ConservesIndexes ByteBuf byteBuf) {
    this.byteBuf = byteBuf.retain();
  }

  @Override
  public void close() {
    if (!closed) {
      closed = true;
      this.byteBuf.release();
    }
  }

  @Override
  public boolean isValid() {
    return !closed;
  }

}
