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
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.CommandResult;
import com.eightkdata.mongowp.server.callback.WriteOpResult;

import javax.annotation.Nonnull;

/**
 *
 */
public class WriteCommandResult<R> implements CommandResult<R> {

  @Nonnull
  private final R result;
  private final WriteOpResult writeOpResult;

  public WriteCommandResult(@Nonnull R result, @Nonnull WriteOpResult writeOpResult) {
    this.result = result;
    this.writeOpResult = writeOpResult;
  }

  @Nonnull
  @Override
  public WriteOpResult getWriteOpResult() {
    return writeOpResult;
  }

  @Override
  @Nonnull
  public R getResult() {
    return result;
  }

}
