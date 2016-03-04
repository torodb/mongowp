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

import com.eightkdata.mongowp.bson.*;

/**
 *
 * @param <Result>
 * @param <Arg>
 */
public class BsonVisitorAdaptor<Result, Arg> implements BsonValueVisitor<Result, Arg>{

    protected Result defaultCase(BsonValue<?> value, Arg arg) {
        return null;
    }

    @Override
    public Result visit(BsonArray value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonBinary value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonDbPointer value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonDateTime value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonDocument value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonDouble value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonInt32 value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonInt64 value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonBoolean value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonJavaScript value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonJavaScriptWithScope value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonMax value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonMin value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonNull value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonObjectId value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonRegex value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonString value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonUndefined value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonTimestamp value, Arg arg) {
        return defaultCase(value, arg);
    }

    @Override
    public Result visit(BsonDeprecated value, Arg arg) {
        return defaultCase(value, arg);
    }

}
