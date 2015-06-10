/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *
 */


package com.eightkdata.nettybson.mongodriver;

import com.eightkdata.nettybson.api.BSONDocument;
import io.netty.buffer.ByteBuf;
import org.bson.*;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.Set;

/**
 *
 */
@Immutable
public class MongoBSONDocument implements BSONDocument {
    private final BSONObject bson;

    /**
     * Generates an instance reading from the ByteBuf. Advances the readerIndex of the buffer until the end of the bson
     * @param buffer
     */
    public MongoBSONDocument(ByteBuf buffer) {
        buffer.markReaderIndex();
        int documentLength = buffer.readInt();
        buffer.resetReaderIndex();
        byte[] bsonBytes = new byte[documentLength];
        buffer.readBytes(bsonBytes);

        BSONDecoder bsonDecoder = new BasicBSONDecoder();
        bson = bsonDecoder.readObject(bsonBytes);
    }

    @Override
    public Map<String, Object> asMap() {
        return bson.toMap();
    }

    public MongoBSONDocument(Map<String,Object> keyValues) {
        bson = new BasicBSONObject(keyValues);
    }

    public MongoBSONDocument(BSONObject keyValues) {
        bson = new BasicBSONObject(keyValues.toMap());
    }
    
    public BSONObject getBSONObject() {
    	return bson;
    }

    @Override
    public boolean hasKey(@Nonnull String key) {
        return bson.containsField(key);
    }

    @Override
    public Set<String> getKeys() {
        return bson.keySet();
    }

    @Override
    public Object getValue(@Nonnull String key) {
        return bson.get(key);
    }

    @Override
    public void writeToByteBuf(@Nonnull ByteBuf buffer) {
        buffer.writeBytes(new BasicBSONEncoder().encode(bson));
    }

    @Override
    public String toString() {
        return bson.toString();
    }
}
