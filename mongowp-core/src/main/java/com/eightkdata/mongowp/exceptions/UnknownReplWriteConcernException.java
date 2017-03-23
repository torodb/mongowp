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
public class UnknownReplWriteConcernException extends MongoException {

  private static final long serialVersionUID = 6625110874596682355L;

  private final String writeConcernId;

  public UnknownReplWriteConcernException(String writeConcernId) {
    super(ErrorCode.UNKNOWN_REPL_WRITE_CONCERN);
    this.writeConcernId = writeConcernId;
  }

  public UnknownReplWriteConcernException(String writeConcernId, String customMessage) {
    super(customMessage, ErrorCode.UNKNOWN_REPL_WRITE_CONCERN);
    this.writeConcernId = writeConcernId;
  }

  public String getWriteConcernId() {
    return writeConcernId;
  }

}
