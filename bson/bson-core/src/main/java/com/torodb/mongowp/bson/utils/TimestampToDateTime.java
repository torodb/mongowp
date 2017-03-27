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
package com.torodb.mongowp.bson.utils;

import com.torodb.mongowp.bson.BsonDateTime;
import com.torodb.mongowp.bson.BsonTimestamp;

import java.util.function.BiFunction;
import java.util.function.LongFunction;

/**
 *
 */
public class TimestampToDateTime {

  private TimestampToDateTime() {
  }

  public static BsonDateTime toDateTime(BsonTimestamp timestamp,
      LongFunction<BsonDateTime> dateTimeCreator) {
    long rawData = timestamp.getSecondsSinceEpoch();
    rawData <<= 32;
    rawData |= timestamp.getOrdinal();
    return dateTimeCreator.apply(rawData);
  }

  public static BsonTimestamp toTimestamp(BsonDateTime dateTime,
      BiFunction<Integer, Integer, BsonTimestamp> tsCreator) {
    long millisFromUnix = dateTime.getMillisFromUnix();

    int secs = (int) (millisFromUnix >> 32);
    int ordinal = (int) (millisFromUnix & 0xFFFFFFFF);

    return tsCreator.apply(secs, ordinal);
  }

}
