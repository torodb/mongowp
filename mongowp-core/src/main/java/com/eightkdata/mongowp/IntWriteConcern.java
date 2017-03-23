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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 */
class IntWriteConcern extends WriteConcern {

  private final int w;

  IntWriteConcern(@Nonnull SyncMode syncMode, @Nonnegative int timeout, int w) {
    super(syncMode, timeout);
    this.w = w;
  }

  @Override
  public int getWInt() throws UnsupportedOperationException {
    return w;
  }

  @Override
  public String getWString() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("This write concern is using 'w' as an int");
  }

  @Override
  public WType getWType() {
    return WType.INT;
  }

}
