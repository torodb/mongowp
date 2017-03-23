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
package com.eightkdata.mongowp.bson.utils;

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonBoolean;
import com.eightkdata.mongowp.bson.BsonDateTime;
import com.eightkdata.mongowp.bson.BsonDecimal128;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonDouble;
import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.BsonInt64;
import com.eightkdata.mongowp.bson.BsonMax;
import com.eightkdata.mongowp.bson.BsonMin;
import com.eightkdata.mongowp.bson.BsonNull;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.BsonTimestamp;
import com.eightkdata.mongowp.bson.BsonUndefined;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.annotations.NotMutable;
import com.eightkdata.mongowp.bson.impl.DefaultBsonTimestamp;
import com.eightkdata.mongowp.bson.impl.EmptyBsonArray;
import com.eightkdata.mongowp.bson.impl.EmptyBsonDocument;
import com.eightkdata.mongowp.bson.impl.FalseBsonBoolean;
import com.eightkdata.mongowp.bson.impl.InstantBsonDateTime;
import com.eightkdata.mongowp.bson.impl.ListBasedBsonDocument;
import com.eightkdata.mongowp.bson.impl.ListBsonArray;
import com.eightkdata.mongowp.bson.impl.LongBsonDateTime;
import com.eightkdata.mongowp.bson.impl.LongsBsonDecimal128;
import com.eightkdata.mongowp.bson.impl.MapBasedBsonDocument;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonDouble;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonInt32;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonInt64;
import com.eightkdata.mongowp.bson.impl.SimpleBsonMax;
import com.eightkdata.mongowp.bson.impl.SimpleBsonMin;
import com.eightkdata.mongowp.bson.impl.SimpleBsonNull;
import com.eightkdata.mongowp.bson.impl.SimpleBsonUndefined;
import com.eightkdata.mongowp.bson.impl.SingleEntryBsonDocument;
import com.eightkdata.mongowp.bson.impl.SingleValueBsonArray;
import com.eightkdata.mongowp.bson.impl.StringBsonString;
import com.eightkdata.mongowp.bson.impl.TrueBsonBoolean;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;

public class DefaultBsonValues {

  public static final BsonNull NULL = SimpleBsonNull.getInstance();
  public static final BsonUndefined UNDEFINED = SimpleBsonUndefined.getInstance();
  public static final BsonBoolean TRUE = TrueBsonBoolean.getInstance();
  public static final BsonBoolean FALSE = FalseBsonBoolean.getInstance();
  public static final BsonMax MAX = SimpleBsonMax.getInstance();
  public static final BsonMin MIN = SimpleBsonMin.getInstance();

  public static final PrimitiveBsonDouble DOUBLE_ZERO = PrimitiveBsonDouble.newInstance(0);
  public static final PrimitiveBsonDouble DOUBLE_ONE = PrimitiveBsonDouble.newInstance(1);

  public static final PrimitiveBsonInt32 INT32_ZERO = PrimitiveBsonInt32.newInstance(0);
  public static final PrimitiveBsonInt32 INT32_ONE = PrimitiveBsonInt32.newInstance(1);

  public static final PrimitiveBsonInt64 INT64_ZERO = PrimitiveBsonInt64.newInstance(0);
  public static final PrimitiveBsonInt64 INT64_ONE = PrimitiveBsonInt64.newInstance(1);

  public static final BsonString EMPTY_STRING = new StringBsonString("");

  public static final BsonDocument EMPTY_DOC = EmptyBsonDocument.getInstance();

  public static final BsonArray EMPTY_ARRAY = EmptyBsonArray.getInstance();

  private DefaultBsonValues() {
  }

  public static BsonInt32 newInt(int value) {
    return PrimitiveBsonInt32.newInstance(value);
  }

  public static BsonInt64 newLong(long value) {
    return PrimitiveBsonInt64.newInstance(value);
  }

  public static BsonDouble newDouble(double value) {
    return PrimitiveBsonDouble.newInstance(value);
  }

  public static BsonDecimal128 newDecimal128(long high, long low) {
    return LongsBsonDecimal128.newInstance(high, low);
  }
  
  public static BsonString newString(String value) {
    if (value.isEmpty()) {
      return EMPTY_STRING;
    }
    return new StringBsonString(value);
  }

  public static BsonBoolean newBoolean(boolean bool) {
    if (bool) {
      return TRUE;
    }
    return FALSE;
  }

  public static final BsonArray newArrayFromSingleValue(BsonValue<?> val) {
    return new SingleValueBsonArray(val);
  }

  public static final BsonArray newArray(List<BsonValue<?>> list) {
    switch (list.size()) {
      case 0:
        return EMPTY_ARRAY;
      case 1:
        return new SingleValueBsonArray(list.get(0));
      default:
        return new ListBsonArray(list);
    }
  }

  public static final BsonDocument newDocument(String key, BsonValue<?> value) {
    return new SingleEntryBsonDocument(key, value);
  }

  public static final BsonDocument newDocument(@NotMutable List<Entry<?>> list) {
    switch (list.size()) {
      case 0:
        return EMPTY_DOC;
      case 1:
        Entry<?> entry = list.get(0);
        return new SingleEntryBsonDocument(entry.getKey(), entry.getValue());
      default:
        return new ListBasedBsonDocument(list);
    }
  }

  public static final BsonDocument newDocument(
      @NotMutable LinkedHashMap<String, BsonValue<?>> map) {
    switch (map.size()) {
      case 0:
        return EMPTY_DOC;
      case 1: {
        java.util.Map.Entry<String, BsonValue<?>> entry = map.entrySet().iterator().next();
        return new SingleEntryBsonDocument(entry.getKey(), entry.getValue());
      }
      default:
        return new MapBasedBsonDocument(map);
    }
  }

  public static final BsonDateTime newDateTime(long millis) {
    return new LongBsonDateTime(millis);
  }

  public static final BsonDateTime newDateTime(Instant instant) {
    return new InstantBsonDateTime(instant);
  }

  public static final BsonDateTime newDateTime(BsonTimestamp timestamp) {
    return TimestampToDateTime.toDateTime(timestamp, LongBsonDateTime::new);
  }

  public static BsonTimestamp newTimestamp(int secs, int ordinal) {
    return new DefaultBsonTimestamp(secs, ordinal);
  }
}
