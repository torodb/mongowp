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
package com.torodb.mongowp.bson.netty;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.abst.AbstractIterableBasedBsonArray;
import com.torodb.mongowp.bson.impl.ListBsonArray;
import com.torodb.mongowp.bson.netty.annotations.Loose;
import com.torodb.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.torodb.mongowp.bson.netty.annotations.Tight;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;

import java.io.ObjectStreamException;
import java.util.NoSuchElementException;

/**
 *
 */
@SuppressFBWarnings(value = {"SE_BAD_FIELD", "SE_NO_SERIALVERSIONID"},
    justification = "writeReplace is used")
public class IterableNettyBsonArray extends AbstractIterableBasedBsonArray {

  @Loose
  private final ByteBuf byteBuf;
  private final OffHeapNettyBsonLowLevelReader offHeapReader;

  /**
   *
   * @param byteBuf       it must include the final 0x00
   * @param offHeapReader
   */
  public IterableNettyBsonArray(@Loose @ModifiesIndexes ByteBuf byteBuf,
      OffHeapNettyBsonLowLevelReader offHeapReader) {
    this.byteBuf = byteBuf;
    this.offHeapReader = offHeapReader;
  }

  @Override
  public UnmodifiableIterator<BsonValue<?>> iterator() {
    return new MyIterator(byteBuf.slice(), offHeapReader);
  }

  private Object writeReplace() throws ObjectStreamException {
    return new ListBsonArray(Lists.newArrayList(this));
  }

  private static class MyIterator extends UnmodifiableIterator<BsonValue<?>> {

    @Tight
    private final ByteBuf byteBuf;
    private final NettyBsonLowLevelReader reader;

    public MyIterator(ByteBuf byteBuf, NettyBsonLowLevelReader reader) {
      this.byteBuf = byteBuf;
      this.reader = reader;
    }

    @Override
    public boolean hasNext() {
      return reader.hasNext(byteBuf);
    }

    @Override
    public BsonValue<?> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      try {
        return reader.readArrayEntry(byteBuf);
      } catch (NettyBsonReaderException ex) {
        throw new RuntimeException(ex);
      }
    }

  }

}
