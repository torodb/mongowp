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

package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonBinary;
import com.eightkdata.mongowp.bson.BsonBoolean;
import com.eightkdata.mongowp.bson.BsonDateTime;
import com.eightkdata.mongowp.bson.BsonDbPointer;
import com.eightkdata.mongowp.bson.BsonDecimal128;
import com.eightkdata.mongowp.bson.BsonDeprecated;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDouble;
import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.BsonInt64;
import com.eightkdata.mongowp.bson.BsonJavaScript;
import com.eightkdata.mongowp.bson.BsonJavaScriptWithScope;
import com.eightkdata.mongowp.bson.BsonNull;
import com.eightkdata.mongowp.bson.BsonNumber;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.bson.BsonRegex;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.BsonTimestamp;
import com.eightkdata.mongowp.bson.BsonUndefined;
import com.eightkdata.mongowp.bson.BsonValue;

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
  public boolean isDbPointer() {
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
  public boolean isDeprecated() {
    return false;
  }

  @Override
  public boolean isDecimal128() {
    return false;
  }
  
  @Override
  public BsonNumber asNumber() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to number");
  }

  @Override
  public BsonDouble asDouble() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to double");
  }

  @Override
  public BsonString asString() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to string");
  }

  @Override
  public BsonDocument asDocument() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to document");
  }

  @Override
  public BsonArray asArray() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to array");
  }

  @Override
  public BsonBinary asBinary() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to binary");
  }

  @Override
  public BsonUndefined asUndefined() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to undefined");
  }

  @Override
  public BsonObjectId asObjectId() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to objectId");
  }

  @Override
  public BsonBoolean asBoolean() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to boolean");
  }

  @Override
  public BsonDateTime asDateTime() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to dateTime");
  }

  @Override
  public BsonNull asNull() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to null");
  }

  @Override
  public BsonRegex asRegex() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to regex");
  }

  @Override
  public BsonDbPointer asDbPointer() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to dbPointer");
  }

  @Override
  public BsonJavaScript asJavaScript() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to javaScript");
  }

  @Override
  public BsonJavaScriptWithScope asJavaScriptWithScope() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to javaScript with scope");
  }

  @Override
  public BsonInt32 asInt32() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to int32");
  }

  @Override
  public BsonTimestamp asTimestamp() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to timestamp");
  }

  @Override
  public BsonInt64 asInt64() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to int64");
  }

  @Override
  public BsonDecimal128 asDecimal128() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to decimal128");
  }
  
  @Override
  public BsonDeprecated asDeprecated() throws UnsupportedOperationException {
    throw new UnsupportedOperationException(
        "Values of type " + getType() + " cannot be casted to deprecated");
  }

}
