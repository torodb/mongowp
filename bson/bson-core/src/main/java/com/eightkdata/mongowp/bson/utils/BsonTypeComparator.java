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
 * along with bson-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.utils;

import com.eightkdata.mongowp.bson.BsonType;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;

import static com.eightkdata.mongowp.bson.BsonType.*;

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
            JAVA_SCRIPT, //Documentation does not indicate JS and JS_WITH_SCOPE order. We choose to place them before MAX
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
     * @param type
     * @return
     */
    static BsonType generalize(BsonType type) {
        if (isNumeric(type)) {
            return DOUBLE;
        }
        assert ORDERED_TYPES.contains(type);
        return type;
    }

    private static boolean isNumeric(BsonType type) {
        return type == INT32 || type == INT64 || type == DOUBLE;
    }

}
