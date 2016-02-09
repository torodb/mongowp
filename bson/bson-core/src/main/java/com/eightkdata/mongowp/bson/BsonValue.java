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

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This interface represents BSON value.
 *
 * Even Java compilator does allow it, it is not allowed to directly implement
 * this class. The only classes/interfaces that can directly implement/extend
 * this interface are the ones that are on its package (like {@link BsonArray}, 
 * {@link BsonDocument}, {@link BsonInt32}, etc). Specific value
 * implementations must implement these interfaces and it is recommended to
 * extend the abstract clases located on {@linkplain com.eightkdata.mongowp.bson.abst}.
 *
 * @param <V> the java class represented by this BsonValue
 */
public interface BsonValue<V> extends Serializable {

    @Nonnull
    BsonType getType();

    Class<? extends V> getValueClass();

    /**
     * Returns the Java value represented by this object.
     *
     * @return
     */
    @Nonnull
    V getValue();

    /**
     * Two BsonValues are equal iff their types and java value are equals.
     *
     * Each specific interface contains define its own specific equal semantics.
     * @param obj
     * @return 
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code of the given value.
     *
     * The hash code of a value must be known at compile time and it must be
     * constant.
     * @return
     */
    @Override
    int hashCode();

    boolean isNumber();
    boolean isDouble();
    boolean isInt32();
    boolean isInt64();
    boolean isString();
    boolean isDocument();
    boolean isArray();
    boolean isBinary();
    boolean isUndefined();
    boolean isObjectId();
    boolean isBoolean();
    boolean isDateTime();
    boolean isNull();
    boolean isRegex();
    boolean isJavaScript();
    boolean isJavaScriptWithScope();
    boolean isTimestamp();

    BsonNumber asNumber() throws UnsupportedOperationException;
    BsonDouble asDouble() throws UnsupportedOperationException;
    BsonString asString() throws UnsupportedOperationException;
    BsonDocument asDocument() throws UnsupportedOperationException;
    BsonArray asArray() throws UnsupportedOperationException;
    BsonBinary asBinary() throws UnsupportedOperationException;
    BsonUndefined asUndefined() throws UnsupportedOperationException;
    BsonObjectId asObjectId() throws UnsupportedOperationException;
    BsonBoolean asBoolean() throws UnsupportedOperationException;
    BsonDateTime asDateTime() throws UnsupportedOperationException;
    BsonNull asNull() throws UnsupportedOperationException;
    BsonRegex asRegex() throws UnsupportedOperationException;
    BsonJavaScript asJavaScript() throws UnsupportedOperationException;
    BsonJavaScriptWithScope asJavaScriptWithScope() throws UnsupportedOperationException;
    BsonInt32 asInt32() throws UnsupportedOperationException;
    BsonTimestamp asTimestamp() throws UnsupportedOperationException;
    BsonInt64 asInt64() throws UnsupportedOperationException;

    @Nullable
    <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, @Nullable Arg arg);
}
