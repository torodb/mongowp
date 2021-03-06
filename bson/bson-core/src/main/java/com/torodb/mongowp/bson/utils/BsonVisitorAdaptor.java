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
package com.torodb.mongowp.bson.utils;

import com.torodb.mongowp.bson.BsonArray;
import com.torodb.mongowp.bson.BsonBinary;
import com.torodb.mongowp.bson.BsonBoolean;
import com.torodb.mongowp.bson.BsonDateTime;
import com.torodb.mongowp.bson.BsonDbPointer;
import com.torodb.mongowp.bson.BsonDecimal128;
import com.torodb.mongowp.bson.BsonDeprecated;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.BsonDouble;
import com.torodb.mongowp.bson.BsonInt32;
import com.torodb.mongowp.bson.BsonInt64;
import com.torodb.mongowp.bson.BsonJavaScript;
import com.torodb.mongowp.bson.BsonJavaScriptWithScope;
import com.torodb.mongowp.bson.BsonMax;
import com.torodb.mongowp.bson.BsonMin;
import com.torodb.mongowp.bson.BsonNull;
import com.torodb.mongowp.bson.BsonObjectId;
import com.torodb.mongowp.bson.BsonRegex;
import com.torodb.mongowp.bson.BsonString;
import com.torodb.mongowp.bson.BsonTimestamp;
import com.torodb.mongowp.bson.BsonUndefined;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.BsonValueVisitor;

/**
 * An adaptor that implements a {@link BsonValueVisitor}.
 *
 * @param <R> The type returned by this visitor
 * @param <A> The argument used by this visitor (or {@link Void} if no argument is needed
 */
public class BsonVisitorAdaptor<R, A> implements BsonValueVisitor<R, A> {

  protected R defaultCase(BsonValue<?> value, A arg) {
    return null;
  }

  @Override
  public R visit(BsonArray value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonBinary value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonDbPointer value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonDateTime value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonDocument value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonDouble value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonInt32 value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonInt64 value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonBoolean value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonJavaScript value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonJavaScriptWithScope value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonMax value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonMin value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonNull value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonObjectId value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonRegex value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonString value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonUndefined value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonTimestamp value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonDeprecated value, A arg) {
    return defaultCase(value, arg);
  }

  @Override
  public R visit(BsonDecimal128 value, A arg) {
    return defaultCase(value, arg);
  }

}
