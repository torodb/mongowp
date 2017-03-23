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
package com.torodb.mongowp.commands.oplog;

/**
 *
 */
public enum OplogVersion {
  V1(1),
  V2(2);

  private final int numericValue;

  private OplogVersion(int numericValue) {
    this.numericValue = numericValue;
  }

  public static OplogVersion valueOf(int i) {
    for (OplogVersion oplogVersion : OplogVersion.values()) {
      if (oplogVersion.numericValue == i) {
        return oplogVersion;
      }
    }
    throw new IllegalArgumentException("Unknown version " + i);
  }

  public int getNumericValue() {
    return numericValue;
  }
}
