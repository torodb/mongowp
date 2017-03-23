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
package com.eightkdata.mongowp.bson;

import javax.annotation.Nonnull;

/**
 *
 */
public enum BsonType {
  DOUBLE(BsonDouble.class, 1),
  STRING(BsonString.class, 2),
  DOCUMENT(BsonDocument.class, 3),
  ARRAY(
      BsonArray.class, 4),
  BINARY(BsonBinary.class, 5),
  UNDEFINED(BsonUndefined.class,
      6),
  OBJECT_ID(BsonObjectId.class, 7),
  BOOLEAN(BsonBoolean.class,
      8),
  DATETIME(BsonDateTime.class, 9),
  NULL(BsonNull.class, 10),
  REGEX(BsonRegex.class,
      11),
  DB_POINTER(BsonDbPointer.class, 12),
  JAVA_SCRIPT(BsonJavaScript.class,
      13),
  DEPRECATED(BsonDeprecated.class, 14),
  JAVA_SCRIPT_WITH_SCOPE(
      BsonJavaScriptWithScope.class, 15),
  INT32(BsonInt32.class,
      16),
  TIMESTAMP(BsonTimestamp.class, 17),
  INT64(BsonInt64.class,
      18),
  DECIMAL128(BsonDecimal128.class, 19),
  MIN(BsonMin.class, 255),
  MAX(BsonMax.class, 127);

  private final Class<? extends BsonValue> javaValueClass;
  private final int intType;

  private <V extends BsonValue> BsonType(Class<V> javaValueClass, int intType) {
    this.javaValueClass = javaValueClass;
    this.intType = intType;
  }

  public Class<? extends BsonValue> getValueClass() {
    return javaValueClass;
  }

  @Nonnull
  public static BsonType fromInt(int type) throws IllegalArgumentException {
    for (BsonType value : values()) {
      if (value.intType == type) {
        return value;
      }
    }
    throw new IllegalArgumentException("There is no type whose integer value is " + type);
  }
}
