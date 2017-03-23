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
package com.torodb.mongowp.bson;

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
