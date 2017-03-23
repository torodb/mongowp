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
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.bson.BsonType;

import java.util.Locale;

/**
 *
 */
public class TypesMismatchException extends MongoException {

  private static final long serialVersionUID = 1L;

  private final String fieldId;
  private final String expectedType;
  private final BsonType foundType;

  public TypesMismatchException(String fieldId, BsonType expectedType, BsonType foundType) {
    super(ErrorCode.TYPE_MISMATCH, fieldId, expectedType, foundType);
    this.fieldId = fieldId;
    this.expectedType = expectedType.toString().toLowerCase(Locale.ROOT);
    this.foundType = foundType;
  }

  public TypesMismatchException(String fieldId, BsonType expectedType, BsonType foundType,
      String customMessage) {
    super(customMessage, ErrorCode.TYPE_MISMATCH);
    this.fieldId = fieldId;
    this.expectedType = expectedType.toString().toLowerCase(Locale.ROOT);
    this.foundType = foundType;
  }

  public TypesMismatchException(String fieldId, String expectedType, BsonType foundType) {
    super(ErrorCode.TYPE_MISMATCH, fieldId, expectedType, foundType);
    this.fieldId = fieldId;
    this.expectedType = expectedType;
    this.foundType = foundType;
  }

  public TypesMismatchException(String fieldId, String expectedType, BsonType foundType,
      String customMessage) {
    super(customMessage, ErrorCode.TYPE_MISMATCH);
    this.fieldId = fieldId;
    this.expectedType = expectedType;
    this.foundType = foundType;
  }

  public String getFieldId() {
    return fieldId;
  }

  public String getExpectedType() {
    return expectedType;
  }

  public BsonType getFoundType() {
    return foundType;
  }

  public TypesMismatchException newWithMessage(String customeMessage) {
    return new TypesMismatchException(getFieldId(), getExpectedType(), getFoundType(),
        customeMessage);
  }
}
