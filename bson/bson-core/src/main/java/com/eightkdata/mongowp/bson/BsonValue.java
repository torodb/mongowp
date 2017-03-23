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
package com.eightkdata.mongowp.bson;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This interface represents BSON value.
 * <p>
 * Even Java compilator does allow it, it is not allowed to directly implement this class. The only
 * classes/interfaces that can directly implement/extend this interface are the ones that are on its
 * package (like {@link BsonArray}, {@link BsonDocument}, {@link BsonInt32}, etc). Specific value
 * implementations must implement these interfaces and it is recommended to extend the abstract
 * clases located on {@linkplain com.eightkdata.mongowp.bson.abst}.
 *
 * @param <V> the java class represented by this BsonValue
 */
public interface BsonValue<V> extends Serializable, Comparable<BsonValue<?>> {

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
   * <p>
   * Each specific interface contains define its own specific equal semantics.
   *
   * @param obj
   * @return
   */
  @Override
  boolean equals(Object obj);

  /**
   * Returns the hash code of the given value.
   * <p>
   * The hash code of a value must be known at compile time and it must be constant.
   *
   * @return
   */
  @Override
  int hashCode();

  boolean isNumber();

  boolean isDouble();

  boolean isInt32();

  boolean isInt64();
  
  boolean isDecimal128();

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

  boolean isDbPointer();

  boolean isJavaScript();

  boolean isJavaScriptWithScope();

  boolean isTimestamp();

  boolean isDeprecated();

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

  BsonDbPointer asDbPointer() throws UnsupportedOperationException;

  BsonJavaScript asJavaScript() throws UnsupportedOperationException;

  BsonJavaScriptWithScope asJavaScriptWithScope() throws UnsupportedOperationException;

  BsonInt32 asInt32() throws UnsupportedOperationException;

  BsonTimestamp asTimestamp() throws UnsupportedOperationException;

  BsonInt64 asInt64() throws UnsupportedOperationException;
  
  BsonDecimal128 asDecimal128() throws UnsupportedOperationException;

  BsonDeprecated asDeprecated() throws UnsupportedOperationException;

  @Nullable
  <R, A> R accept(BsonValueVisitor<R, A> visitor, @Nullable A arg);
}
