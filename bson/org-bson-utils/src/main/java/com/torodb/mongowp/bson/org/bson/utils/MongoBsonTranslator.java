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
package com.torodb.mongowp.bson.org.bson.utils;

import com.google.common.primitives.UnsignedBytes;
import com.torodb.mongowp.bson.BinarySubtype;
import com.torodb.mongowp.bson.BsonDocument.Entry;
import com.torodb.mongowp.bson.impl.ByteArrayBsonBinary;
import com.torodb.mongowp.bson.impl.ByteArrayBsonObjectId;
import com.torodb.mongowp.bson.impl.DefaultBsonDbPointer;
import com.torodb.mongowp.bson.impl.DefaultBsonJavaScript;
import com.torodb.mongowp.bson.impl.DefaultBsonJavaScriptWithCode;
import com.torodb.mongowp.bson.impl.DefaultBsonRegex;
import com.torodb.mongowp.bson.impl.DefaultBsonTimestamp;
import com.torodb.mongowp.bson.impl.FalseBsonBoolean;
import com.torodb.mongowp.bson.impl.ListBsonArray;
import com.torodb.mongowp.bson.impl.LongBsonDateTime;
import com.torodb.mongowp.bson.impl.LongsBsonDecimal128;
import com.torodb.mongowp.bson.impl.MapBasedBsonDocument;
import com.torodb.mongowp.bson.impl.PrimitiveBsonDouble;
import com.torodb.mongowp.bson.impl.PrimitiveBsonInt32;
import com.torodb.mongowp.bson.impl.PrimitiveBsonInt64;
import com.torodb.mongowp.bson.impl.SimpleBsonMax;
import com.torodb.mongowp.bson.impl.SimpleBsonMin;
import com.torodb.mongowp.bson.impl.SimpleBsonNull;
import com.torodb.mongowp.bson.impl.SimpleBsonUndefined;
import com.torodb.mongowp.bson.impl.StringBsonDeprecated;
import com.torodb.mongowp.bson.impl.StringBsonString;
import com.torodb.mongowp.bson.impl.TrueBsonBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonArray;
import org.bson.BsonBinary;
import org.bson.BsonBoolean;
import org.bson.BsonDateTime;
import org.bson.BsonDbPointer;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonJavaScript;
import org.bson.BsonJavaScriptWithScope;
import org.bson.BsonMaxKey;
import org.bson.BsonMinKey;
import org.bson.BsonNull;
import org.bson.BsonObjectId;
import org.bson.BsonRegularExpression;
import org.bson.BsonString;
import org.bson.BsonSymbol;
import org.bson.BsonTimestamp;
import org.bson.BsonType;
import org.bson.BsonUndefined;
import org.bson.BsonValue;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MongoBsonTranslator {

  private static final Logger LOGGER = LogManager.getLogger(MongoBsonTranslator.class);

  static final List<org.bson.BsonType> deprecatedTypes =
      Arrays.asList(new BsonType[] {org.bson.BsonType.END_OF_DOCUMENT, org.bson.BsonType.SYMBOL});

  public static final Function<BsonDocument, com.torodb.mongowp.bson.BsonDocument>
      FROM_MONGO_FUNCTION = new FromMongoFunction();
  public static final Function<com.torodb.mongowp.bson.BsonDocument, BsonDocument>
      TO_MONGO_FUNCTION = new ToMongoFunction();
  private static final byte FIRST_USER_DEFINED = UnsignedBytes.parseUnsignedByte("80", 16);

  @Nullable
  public static BsonDocument translate(
      @Nullable com.torodb.mongowp.bson.BsonDocument wpDocument) {
    return (BsonDocument) translatePrivate(wpDocument);
  }

  @Nullable
  public static com.torodb.mongowp.bson.BsonDocument translate(
      @Nullable org.bson.BsonDocument mongoDocument) {
    return (com.torodb.mongowp.bson.BsonDocument) translatePrivate(mongoDocument);
  }

  @Nullable
  private static BsonValue translatePrivate(
      @Nullable com.torodb.mongowp.bson.BsonValue<?> value) {
    if (value == null) {
      return null;
    }

    switch (value.getType()) {
      case ARRAY: {
        com.torodb.mongowp.bson.BsonArray casted =
                (com.torodb.mongowp.bson.BsonArray) value;
        List<BsonValue> list = new ArrayList<>();
        for (com.torodb.mongowp.bson.BsonValue<?> wpValue : casted) {
          list.add(translatePrivate(wpValue));
        }
        return new BsonArray(list);
      }
      case BINARY: {
        com.torodb.mongowp.bson.BsonBinary casted =
                (com.torodb.mongowp.bson.BsonBinary) value;
        return new BsonBinary(casted.getNumericSubType(), casted.getByteSource().read());
      }
      case DATETIME:
        return new BsonDateTime(
                ((com.torodb.mongowp.bson.BsonDateTime) value).getMillisFromUnix());
      case DB_POINTER: {
        com.torodb.mongowp.bson.BsonDbPointer casted =
                (com.torodb.mongowp.bson.BsonDbPointer) value;
        return new BsonDbPointer(casted.getNamespace(), translateObjectId(casted.getId()));
      }
      case DOCUMENT: {
        com.torodb.mongowp.bson.BsonDocument casted =
                (com.torodb.mongowp.bson.BsonDocument) value;
        BsonDocument result = new org.bson.BsonDocument();
        for (Entry<?> entry : casted) {
          result.append(entry.getKey(), translatePrivate(entry.getValue()));
        }
        return result;
      }
      case DOUBLE:
        return new BsonDouble(((com.torodb.mongowp.bson.BsonDouble) value).doubleValue());
      case BOOLEAN: {
        com.torodb.mongowp.bson.BsonBoolean casted =
                (com.torodb.mongowp.bson.BsonBoolean) value;
        if (casted.getPrimitiveValue()) {
          return BsonBoolean.TRUE;
        } else {
          return BsonBoolean.FALSE;
        }
      }
      case INT32:
        return new BsonInt32(((com.torodb.mongowp.bson.BsonInt32) value).intValue());
      case INT64:
        return new BsonInt64(((com.torodb.mongowp.bson.BsonInt64) value).longValue());
      case DECIMAL128: {
        com.torodb.mongowp.bson.BsonDecimal128 casted =
                (com.torodb.mongowp.bson.BsonDecimal128) value;
        return new org.bson.BsonDecimal128(
                Decimal128.fromIEEE754BIDEncoding(casted.getHigh(), casted.getLow()));
      }
      case JAVA_SCRIPT: {
        return new BsonJavaScript(
                ((com.torodb.mongowp.bson.BsonJavaScript) value).getValue());
      }
      case JAVA_SCRIPT_WITH_SCOPE: {
        com.torodb.mongowp.bson.BsonJavaScriptWithScope casted =
                (com.torodb.mongowp.bson.BsonJavaScriptWithScope) value;
        return new BsonJavaScriptWithScope(casted.getJavaScript(), translate(casted.getScope()));
      }
      case MAX:
        return new BsonMaxKey();
      case MIN:
        return new BsonMinKey();
      case NULL:
        return new BsonNull();
      case OBJECT_ID:
        return new BsonObjectId(
                translateObjectId((com.torodb.mongowp.bson.BsonObjectId) value));
      case REGEX: {
        com.torodb.mongowp.bson.BsonRegex casted =
                (com.torodb.mongowp.bson.BsonRegex) value;
        return new BsonRegularExpression(casted.getPattern(), casted.getOptionsAsText());
      }
      case STRING:
        return new BsonString(((com.torodb.mongowp.bson.BsonString) value).getValue());
      case TIMESTAMP: {
        com.torodb.mongowp.bson.BsonTimestamp casted =
                (com.torodb.mongowp.bson.BsonTimestamp) value;
        return new BsonTimestamp(casted.getSecondsSinceEpoch(), casted.getOrdinal());
      }
      case UNDEFINED:
        return new BsonUndefined();
      case DEPRECATED:
        return new BsonSymbol(value.asDeprecated().getValue());
      default:
        throw new AssertionError("It is not defined how to translate the type " + value.getType());
    }
  }

  @Nullable
  private static com.torodb.mongowp.bson.BsonValue<?> translatePrivate(
          @Nullable BsonValue value) {
    if (value == null) {
      return null;
    }
    switch (value.getBsonType()) {
      case ARRAY: {
        List<com.torodb.mongowp.bson.BsonValue<?>> list = new ArrayList<>();
        for (BsonValue mongoValue : value.asArray()) {
          list.add(translatePrivate(mongoValue));
        }
        return new ListBsonArray(list);
      }
      case BINARY: {
        BsonBinary casted = value.asBinary();
        return new ByteArrayBsonBinary(
                getBinarySubtype(casted.getType()), casted.getType(), casted.getData());
      }
      case DATE_TIME:
        return new LongBsonDateTime(value.asDateTime().getValue());
      case DB_POINTER: {
        BsonDbPointer casted = value.asDBPointer();
        return new DefaultBsonDbPointer(
                casted.getNamespace(), new ByteArrayBsonObjectId(casted.getId().toByteArray()));
      }
      case DOCUMENT: {
        LinkedHashMap<String, com.torodb.mongowp.bson.BsonValue<?>> map =
                new LinkedHashMap<>(value.asDocument().size());
        for (java.util.Map.Entry<String, BsonValue> entry : value.asDocument().entrySet()) {
          map.put(entry.getKey(), translatePrivate(entry.getValue()));
        }
        return new MapBasedBsonDocument(map);
      }
      case DOUBLE:
        return PrimitiveBsonDouble.newInstance(value.asDouble().getValue());
      case BOOLEAN:
        if (value.asBoolean().getValue()) {
          return TrueBsonBoolean.getInstance();
        } else {
          return FalseBsonBoolean.getInstance();
        }
      case INT32:
        return PrimitiveBsonInt32.newInstance(value.asInt32().getValue());
      case INT64:
        return PrimitiveBsonInt64.newInstance(value.asInt64().getValue());
      case JAVASCRIPT: {
        return new DefaultBsonJavaScript(value.asJavaScript().getCode());
      }
      case DECIMAL128:
        return new LongsBsonDecimal128(
                value.asDecimal128().decimal128Value().getHigh(),
                value.asDecimal128().decimal128Value().getLow());
      case JAVASCRIPT_WITH_SCOPE: {
        BsonJavaScriptWithScope casted = value.asJavaScriptWithScope();
        return new DefaultBsonJavaScriptWithCode(casted.getCode(), translate(casted.getScope()));
      }
      case MAX_KEY:
        return SimpleBsonMax.getInstance();
      case MIN_KEY:
        return SimpleBsonMin.getInstance();
      case NULL:
        return SimpleBsonNull.getInstance();
      case OBJECT_ID:
        return new ByteArrayBsonObjectId(value.asObjectId().getValue().toByteArray());
      case REGULAR_EXPRESSION: {
        BsonRegularExpression casted = value.asRegularExpression();
        return new DefaultBsonRegex(casted.getOptions(), casted.getPattern());
      }
      case STRING:
        return new StringBsonString(value.asString().getValue());
      case TIMESTAMP: {
        BsonTimestamp casted = value.asTimestamp();
        return new DefaultBsonTimestamp(casted.getTime(), casted.getInc());
      }
      case UNDEFINED:
        return SimpleBsonUndefined.getInstance();
      case END_OF_DOCUMENT:
      case SYMBOL:
        return new StringBsonDeprecated(value.asSymbol().getSymbol());
      default:
        throw new AssertionError(
                "It is not defined how to translate the type " + value.getBsonType());
    }
  }

  private static ObjectId translateObjectId(com.torodb.mongowp.bson.BsonObjectId id) {
    return new ObjectId(id.toHexString());
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
      default:
      {
        if (UnsignedBytes.compare(readByte, FIRST_USER_DEFINED) >= 0) {
          return BinarySubtype.USER_DEFINED;
        } else {
          throw new AssertionError(
                  "Unrecognized binary type 0x" + UnsignedBytes.toString(readByte, 16));
        }
      }
    }
  }

  private static class FromMongoFunction
      implements Function<BsonDocument, com.torodb.mongowp.bson.BsonDocument> {

    @Override
    public com.torodb.mongowp.bson.BsonDocument apply(@Nonnull BsonDocument t) {
      return translate(t);
    }
  }

  private static class ToMongoFunction
      implements Function<com.torodb.mongowp.bson.BsonDocument, BsonDocument> {

    @Override
    public BsonDocument apply(@Nonnull com.torodb.mongowp.bson.BsonDocument t) {
      return translate(t);
    }
  }
}
