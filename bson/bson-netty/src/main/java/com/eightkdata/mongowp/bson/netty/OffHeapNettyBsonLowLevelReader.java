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
package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import io.netty.buffer.ByteBuf;

import javax.inject.Inject;

/**
 *
 */
public class OffHeapNettyBsonLowLevelReader extends OffHeapValuesNettyBsonLowLevelReader {

  @Inject
  public OffHeapNettyBsonLowLevelReader(NettyStringReader stringReader) {
    super(stringReader);
  }

  @Override
  BsonDocument readDocument(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    int length = byteBuf.readInt();
    int significantLenght = length - 4; //the final 0x00 must be included

    ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

    assert byteBuf.getByte(byteBuf.readerIndex() - 1) == 0x00;

    return new IterableNettyBsonDocument(significantSlice, this);
  }

  @Override
  BsonArray readArray(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    int length = byteBuf.readInt();
    int significantLenght = length - 4; //the final 0x00 must be included

    ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

    assert byteBuf.getByte(byteBuf.readerIndex() - 1) == 0x00;

    return new IterableNettyBsonArray(significantSlice, this);
  }
}
