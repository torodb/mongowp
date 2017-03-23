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
package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonDateTime;

import java.time.Instant;

/**
 *
 */
public class LongBsonDateTime extends AbstractBsonDateTime {

  private static final long serialVersionUID = -6469977865641228229L;

  private final long millis;

  public LongBsonDateTime(long millis) {
    this.millis = millis;
  }

  @Override
  public long getMillisFromUnix() {
    return millis;
  }

  @Override
  public Instant getValue() {
    return Instant.ofEpochMilli(millis);
  }

}
