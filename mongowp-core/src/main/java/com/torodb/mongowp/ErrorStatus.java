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
package com.torodb.mongowp;

import com.google.common.base.Preconditions;

class ErrorStatus<R> implements Status<R> {

  private static final long serialVersionUID = 7915632879225884360L;

  private final ErrorCode errorCode;
  private final String errorMsg;

  public ErrorStatus(ErrorCode errorCode) {
    this(errorCode, null);
  }

  public ErrorStatus(ErrorCode errorCode, String errorMsg) {
    Preconditions.checkArgument(errorCode != ErrorCode.OK);
    this.errorCode = errorCode;
    this.errorMsg = errorMsg;
  }

  @Override
  public ErrorCode getErrorCode() {
    return errorCode;
  }

  @Override
  public boolean isOk() {
    return errorCode == ErrorCode.OK;
  }

  @Override
  public R getResult() throws IllegalStateException {
    throw new IllegalArgumentException("This status is not OK, so there is no result");
  }

  @Override
  public String getErrorMsg() throws IllegalStateException {
    return errorMsg;
  }

  @Override
  public String toString() {
    return "{errCode :" + errorCode + ", errorMsg:" + errorMsg + '}';
  }

}
