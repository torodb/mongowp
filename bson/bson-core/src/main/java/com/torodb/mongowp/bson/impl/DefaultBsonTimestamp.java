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
package com.torodb.mongowp.bson.impl;

import com.torodb.mongowp.bson.abst.AbstractBsonTimestamp;

/**
 *
 */
public class DefaultBsonTimestamp extends AbstractBsonTimestamp {

  private static final long serialVersionUID = 8902213292168089542L;
  private final int secondsSinceEpoch;
  private final int ordinal;

  public DefaultBsonTimestamp(int secondsSinceEpoch, int ordinal) {
    this.secondsSinceEpoch = secondsSinceEpoch;
    this.ordinal = ordinal;
  }

  @Override
  public int getSecondsSinceEpoch() {
    return secondsSinceEpoch;
  }

  @Override
  public int getOrdinal() {
    return ordinal;
  }
}
