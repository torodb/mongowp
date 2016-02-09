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

import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.*;
import com.eightkdata.mongowp.bson.abst.AbstractBsonDocument.SimpleEntry;
import com.eightkdata.mongowp.bson.netty.annotations.ConservesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import io.netty.buffer.ByteBuf;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eightkdata.mongowp.bson.BsonType.*;

/**
 *
 */
public abstract class NettyBsonLowLevelReader {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(NettyBsonLowLevelReader.class);
    
    private final NettyStringReader stringReader;

    @Inject
    public NettyBsonLowLevelReader(NettyStringReader stringReader) {
        this.stringReader = stringReader;
    }

    protected NettyStringReader getStringReader() {
        return stringReader;
    }

    public BsonValue<?> readArrayEntry(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException {
        BsonType bsonType = ParsingTools.getBsonType(byteBuf.readByte());
        stringReader.skipCString(byteBuf);
        switch (bsonType) {
            case ARRAY: return readArray(byteBuf);
            case BINARY: return readBinary(byteBuf);
            case DATETIME: return readDateTime(byteBuf);
            case DB_POINTER: return readDbPointer(byteBuf);
            case DEPRECTED: return readDeprecated(byteBuf);
            case DOCUMENT: return readDocument(byteBuf);
            case DOUBLE: return readDouble(byteBuf);
            case BOOLEAN: return readBoolean(byteBuf);
            case INT32: return readInt32(byteBuf);
            case INT64: return readInt64(byteBuf);
            case JAVA_SCRIPT: return readJavaScript(byteBuf);
            case JAVA_SCRIPT_WITH_SCOPE: return readJavaScriptWithScope(byteBuf);
            case MAX: return readMax(byteBuf);
            case MIN: return readMin(byteBuf);
            case NULL: return readNull(byteBuf);
            case OBJECT_ID: return readObjectId(byteBuf);
            case REGEX: return readRegex(byteBuf);
            case STRING: return readString(byteBuf);
            case TIMESTAMP: return readTimestamp(byteBuf);
            case UNDEFINED: return readUndefined(byteBuf);
            default: throw new NettyBsonReaderException("Unexpected bson type " + bsonType);
        }
    }

    public Entry<?> readDocumentEntry(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException {
        BsonType bsonType = ParsingTools.getBsonType(byteBuf.readByte());
        String key = stringReader.readCString(byteBuf, true);
        switch (bsonType) {
            case ARRAY: return new SimpleEntry<>(key, readArray(byteBuf));
            case BINARY: return new SimpleEntry<>(key, readBinary(byteBuf));
            case DATETIME: return new SimpleEntry<>(key, readDateTime(byteBuf));
            case DB_POINTER: return new SimpleEntry<>(key, readDbPointer(byteBuf));
            case DEPRECTED: return new SimpleEntry<>(key, readDeprecated(byteBuf));
            case DOCUMENT: return new SimpleEntry<>(key, readDocument(byteBuf));
            case DOUBLE: return new SimpleEntry<>(key, readDouble(byteBuf));
            case BOOLEAN: return new SimpleEntry<>(key, readBoolean(byteBuf));
            case INT32: return new SimpleEntry<>(key, readInt32(byteBuf));
            case INT64: return new SimpleEntry<>(key, readInt64(byteBuf));
            case JAVA_SCRIPT: return new SimpleEntry<>(key, readJavaScript(byteBuf));
            case JAVA_SCRIPT_WITH_SCOPE: return new SimpleEntry<>(key, readJavaScriptWithScope(byteBuf));
            case MAX: return new SimpleEntry<>(key, readMax(byteBuf));
            case MIN: return new SimpleEntry<>(key, readMin(byteBuf));
            case NULL: return new SimpleEntry<>(key, readNull(byteBuf));
            case OBJECT_ID: return new SimpleEntry<>(key, readObjectId(byteBuf));
            case REGEX: return new SimpleEntry<>(key, readRegex(byteBuf));
            case STRING: return new SimpleEntry<>(key, readString(byteBuf));
            case TIMESTAMP: return new SimpleEntry<>(key, readTimestamp(byteBuf));
            case UNDEFINED: return new SimpleEntry<>(key, readUndefined(byteBuf));
            default: throw new NettyBsonReaderException("Unexpected bson type " + bsonType);
        }
    }

    public boolean hasNext(@Tight @ConservesIndexes ByteBuf byteBuf) {
        return byteBuf.getByte(byteBuf.readerIndex()) != 0x00;
    }

    abstract BsonArray readArray(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonBinary readBinary(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonDateTime readDateTime(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonDbPointer readDbPointer(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonValue<?> readDeprecated(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonDocument readDocument(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonDocument readJavaScriptScope(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonDouble readDouble(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonBoolean readBoolean(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonInt32 readInt32(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonInt64 readInt64(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonJavaScript readJavaScript(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonJavaScriptWithScope readJavaScriptWithScope(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonMax readMax(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonMin readMin(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonNull readNull(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonObjectId readObjectId(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonRegex readRegex(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonString readString(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonTimestamp readTimestamp(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

    abstract BsonUndefined readUndefined(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

}
