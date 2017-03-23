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
public class NodeNotElectableException extends MongoException {

  private static final long serialVersionUID = -4393269068299293234L;

  private final int id;
  private final int configVersion;
  private final String replSetName;

  public NodeNotElectableException(int id, int configVersion, String replSetName) {
    super(ErrorCode.NODE_NOT_ELECTABLE);
    this.id = id;
    this.configVersion = configVersion;
    this.replSetName = replSetName;
  }

  public NodeNotElectableException(int id, int configVersion, String replSetName,
      String customMessage) {
    super(customMessage, ErrorCode.NODE_NOT_ELECTABLE);
    this.id = id;
    this.configVersion = configVersion;
    this.replSetName = replSetName;
  }

  public int getId() {
    return id;
  }

  public int getConfigVersion() {
    return configVersion;
  }

  public String getReplSetName() {
    return replSetName;
  }

}
