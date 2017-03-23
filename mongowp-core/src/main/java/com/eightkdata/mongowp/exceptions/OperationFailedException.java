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

/**
 *
 */
public class OperationFailedException extends MongoException {

  private static final long serialVersionUID = 1L;

  private final String reason;

  public OperationFailedException() {
    super(ErrorCode.OPERATION_FAILED);
    this.reason = "not specified";
  }

  public OperationFailedException(String reason) {
    super(ErrorCode.OPERATION_FAILED, reason);
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }

}
