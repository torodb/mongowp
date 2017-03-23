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
package com.eightkdata.mongowp.server.exception;

import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.server.callback.WriteOpResult;
import com.google.common.base.Preconditions;

/**
 *
 */
public class ErrorOnWriteException extends MongoException {

  private static final long serialVersionUID = 1L;

  private final transient WriteOpResult writeOpResult;

  public ErrorOnWriteException(WriteOpResult writeOpResult, String customMessage) {
    super(customMessage, writeOpResult.getErrorCode());
    Preconditions.checkArgument(writeOpResult.errorOcurred(),
        "trying to create an " + getClass().getName() + " with a "
        + "WriteOpResult that does not contain an error!");
    this.writeOpResult = writeOpResult;
  }

  public WriteOpResult getWriteOpResult() {
    return writeOpResult;
  }
}
