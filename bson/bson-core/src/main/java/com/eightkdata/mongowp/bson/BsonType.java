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
 * along with bson. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson;

import javax.annotation.Nonnull;

/**
 *
 */
public enum BsonType {
    DOUBLE(BsonDouble.class,1),
    STRING(BsonString.class, 2),
    DOCUMENT(BsonDocument.class, 3),
    ARRAY(BsonArray.class, 4),
    BINARY(BsonBinary.class, 5),
    UNDEFINED(BsonUndefined.class, 6),
    OBJECT_ID(BsonObjectId.class, 7),
    BOOLEAN(BsonBoolean.class, 8),
    DATETIME(BsonDateTime.class, 9),
    NULL(BsonNull.class, 10),
    REGEX(BsonRegex.class, 11),
    DB_POINTER(BsonDbPointer.class, 12),
    JAVA_SCRIPT(BsonJavaScript.class, 13),
    DEPRECTED(BsonDeprecated.class, 14),
    JAVA_SCRIPT_WITH_SCOPE(BsonJavaScriptWithScope.class, 15),
    INT32(BsonInt32.class, 16),
    TIMESTAMP(BsonTimestamp.class, 17),
    INT64(BsonInt64.class, 18),
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
