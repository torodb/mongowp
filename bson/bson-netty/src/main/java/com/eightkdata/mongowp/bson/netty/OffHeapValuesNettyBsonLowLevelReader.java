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

import com.eightkdata.mongowp.bson.BsonBinary;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.impl.ListBasedBsonDocument;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 *
 */
public class OffHeapValuesNettyBsonLowLevelReader extends DefaultNettyBsonLowLevelReader {

  @Inject
  public OffHeapValuesNettyBsonLowLevelReader(NettyStringReader stringReader) {
    super(stringReader);
  }

  @Override
  BsonString readString(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return new NettyBsonString(getStringReader().readStringAsSlice(byteBuf));
  }

  @Override
  BsonDocument readDocument(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    int length = byteBuf.readInt();
    int significantLenght = length - 4 - 1;

    ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

    byte b = byteBuf.readByte();
    assert b == 0x00;

    ArrayList<BsonDocument.Entry<?>> list = new ArrayList<>();
    while (significantSlice.readableBytes() > 0) {
      Entry<?> entry = readDocumentEntry(significantSlice);
      list.add(entry);
    }
    return new ListBasedBsonDocument(list);
  }

  @Override
  BsonBinary readBinary(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    int length = byteBuf.readInt();
    byte subtype = byteBuf.readByte();

    ByteBuf content = byteBuf.readSlice(length);

    return new NettyBsonBsonBinary(subtype, ParsingTools.getBinarySubtype(subtype), content);
  }
}
