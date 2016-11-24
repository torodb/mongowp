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

package com.eightkdata.mongowp.bson.utils;

import com.eightkdata.mongowp.bson.BsonDateTime;
import com.eightkdata.mongowp.bson.BsonTimestamp;

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
