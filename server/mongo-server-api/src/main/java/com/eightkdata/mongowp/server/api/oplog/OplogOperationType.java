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
package com.eightkdata.mongowp.server.api.oplog;

/**
 *
 */
public enum OplogOperationType {

  INSERT("i"),
  UPDATE("u"),
  DELETE("d"),
  DB_CMD("c"),
  /**
   * declares presence of a database ({@link OplogOperation#namespace} is set to the db name + '.')
   */
  DB("db"),
  /**
   * used for changes in the database or collections which don't result in a change in the stored
   * data or as a <em>keep alive</em> operation
   */
  NOOP("n");
  private final String oplogName;

  private OplogOperationType(String oplogName) {
    this.oplogName = oplogName;
  }

  public String getOplogName() {
    return oplogName;
  }

  public static OplogOperationType fromOplogName(String name) throws IllegalArgumentException {
    for (OplogOperationType value : OplogOperationType.values()) {
      if (value.getOplogName().equals(name)) {
        return value;
      }
    }
    throw new IllegalArgumentException("There is no oplog type whose name is '"
        + name + '\'');
  }
}
