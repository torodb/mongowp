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

package com.eightkdata.mongowp.server.util;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.netty.NettyBsonDocumentReader;
import com.eightkdata.mongowp.bson.netty.NettyBsonReaderException;
import com.eightkdata.mongowp.bson.netty.NettyBsonReaderRuntimeException;
import com.eightkdata.mongowp.bson.netty.annotations.ConservesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.UnmodifiableIterator;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 */
public final class ByteBufIterableDocumentProvider extends IterableDocumentProvider<BsonDocument> {

  private static final Logger LOGGER = LogManager.getLogger(ByteBufIterableDocumentProvider.class);
  private boolean closed = false;
  private final ByteBuf byteBuf;
  private final NettyBsonDocumentReader reader;

  public ByteBufIterableDocumentProvider(
      @Tight @ModifiesIndexes ByteBuf byteBuf,
      NettyBsonDocumentReader reader) {
    this.byteBuf = byteBuf;
    this.reader = reader;
  }

  @Override
  public FluentIterable<BsonDocument> getIterable(AllocationType algorithm) {
    return new MyIterable(algorithm, reader, byteBuf);
  }

  private static final class MyIterable extends FluentIterable<BsonDocument> {

    private final AllocationType allocationType;
    private final NettyBsonDocumentReader reader;
    private final ByteBuf buff;

    private MyIterable(AllocationType allocationType, NettyBsonDocumentReader reader,
        @Tight @ConservesIndexes ByteBuf buff) {
      this.allocationType = allocationType;
      this.reader = reader;
      this.buff = buff;
    }

    @Override
    public Iterator<BsonDocument> iterator() {
      return new MyIterator(allocationType, reader, buff.slice());
    }

  }

  private static final class MyIterator extends UnmodifiableIterator<BsonDocument> {

    private final AllocationType allocationType;
    private final NettyBsonDocumentReader reader;
    private final ByteBuf buff;

    private MyIterator(AllocationType allocationType, NettyBsonDocumentReader reader,
        @Tight @ModifiesIndexes ByteBuf buff) {
      this.allocationType = allocationType;
      this.reader = reader;
      this.buff = buff;
    }

    @Override
    public boolean hasNext() {
      return buff.readableBytes() > 0;
    }

    @Override
    public BsonDocument next() {
      try {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return reader.readDocument(allocationType, buff);
      } catch (NettyBsonReaderException ex) {
        throw new NettyBsonReaderRuntimeException(ex);
      }
    }

  }

}
