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
import com.eightkdata.mongowp.bson.BsonDocument;

import javax.annotation.Nullable;

/**
 *
 */
public class NodeNotFoundException extends MongoException {

  private static final long serialVersionUID = -1959031904353181467L;

  @Nullable
  private final BsonDocument replSetConf;

  public NodeNotFoundException() {
    this((BsonDocument) null);
  }

  public NodeNotFoundException(BsonDocument replSetConf) {
    super(ErrorCode.NODE_NOT_FOUND);
    this.replSetConf = replSetConf;
  }

  public NodeNotFoundException(String customMessage) {
    this(null, customMessage);
  }

  public NodeNotFoundException(BsonDocument replSetConf, String customMessage) {
    super(customMessage, ErrorCode.NODE_NOT_FOUND);
    this.replSetConf = replSetConf;
  }

  public BsonDocument getReplSetConf() {
    return replSetConf;
  }
}
