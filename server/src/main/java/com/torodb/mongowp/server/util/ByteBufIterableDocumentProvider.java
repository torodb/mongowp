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
package com.torodb.mongowp.server.util;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.UnmodifiableIterator;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.netty.NettyBsonDocumentReader;
import com.torodb.mongowp.bson.netty.NettyBsonReaderException;
import com.torodb.mongowp.bson.netty.NettyBsonReaderRuntimeException;
import com.torodb.mongowp.bson.netty.annotations.ConservesIndexes;
import com.torodb.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.torodb.mongowp.bson.netty.annotations.Tight;
import com.torodb.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.torodb.mongowp.messages.utils.IterableDocumentProvider;
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
