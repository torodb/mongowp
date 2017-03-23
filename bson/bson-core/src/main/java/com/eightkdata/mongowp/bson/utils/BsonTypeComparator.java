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

import com.eightkdata.mongowp.bson.BsonType;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;

/**
 * An object that comparares {@link BsonType bson types} as specified by
 * <a href="https://docs.mongodb.org/manual/reference/bson-types/#comparison-sort-order">MongoDB
 * documentation</a>
 */
public final class BsonTypeComparator implements Comparator<BsonType>, Serializable {

  private static final long serialVersionUID = -2424698084404486227L;

  private static final List<BsonType> ORDERED_TYPES = Lists.newArrayList(
      MIN,
      NULL,
      DOUBLE, //also int32 and int64
      STRING, //also symbol
      DOCUMENT,
      ARRAY,
      BINARY,
      OBJECT_ID,
      BOOLEAN,
      DATETIME,
      TIMESTAMP,
      REGEX,
      /*
       * Documentation does not indicate JS and JS_WITH_SCOPE order. We choose to place them before
       * MAX
       */
      JAVA_SCRIPT,
      JAVA_SCRIPT_WITH_SCOPE,
      MAX
  );

  private static final EnumMap<BsonType, Integer> TO_POS_MAP;

  public static final BsonTypeComparator INSTANCE = new BsonTypeComparator();

  static {
    EnumMap<BsonType, Integer> map = new EnumMap<>(BsonType.class);
    for (BsonType type : BsonType.values()) {
      int pos = ORDERED_TYPES.indexOf(generalize(type));
      if (pos == -1) {
        throw new AssertionError("BsonType " + type + " does not have a defined order");
      }
      map.put(type, pos);
    }
    TO_POS_MAP = map;
  }

  @Override
  public int compare(BsonType t1, BsonType t2) {
    assert TO_POS_MAP.containsKey(t1);
    assert TO_POS_MAP.containsKey(t2);

    if (t1 == t2) {
      return 0;
    }

    return TO_POS_MAP.get(t1) - TO_POS_MAP.get(t2);
  }

  /**
   * Generalize the given type to a type that is present on {@link #ORDERED_TYPES}.
   */
  static BsonType generalize(BsonType type) {
    if (isNumeric(type)) {
      return DOUBLE;
    }
    if (type == UNDEFINED || type == DEPRECATED) {
      return NULL;
    }
    if (type == DB_POINTER) {
      return OBJECT_ID;
    }
    assert ORDERED_TYPES.contains(type);
    return type;
  }

  private static boolean isNumeric(BsonType type) {
    return type == INT32 || type == INT64 || type == DOUBLE || type == DECIMAL128;
  }

}
