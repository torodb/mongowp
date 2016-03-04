/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mongowp-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.utils;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.annotations.Material;
import com.eightkdata.mongowp.bson.*;
import com.eightkdata.mongowp.bson.annotations.NotMutable;
import com.eightkdata.mongowp.bson.impl.InstantBsonDateTime;
import com.eightkdata.mongowp.bson.impl.LongBsonDateTime;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.fields.HostAndPortField;
import com.eightkdata.mongowp.fields.ObjectIdField;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.threeten.bp.Instant;

import static com.eightkdata.mongowp.bson.utils.DefaultBsonValues.*;

/**
 *
 */
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

    public <JV> BsonArrayBuilder add(@Nullable BsonValue<JV> value) {
        Preconditions.checkState(!built);
        if (value == null) {
            list.add(NULL);
        }
        else {
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

    public BsonArrayBuilder addNumber(Number value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return addNull();
        }
        list.add(toBsonNumber(value));
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
        throw new IllegalArgumentException("Numbers of class " + number.getClass() + " are not supported");
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
        }
        else {
            list.add(newLong(value));
        }
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

    public BsonArrayBuilder add(OpTime value) {
        Preconditions.checkState(!built);
        if (value == null) {
            return addNull();
        }
        list.add(value.asBsonTimestamp());
        return this;
    }

    public BsonArrayBuilder addArray(List<BsonValue<?>> list) {
        return add(newArray(list));
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
