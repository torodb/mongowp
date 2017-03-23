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
import com.eightkdata.mongowp.Status;

import java.text.MessageFormat;

import javax.annotation.Nonnull;

/**
 *
 */
public class MongoException extends Exception {

  private static final long serialVersionUID = 1L;

  private final ErrorCode errorCode;

  public MongoException(ErrorCode errorCode) {
    super(errorCode.getErrorMessage());
    this.errorCode = errorCode;
  }

  public MongoException(Status<?> status) {
    super(status.getErrorMsg());
    errorCode = status.getErrorCode();
  }

  public MongoException(
      @Nonnull String customMessage,
      @Nonnull ErrorCode errorCode) {
    super(customMessage);
    this.errorCode = errorCode;
  }

  public MongoException(
      @Nonnull String customMessage,
      @Nonnull Throwable cause,
      @Nonnull ErrorCode errorCode) {
    super(customMessage, cause);
    this.errorCode = errorCode;
  }

  public MongoException(
      @Nonnull ErrorCode errorCode,
      @Nonnull Object... args) {
    super(calculateMessage(errorCode, args));
    this.errorCode = errorCode;
  }

  public MongoException(
      @Nonnull Throwable cause,
      @Nonnull ErrorCode errorCode,
      @Nonnull Object... args) {
    super(calculateMessage(errorCode, args), cause);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  private static String calculateMessage(ErrorCode errorCode, Object... args) {
    try {
      return MessageFormat.format(errorCode.getErrorMessage(), args);
    } catch (IllegalArgumentException ex) {
      return "Unknown error message";
    }
  }
}
