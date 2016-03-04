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

import com.eightkdata.mongowp.bson.abst.AbstractBsonTimestamp;

/**
 *
 * @param <Result> The type returned by this visitor
 * @param <Arg>    The argument used by this visitor (or {@link Void} if no
 *                 argument is needed
 */
public interface BsonValueVisitor<Result, Arg> {

    Result visit(BsonArray value, Arg arg);

    Result visit(BsonBinary value, Arg arg);

    Result visit(BsonDbPointer value, Arg arg);

    Result visit(BsonDateTime value, Arg arg);

    Result visit(BsonDocument value, Arg arg);

    Result visit(BsonDouble value, Arg arg);

    Result visit(BsonInt32 value, Arg arg);

    Result visit(BsonInt64 value, Arg arg);

    Result visit(BsonBoolean value, Arg arg);

    Result visit(BsonJavaScript value, Arg arg);

    Result visit(BsonJavaScriptWithScope value, Arg arg);

    Result visit(BsonMax value, Arg arg);

    Result visit(BsonMin value, Arg arg);

    Result visit(BsonNull value, Arg arg);

    Result visit(BsonObjectId value, Arg arg);

    Result visit(BsonRegex value, Arg arg);

    Result visit(BsonString value, Arg arg);

    Result visit(BsonUndefined value, Arg arg);

    Result visit(BsonTimestamp value, Arg arg);

    Result visit(BsonDeprecated value, Arg arg);
}
