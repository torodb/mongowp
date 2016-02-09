/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bson-netty. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader;
import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import javax.inject.Inject;

import static com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType.*;

/**
 *
 */
public class NettyBsonDocumentReader implements BsonDocumentReader<ByteBuf> {

    private final EnumMap<AllocationType, NettyBsonLowLevelReader> readerMap;

    @Inject
    public NettyBsonDocumentReader(
            DefaultNettyBsonLowLevelReader heapReader,
            OffHeapNettyBsonLowLevelReader offHeapReader,
            OffHeapValuesNettyBsonLowLevelReader offHeapValuesReader) {
        readerMap = new EnumMap<>(AllocationType.class);
        readerMap.put(HEAP, heapReader);
        readerMap.put(OFFHEAP, offHeapReader);
        readerMap.put(OFFHEAP_VALUES, offHeapValuesReader);
    }

    @Override
    public BsonDocument readDocument(AllocationType heapAlgorithm, @Loose @ModifiesIndexes ByteBuf source) throws NettyBsonReaderException {
        NettyBsonLowLevelReader reader = readerMap.get(heapAlgorithm);

        if (reader == null) {
            AllocationType previousAlgorithm = heapAlgorithm.getLessRestrictive();
            while (reader == null && previousAlgorithm != null) {
                reader = readerMap.get(heapAlgorithm);
            }
            if (reader == null) {
                throw new AssertionError("There is no reader that support " + heapAlgorithm + " or a less restrictive algorithm");
            }
        }
        return reader.readDocument(source);
    }
 }
