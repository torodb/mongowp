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
import static com.torodb.mongowp.bson.utils.DefaultBsonValues.newDocument;
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
import com.torodb.mongowp.bson.BsonDocument.Entry;
import com.torodb.mongowp.bson.BsonNumber;
import com.torodb.mongowp.bson.BsonObjectId;
import com.torodb.mongowp.bson.BsonTimestamp;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.impl.InstantBsonDateTime;
import com.torodb.mongowp.bson.impl.LongBsonDateTime;
import com.torodb.mongowp.fields.ArrayField;
import com.torodb.mongowp.fields.BooleanField;
import com.torodb.mongowp.fields.BsonField;
import com.torodb.mongowp.fields.DateTimeField;
import com.torodb.mongowp.fields.DocField;
import com.torodb.mongowp.fields.DoubleField;
import com.torodb.mongowp.fields.HostAndPortField;
import com.torodb.mongowp.fields.IntField;
import com.torodb.mongowp.fields.LongField;
import com.torodb.mongowp.fields.NumberField;
import com.torodb.mongowp.fields.ObjectIdField;
import com.torodb.mongowp.fields.StringField;
import com.torodb.mongowp.fields.TimestampField;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BsonDocumentBuilder {

  private final LinkedHashMap<String, BsonValue<?>> map;
  private boolean built;

  public BsonDocumentBuilder() {
    this.map = new LinkedHashMap<>();
    built = false;
  }

  public BsonDocumentBuilder(int initialCapacity) {
    this.map = new LinkedHashMap<>(initialCapacity);
    built = false;
  }

  public BsonDocumentBuilder(BsonDocument doc) {
    this.map = new LinkedHashMap<>(doc.size());
    for (Entry<?> entry : doc) {
      map.put(entry.getKey(), entry.getValue());
    }
    built = false;
  }

  public boolean containsField(@Nonnull BsonField<?, ?> field) {
    Preconditions.checkState(!built);
    return map.containsKey(field.getFieldName());
  }

  public BsonDocumentBuilder copy(@Nonnull BsonDocument otherDoc) {
    Preconditions.checkState(!built);
    for (Entry<?> entrySet : otherDoc) {
      map.put(entrySet.getKey(), entrySet.getValue());
    }
    return this;
  }

  public BsonDocumentBuilder appendUnsafe(String fieldName, @Nullable BsonValue value) {
    Preconditions.checkState(!built);
    if (value == null) {
      map.put(fieldName, NULL);
      return this;
    }
    map.put(fieldName, value);
    return this;
  }

  public <JavaTypeT> BsonDocumentBuilder append(BsonField<JavaTypeT, BsonValue<JavaTypeT>> field,
      @Nullable BsonValue<JavaTypeT> value) {
    Preconditions.checkState(!built);
    if (value == null) {
      map.put(field.getFieldName(), NULL);
      return this;
    }
    map.put(field.getFieldName(), value);
    return this;
  }

  public <T> BsonDocumentBuilder append(BsonField<T, BsonValue<T>> field, T value,
      Function<T, BsonValue<T>> translator) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), translator.apply(value));
    return this;
  }

  public BsonDocumentBuilder append(BooleanField field, boolean value) {
    Preconditions.checkState(!built);
    map.put(field.getFieldName(), newBoolean(value));
    return this;
  }

  public BsonDocumentBuilder append(IntField field, int value) {
    Preconditions.checkState(!built);
    map.put(field.getFieldName(), newInt(value));
    return this;
  }

  public BsonDocumentBuilder append(LongField field, long value) {
    Preconditions.checkState(!built);
    map.put(field.getFieldName(), newLong(value));
    return this;
  }

  public BsonDocumentBuilder append(StringField field, String value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), newString(value));
    return this;
  }

  public BsonDocumentBuilder append(DoubleField field, double value) {
    Preconditions.checkState(!built);
    map.put(field.getFieldName(), newDouble(value));
    return this;
  }

  public BsonDocumentBuilder append(DateTimeField field, Instant value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), new InstantBsonDateTime(value));
    return this;
  }

  public BsonDocumentBuilder append(TimestampField field, OpTime value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(
        field.getFieldName(),
        value.getTimestamp()
    );
    return this;
  }

  public BsonDocumentBuilder append(TimestampField field, BsonTimestamp value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(
        field.getFieldName(),
        value
    );
    return this;
  }

  public BsonDocumentBuilder append(ArrayField field, List<BsonValue<?>> list) {
    return append(field, newArray(list));
  }

  public BsonDocumentBuilder append(ArrayField field, BsonArray value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), value);
    return this;
  }

  public BsonDocumentBuilder append(DocField field, BsonDocument value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), value);
    return this;
  }

  public BsonDocumentBuilder append(DocField field, BsonDocumentBuilder value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), value.build());
    return this;
  }

  public BsonDocumentBuilder append(HostAndPortField field, HostAndPort value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), newString(value.toString()));
    return this;
  }

  public BsonDocumentBuilder append(HostAndPortField field, String value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), newString(value));
    return this;
  }

  public BsonDocumentBuilder append(ObjectIdField field, BsonObjectId value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), value);
    return this;
  }

  public BsonDocumentBuilder appendNumber(NumberField<?> field, int value) {
    Preconditions.checkState(!built);
    map.put(field.getFieldName(), newInt(value));
    return this;
  }

  public BsonDocumentBuilder appendNumber(NumberField<?> field, long value) {
    Preconditions.checkState(!built);
    if (value < Integer.MAX_VALUE && value > Integer.MIN_VALUE) {
      map.put(field.getFieldName(), newInt((int) value));
    } else {
      map.put(field.getFieldName(), newLong(value));
    }
    return this;
  }

  public BsonDocumentBuilder appendNumber(NumberField<?> field, Number value) {
    Preconditions.checkState(!built);
    if (value == null) {
      return appendNull(field);
    }
    map.put(field.getFieldName(), toBsonNumber(value));
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
   * @param field
   * @param value millis since Epoch
   * @return
   */
  public BsonDocumentBuilder appendInstant(DateTimeField field, long value) {
    Preconditions.checkState(!built);
    map.put(field.getFieldName(), new LongBsonDateTime(value));
    return this;
  }

  public BsonDocumentBuilder appendNull(BsonField<?, ?> field) {
    Preconditions.checkState(!built);
    map.put(field.getFieldName(), NULL);
    return this;
  }

  @Material
  public BsonDocument build() {
    built = true;
    return newDocument(map);
  }

}
