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
package com.eightkdata.mongowp;

import com.eightkdata.mongowp.exceptions.MongoException;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Optional;

import javax.annotation.Nullable;

public interface Status<R> extends Serializable {

  public ErrorCode getErrorCode();

  public default boolean isOk() {
    return getErrorCode() == ErrorCode.OK;
  }

  @Nullable
  public R getResult() throws IllegalStateException;

  @Nullable
  public String getErrorMsg() throws IllegalStateException;

  public default Optional<R> asOptional() {
    if (isOk()) {
      return Optional.ofNullable(getResult());
    }
    return Optional.empty();
  }

  public static <T> Status<T> ok() {
    return (Status<T>) OkStatus.OK;
  }

  public static <T> Status<T> ok(T result) {
    return new OkStatus<>(result);
  }

  public static <T> Status<T> from(ErrorCode errorCode) {
    return new ErrorStatus<>(errorCode);
  }

  public static <T> Status<T> from(ErrorCode errorCode, String errorMsg) {
    return new ErrorStatus<>(errorCode, errorMsg);
  }

  public static <T> Status<T> from(MongoException ex) {
    return new ErrorStatus<>(ex.getErrorCode(), ex.getLocalizedMessage());
  }

  public static <T> Status<T> withDefaultMsg(ErrorCode errorCode, Object... args) {
    String errorMsg;
    try {
      errorMsg = MessageFormat.format(errorCode.getErrorMessage(), args);
    } catch (IllegalArgumentException ex) {
      errorMsg = "Unknown error message";
    }

    return new ErrorStatus<>(errorCode, errorMsg);
  }

}
