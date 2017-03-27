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


import static com.torodb.mongowp.bson.BsonType.ARRAY;
import static com.torodb.mongowp.bson.BsonType.BINARY;
import static com.torodb.mongowp.bson.BsonType.BOOLEAN;
import static com.torodb.mongowp.bson.BsonType.DATETIME;
import static com.torodb.mongowp.bson.BsonType.DB_POINTER;
import static com.torodb.mongowp.bson.BsonType.DECIMAL128;
import static com.torodb.mongowp.bson.BsonType.DEPRECATED;
import static com.torodb.mongowp.bson.BsonType.DOCUMENT;
import static com.torodb.mongowp.bson.BsonType.DOUBLE;
import static com.torodb.mongowp.bson.BsonType.INT32;
import static com.torodb.mongowp.bson.BsonType.INT64;
import static com.torodb.mongowp.bson.BsonType.JAVA_SCRIPT;
import static com.torodb.mongowp.bson.BsonType.JAVA_SCRIPT_WITH_SCOPE;
import static com.torodb.mongowp.bson.BsonType.MAX;
import static com.torodb.mongowp.bson.BsonType.MIN;
import static com.torodb.mongowp.bson.BsonType.NULL;
import static com.torodb.mongowp.bson.BsonType.OBJECT_ID;
import static com.torodb.mongowp.bson.BsonType.REGEX;
import static com.torodb.mongowp.bson.BsonType.STRING;
import static com.torodb.mongowp.bson.BsonType.TIMESTAMP;
import static com.torodb.mongowp.bson.BsonType.UNDEFINED;

import com.torodb.mongowp.bson.BsonArray;
import com.torodb.mongowp.bson.BsonBinary;
import com.torodb.mongowp.bson.BsonBoolean;
import com.torodb.mongowp.bson.BsonDateTime;
import com.torodb.mongowp.bson.BsonDbPointer;
import com.torodb.mongowp.bson.BsonDecimal128;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.BsonDocument.Entry;
import com.torodb.mongowp.bson.BsonDouble;
import com.torodb.mongowp.bson.BsonInt32;
import com.torodb.mongowp.bson.BsonInt64;
import com.torodb.mongowp.bson.BsonJavaScript;
import com.torodb.mongowp.bson.BsonJavaScriptWithScope;
import com.torodb.mongowp.bson.BsonMax;
import com.torodb.mongowp.bson.BsonMin;
import com.torodb.mongowp.bson.BsonNull;
import com.torodb.mongowp.bson.BsonObjectId;
import com.torodb.mongowp.bson.BsonRegex;
import com.torodb.mongowp.bson.BsonString;
import com.torodb.mongowp.bson.BsonTimestamp;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonUndefined;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.abst.AbstractBsonDocument.SimpleEntry;
import com.torodb.mongowp.bson.netty.annotations.ConservesIndexes;
import com.torodb.mongowp.bson.netty.annotations.Loose;
import com.torodb.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.torodb.mongowp.bson.netty.annotations.Tight;
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
