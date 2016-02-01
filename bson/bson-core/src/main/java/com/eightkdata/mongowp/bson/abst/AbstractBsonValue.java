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

package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.*;

/**
 *
 */
abstract class AbstractBsonValue<V> implements BsonValue<V> {

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isInt32() {
        return false;
    }

    @Override
    public boolean isInt64() {
        return false;
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public boolean isDocument() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isUndefined() {
        return false;
    }

    @Override
    public boolean isObjectId() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isDateTime() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isRegex() {
        return false;
    }

    @Override
    public boolean isJavaScript() {
        return false;
    }

    @Override
    public boolean isJavaScriptWithScope() {
        return false;
    }

    @Override
    public boolean isTimestamp() {
        return false;
    }

    @Override
    public BsonNumber asNumber() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to number");
    }

    @Override
    public BsonDouble asDouble() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to double");
    }

    @Override
    public BsonString asString() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to string");
    }

    @Override
    public BsonDocument asDocument() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to document");
    }

    @Override
    public BsonArray asArray() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to array");
    }

    @Override
    public BsonBinary asBinary() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to binary");
    }

    @Override
    public BsonUndefined asUndefined() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to undefined");
    }

    @Override
    public BsonObjectId asObjectId() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to objectId");
    }

    @Override
    public BsonBoolean asBoolean() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to boolean");
    }

    @Override
    public BsonDateTime asDateTime() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to dateTime");
    }

    @Override
    public BsonNull asNull() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to null");
    }

    @Override
    public BsonRegex asRegex() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to regex");
    }

    @Override
    public BsonJavaScript asJavaScript() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to javaScript");
    }

    @Override
    public BsonJavaScriptWithScope asJavaScriptWithScope() throws
            UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to javaScript with scope");
    }

    @Override
    public BsonInt32 asInt32() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to int32");
    }

    @Override
    public BsonTimestamp asTimestamp() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to timestamp");
    }

    @Override
    public BsonInt64 asInt64() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Values of type " + getType() + " cannot be casted to int64");
    }

}
