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

package com.eightkdata.mongowp.bson;

/**
 * A visitor that visits {@link BsonValue bson values}.
 *
 * @param <R> The type returned by this visitor
 * @param <A> The argument used by this visitor (or {@link Void} if no argument is needed
 */
public interface BsonValueVisitor<R, A> {

  R visit(BsonArray value, A arg);

  R visit(BsonBinary value, A arg);

  R visit(BsonDbPointer value, A arg);

  R visit(BsonDateTime value, A arg);

  R visit(BsonDocument value, A arg);

  R visit(BsonDouble value, A arg);

  R visit(BsonInt32 value, A arg);

  R visit(BsonInt64 value, A arg);

  R visit(BsonBoolean value, A arg);

  R visit(BsonJavaScript value, A arg);

  R visit(BsonJavaScriptWithScope value, A arg);

  R visit(BsonMax value, A arg);

  R visit(BsonMin value, A arg);

  R visit(BsonNull value, A arg);

  R visit(BsonObjectId value, A arg);

  R visit(BsonRegex value, A arg);

  R visit(BsonString value, A arg);

  R visit(BsonUndefined value, A arg);

  R visit(BsonTimestamp value, A arg);

  R visit(BsonDeprecated value, A arg);

  R visit(BsonDecimal128 value, A arg);
}
