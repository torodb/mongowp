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

package com.eightkdata.mongowp.bson.netty;


import static com.eightkdata.mongowp.bson.BsonType.ARRAY;
import static com.eightkdata.mongowp.bson.BsonType.BINARY;
import static com.eightkdata.mongowp.bson.BsonType.BOOLEAN;
import static com.eightkdata.mongowp.bson.BsonType.DATETIME;
import static com.eightkdata.mongowp.bson.BsonType.DB_POINTER;
import static com.eightkdata.mongowp.bson.BsonType.DECIMAL128;
import static com.eightkdata.mongowp.bson.BsonType.DEPRECATED;
import static com.eightkdata.mongowp.bson.BsonType.DOCUMENT;
import static com.eightkdata.mongowp.bson.BsonType.DOUBLE;
import static com.eightkdata.mongowp.bson.BsonType.INT32;
import static com.eightkdata.mongowp.bson.BsonType.INT64;
import static com.eightkdata.mongowp.bson.BsonType.JAVA_SCRIPT;
import static com.eightkdata.mongowp.bson.BsonType.JAVA_SCRIPT_WITH_SCOPE;
import static com.eightkdata.mongowp.bson.BsonType.MAX;
import static com.eightkdata.mongowp.bson.BsonType.MIN;
import static com.eightkdata.mongowp.bson.BsonType.NULL;
import static com.eightkdata.mongowp.bson.BsonType.OBJECT_ID;
import static com.eightkdata.mongowp.bson.BsonType.REGEX;
import static com.eightkdata.mongowp.bson.BsonType.STRING;
import static com.eightkdata.mongowp.bson.BsonType.TIMESTAMP;
import static com.eightkdata.mongowp.bson.BsonType.UNDEFINED;

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonBinary;
import com.eightkdata.mongowp.bson.BsonBoolean;
import com.eightkdata.mongowp.bson.BsonDateTime;
import com.eightkdata.mongowp.bson.BsonDbPointer;
import com.eightkdata.mongowp.bson.BsonDecimal128;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonDouble;
import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.BsonInt64;
import com.eightkdata.mongowp.bson.BsonJavaScript;
import com.eightkdata.mongowp.bson.BsonJavaScriptWithScope;
import com.eightkdata.mongowp.bson.BsonMax;
import com.eightkdata.mongowp.bson.BsonMin;
import com.eightkdata.mongowp.bson.BsonNull;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.bson.BsonRegex;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.BsonTimestamp;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonUndefined;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.abst.AbstractBsonDocument.SimpleEntry;
import com.eightkdata.mongowp.bson.netty.annotations.ConservesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 *
 */
public abstract class NettyBsonLowLevelReader {

  private static final Logger LOGGER = LogManager.getLogger(NettyBsonLowLevelReader.class);

  private final NettyStringReader stringReader;

  @Inject
  public NettyBsonLowLevelReader(NettyStringReader stringReader) {
    this.stringReader = stringReader;
  }

  protected NettyStringReader getStringReader() {
    return stringReader;
  }

  public BsonValue<?> readArrayEntry(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    BsonType bsonType = ParsingTools.getBsonType(byteBuf.readByte());
    stringReader.skipCString(byteBuf);
    switch (bsonType) { 
      case ARRAY:
        return readArray(byteBuf);
      case BINARY:
        return readBinary(byteBuf);
      case DATETIME:
        return readDateTime(byteBuf);
      case DB_POINTER:
        return readDbPointer(byteBuf);
      case DECIMAL128:
        return readDecimal128(byteBuf);
      case DEPRECATED:
        return readDeprecated(byteBuf);
      case DOCUMENT:
        return readDocument(byteBuf);
      case DOUBLE:
        return readDouble(byteBuf);
      case BOOLEAN:
        return readBoolean(byteBuf);
      case INT32:
        return readInt32(byteBuf);
      case INT64:
        return readInt64(byteBuf);
      case JAVA_SCRIPT:
        return readJavaScript(byteBuf);
      case JAVA_SCRIPT_WITH_SCOPE:
        return readJavaScriptWithScope(byteBuf);
      case MAX:
        return readMax(byteBuf);
      case MIN:
        return readMin(byteBuf);
      case NULL:
        return readNull(byteBuf);
      case OBJECT_ID:
        return readObjectId(byteBuf);
      case REGEX:
        return readRegex(byteBuf);
      case STRING:
        return readString(byteBuf);
      case TIMESTAMP:
        return readTimestamp(byteBuf);
      case UNDEFINED:
        return readUndefined(byteBuf);
      default:
        throw new NettyBsonReaderException("Unexpected bson type " + bsonType);
    }
  }

  public Entry<?> readDocumentEntry(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    BsonType bsonType = ParsingTools.getBsonType(byteBuf.readByte());
    String key = stringReader.readCString(byteBuf, true);
    switch (bsonType) {
      case ARRAY:
        return new SimpleEntry<>(key, readArray(byteBuf));
      case BINARY:
        return new SimpleEntry<>(key, readBinary(byteBuf));
      case DATETIME:
        return new SimpleEntry<>(key, readDateTime(byteBuf));
      case DB_POINTER:
        return new SimpleEntry<>(key, readDbPointer(byteBuf));
      case DECIMAL128:
        return new SimpleEntry<>(key, readDecimal128(byteBuf));
      case DEPRECATED:
        return new SimpleEntry<>(key, readDeprecated(byteBuf));
      case DOCUMENT:
        return new SimpleEntry<>(key, readDocument(byteBuf));
      case DOUBLE:
        return new SimpleEntry<>(key, readDouble(byteBuf));
      case BOOLEAN:
        return new SimpleEntry<>(key, readBoolean(byteBuf));
      case INT32:
        return new SimpleEntry<>(key, readInt32(byteBuf));
      case INT64:
        return new SimpleEntry<>(key, readInt64(byteBuf));
      case JAVA_SCRIPT:
        return new SimpleEntry<>(key, readJavaScript(byteBuf));
      case JAVA_SCRIPT_WITH_SCOPE:
        return new SimpleEntry<>(key, readJavaScriptWithScope(byteBuf));
      case MAX:
        return new SimpleEntry<>(key, readMax(byteBuf));
      case MIN:
        return new SimpleEntry<>(key, readMin(byteBuf));
      case NULL:
        return new SimpleEntry<>(key, readNull(byteBuf));
      case OBJECT_ID:
        return new SimpleEntry<>(key, readObjectId(byteBuf));
      case REGEX:
        return new SimpleEntry<>(key, readRegex(byteBuf));
      case STRING:
        return new SimpleEntry<>(key, readString(byteBuf));
      case TIMESTAMP:
        return new SimpleEntry<>(key, readTimestamp(byteBuf));
      case UNDEFINED:
        return new SimpleEntry<>(key, readUndefined(byteBuf));
      default:
        throw new NettyBsonReaderException("Unexpected bson type " + bsonType);
    }
  }

  public boolean hasNext(@Tight @ConservesIndexes ByteBuf byteBuf) {
    return byteBuf.getByte(byteBuf.readerIndex()) != 0x00;
  }

  abstract BsonArray readArray(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonBinary readBinary(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonDateTime readDateTime(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonDbPointer readDbPointer(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonDecimal128 readDecimal128(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;
  
  abstract BsonValue<?> readDeprecated(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonDocument readDocument(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonDocument readJavaScriptScope(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonDouble readDouble(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonBoolean readBoolean(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonInt32 readInt32(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonInt64 readInt64(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonJavaScript readJavaScript(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonJavaScriptWithScope readJavaScriptWithScope(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonMax readMax(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

  abstract BsonMin readMin(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException;

  abstract BsonNull readNull(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonObjectId readObjectId(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonRegex readRegex(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonString readString(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonTimestamp readTimestamp(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

  abstract BsonUndefined readUndefined(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException;

}
