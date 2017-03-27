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
package com.torodb.mongowp.utils;

import static com.torodb.mongowp.bson.utils.DefaultBsonValues.NULL;
import static com.torodb.mongowp.bson.utils.DefaultBsonValues.newArray;
import static com.torodb.mongowp.bson.utils.DefaultBsonValues.newBoolean;
import static com.torodb.mongowp.bson.utils.DefaultBsonValues.newDouble;
import static com.torodb.mongowp.bson.utils.DefaultBsonValues.newInt;
import static com.torodb.mongowp.bson.utils.DefaultBsonValues.newLong;
import static com.torodb.mongowp.bson.utils.DefaultBsonValues.newString;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;
import com.torodb.mongowp.OpTime;
import com.torodb.mongowp.annotations.Material;
import com.torodb.mongowp.bson.BsonArray;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.BsonNumber;
import com.torodb.mongowp.bson.BsonObjectId;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.annotations.NotMutable;
import com.torodb.mongowp.bson.impl.InstantBsonDateTime;
import com.torodb.mongowp.bson.impl.LongBsonDateTime;
import com.torodb.mongowp.fields.DocField;
import com.torodb.mongowp.fields.HostAndPortField;
import com.torodb.mongowp.fields.ObjectIdField;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BsonArrayBuilder {

  private final List<BsonValue<?>> list;
  private boolean built;

  public BsonArrayBuilder() {
    this(new ArrayList<BsonValue<?>>());
  }

  public BsonArrayBuilder(int initialSize) {
    this(new ArrayList<BsonValue<?>>(initialSize));
  }

  public BsonArrayBuilder(@NotMutable List<BsonValue<?>> list) {
    this.list = list;
    built = false;
  }

  public BsonArrayBuilder addAll(@Nonnull List<BsonValue<?>> otherList) {
    Preconditions.checkState(!built);
    for (BsonValue<?> val : otherList) {
      list.add(val);
    }
    return this;
  }

  public <JavaValueT> BsonArrayBuilder add(@Nullable BsonValue<JavaValueT> value) {
    Preconditions.checkState(!built);
    if (value == null) {
      list.add(NULL);
    } else {
      list.add(value);
    }
    return this;
  }

  public <T> BsonArrayBuilder add(T value, Function<T, BsonValue<T>> translator) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(translator.apply(value));
    return this;
  }

  public BsonArrayBuilder add(boolean value) {
    Preconditions.checkState(!built);
    list.add(newBoolean(value));
    return this;
  }

  public BsonArrayBuilder add(int value) {
    Preconditions.checkState(!built);
    list.add(newInt(value));
    return this;
  }

  public BsonArrayBuilder add(long value) {
    Preconditions.checkState(!built);
    list.add(newLong(value));
    return this;
  }

  public BsonArrayBuilder add(String value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(newString(value));
    return this;
  }

  public BsonArrayBuilder add(double value) {
    Preconditions.checkState(!built);
    list.add(newDouble(value));
    return this;
  }

  public BsonArrayBuilder add(Instant value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(new InstantBsonDateTime(value));
    return this;
  }

  public BsonArrayBuilder add(OpTime value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(value.getTimestamp());
    return this;
  }

  public BsonArrayBuilder add(BsonArray value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(value);
    return this;
  }

  public BsonArrayBuilder add(DocField field, BsonDocument value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(value);
    return this;
  }

  public BsonArrayBuilder add(DocField field, BsonArrayBuilder value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(value.build());
    return this;
  }

  public BsonArrayBuilder add(HostAndPortField field, HostAndPort value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(newString(value.toString()));
    return this;
  }

  public BsonArrayBuilder add(ObjectIdField field, BsonObjectId value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(value);
    return this;
  }

  public BsonArrayBuilder addNumber(Number value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return addNull();
    }
    list.add(toBsonNumber(value));
    return this;
  }

  public BsonArrayBuilder addNumber(int value) {
    Preconditions.checkState(!built);
    list.add(newInt(value));
    return this;
  }

  public BsonArrayBuilder addNumber(long value) {
    Preconditions.checkState(!built);
    if (value < Integer.MAX_VALUE && value > Integer.MIN_VALUE) {
      list.add(newInt((int) value));
    } else {
      list.add(newLong(value));
    }
    return this;
  }

  private BsonNumber toBsonNumber(Number number) {
    if (number instanceof Double || number instanceof Float) {
      return newDouble(number.doubleValue());
    }
    if (number instanceof Long) {
      long longValue = number.longValue();
      if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MAX_VALUE) {
        return newInt((int) longValue);
      }
      return newLong(longValue);
    }
    if (number instanceof Integer || number instanceof Byte || number instanceof Short) {
      return newInt(number.intValue());
    }
    throw new IllegalArgumentException("Numbers of class " + number.getClass()
        + " are not supported");
  }

  /**
   *
   * @param value millis since Epoch
   * @return
   */
  public BsonArrayBuilder addInstant(long value) {
    Preconditions.checkState(!built);
    list.add(new LongBsonDateTime(value));
    return this;
  }

  public BsonArrayBuilder addArray(List<BsonValue<?>> list) {
    return add(newArray(list));
  }

  public BsonArrayBuilder addNull() {
    Preconditions.checkState(!built);
    list.add(NULL);
    return this;
  }

  @Material
  public BsonArray build() {
    built = true;
    return newArray(list);
  }

}
