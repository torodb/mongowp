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

import static com.torodb.mongowp.bson.utils.BsonDocumentReader.AllocationType.HEAP;
import static com.torodb.mongowp.bson.utils.BsonDocumentReader.AllocationType.OFFHEAP;
import static com.torodb.mongowp.bson.utils.BsonDocumentReader.AllocationType.OFFHEAP_VALUES;

import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.netty.annotations.Loose;
import com.torodb.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.torodb.mongowp.bson.utils.BsonDocumentReader;
import io.netty.buffer.ByteBuf;

import java.util.EnumMap;

import javax.inject.Inject;

/**
 *
 */
public class NettyBsonDocumentReader implements BsonDocumentReader<ByteBuf> {

  private final EnumMap<AllocationType, NettyBsonLowLevelReader> readerMap;

  @Inject
  public NettyBsonDocumentReader(DefaultNettyBsonLowLevelReader heapReader,
      OffHeapNettyBsonLowLevelReader offHeapReader,
      OffHeapValuesNettyBsonLowLevelReader offHeapValuesReader) {
    readerMap = new EnumMap<>(AllocationType.class);
    readerMap.put(HEAP, heapReader);
    readerMap.put(OFFHEAP, offHeapReader);
    readerMap.put(OFFHEAP_VALUES, offHeapValuesReader);
  }

  @Override
  public BsonDocument readDocument(AllocationType heapAlgorithm,
      @Loose @ModifiesIndexes ByteBuf source) throws NettyBsonReaderException {
    NettyBsonLowLevelReader reader = readerMap.get(heapAlgorithm);

    if (reader == null) {
      AllocationType previousAlgorithm = heapAlgorithm.getLessRestrictive();
      while (reader == null && previousAlgorithm != null) {
        reader = readerMap.get(heapAlgorithm);
      }
      if (reader == null) {
        throw new AssertionError("There is no reader that support " + heapAlgorithm
            + " or a less restrictive algorithm");
      }
    }
    return reader.readDocument(source);
  }
}
