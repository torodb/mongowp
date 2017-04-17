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

import com.google.common.primitives.UnsignedBytes;
import com.torodb.mongowp.bson.BinarySubtype;
import com.torodb.mongowp.bson.BsonRegex.Options;
import com.torodb.mongowp.bson.BsonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.EnumSet;

import javax.annotation.Nonnull;

/**
 *
 */
class ParsingTools {

  private static final Logger LOGGER = LogManager.getLogger(ParsingTools.class);

  private static final byte FIRST_USER_DEFINED = UnsignedBytes.parseUnsignedByte("80", 16);

  /**
   * Translate a byte to the {@link BsonType} it represents, as specified on the
   * <a href="http://bsonspec.org/spec.html">BSON Spec</a>
   *
   * @param typeByte
   * @return
   * @throws NettyBsonReaderException
   */
  @Nonnull
  protected static BsonType getBsonType(byte typeByte) throws NettyBsonReaderException {
    switch (typeByte) {
      case 0x01:
        return DOUBLE;
      case 0x02:
        return STRING;
      case 0x03:
        return DOCUMENT;
      case 0x04:
        return ARRAY;
      case 0x05:
        return BINARY;
      case 0x06:
        return UNDEFINED;
      case 0x07:
        return OBJECT_ID;
      case 0x08:
        return BOOLEAN;
      case 0x09:
        return DATETIME;
      case 0x0A:
        return NULL;
      case 0x0B:
        return REGEX;
      case 0x0C:
        return DB_POINTER;
      case 0x0D:
        return JAVA_SCRIPT;
      case 0x0E:
        return DEPRECATED;
      case 0x0F:
        return JAVA_SCRIPT_WITH_SCOPE;
      case 0x10:
        return INT32;
      case 0x11:
        return TIMESTAMP;
      case 0x12:
        return INT64;
      case 0x13:
        return DECIMAL128;
      case UnsignedBytes.MAX_VALUE:
        return MIN;
      case 0x7F:
        return MAX;
      default:
        throw new NettyBsonReaderException("It is not defined the type associated with the byte "
            + UnsignedBytes.toString(typeByte, 16));
    }
  }

  protected static byte getByte(BsonType bsonType) throws NettyBsonReaderException {
    switch (bsonType) {
      case DOUBLE:
        return 0x01;
      case STRING:
        return 0x02;
      case DOCUMENT:
        return 0x03;
      case ARRAY:
        return 0x04;
      case BINARY:
        return 0x05;
      case UNDEFINED:
        return 0x06;
      case OBJECT_ID:
        return 0x07;
      case BOOLEAN:
        return 0x08;
      case DATETIME:
        return 0x09;
      case NULL:
        return 0x0A;
      case REGEX:
        return 0x0B;
      case DB_POINTER:
        return 0x0C;
      case JAVA_SCRIPT:
        return 0x0D;
      case DEPRECATED:
        return 0x0E;
      case JAVA_SCRIPT_WITH_SCOPE:
        return 0x0F;
      case INT32:
        return 0x10;
      case TIMESTAMP:
        return 0x11;
      case INT64:
        return 0x12;
      case DECIMAL128:
        return 0x13;
      case MIN:
        return UnsignedBytes.MAX_VALUE;
      case MAX:
        return 0x7F;
      default:
        throw new NettyBsonReaderException("It is not defined the byte associated with the type "
            + bsonType);
    }
  }

  protected static BinarySubtype getBinarySubtype(byte readByte) {
    switch (readByte) {
      case 0x00:
        return BinarySubtype.GENERIC;
      case 0x01:
        return BinarySubtype.FUNCTION;
      case 0x02:
        return BinarySubtype.OLD_BINARY;
      case 0x03:
        return BinarySubtype.OLD_UUID;
      case 0x04:
        return BinarySubtype.UUID;
      case 0x05:
        return BinarySubtype.MD5;
      default: {
        if (UnsignedBytes.compare(readByte, FIRST_USER_DEFINED) >= 0) {
          return BinarySubtype.USER_DEFINED;
        } else {
          throw new AssertionError(
              "Unrecognized binary type 0x" + UnsignedBytes.toString(readByte, 16));
        }
      }
    }
  }

  protected static EnumSet<Options> parseRegexOptions(String optionsStr) {
    EnumSet<Options> result = EnumSet.noneOf(Options.class);

    if (optionsStr.isEmpty()) {
      return result;
    }

    int i = 0;
    if (optionsStr.charAt(i) == 'i') {
      result.add(Options.CASE_INSENSITIVE);

      i++;
      if (i >= optionsStr.length()) {
        return result;
      }
    }
    if (optionsStr.charAt(i) == 'l') {
      result.add(Options.LOCALE_DEPENDENT);

      i++;
      if (i >= optionsStr.length()) {
        return result;
      }
    }
    if (optionsStr.charAt(i) == 'm') {
      result.add(Options.MULTILINE_MATCHING);

      i++;
      if (i >= optionsStr.length()) {
        return result;
      }
    }
    if (optionsStr.charAt(i) == 's') {
      result.add(Options.DOTALL_MODE);

      i++;
      if (i >= optionsStr.length()) {
        return result;
      }
    }
    if (optionsStr.charAt(i) == 'u') {
      result.add(Options.UNICODE);

      i++;
      if (i >= optionsStr.length()) {
        return result;
      }
    }
    if (optionsStr.charAt(i) == 'x') {
      result.add(Options.VERBOSE_MODE);

      i++;
      if (i >= optionsStr.length()) {
        return result;
      }
    }

    assert i < optionsStr.length();
    LOGGER.warn("Unexpected regex options '{}'", optionsStr.substring(i));

    return result;
  }

}
