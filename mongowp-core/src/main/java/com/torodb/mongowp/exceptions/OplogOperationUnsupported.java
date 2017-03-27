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
package com.torodb.mongowp.exceptions;

import com.torodb.mongowp.ErrorCode;
import com.torodb.mongowp.annotations.Material;
import com.torodb.mongowp.bson.BsonDocument;

import javax.annotation.Nullable;

/**
 *
 */
public class OplogOperationUnsupported extends MongoException {

  private static final long serialVersionUID = 1L;
  @Material
  private final transient BsonDocument oplogOperationDoc;
  private final String docAsString;

  public OplogOperationUnsupported() {
    super(ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
    this.oplogOperationDoc = null;
    docAsString = null;
  }

  public OplogOperationUnsupported(@Material @Nullable BsonDocument oplogOperationDoc) {
    super(ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
    this.oplogOperationDoc = oplogOperationDoc;
    docAsString = oplogOperationDoc != null ? oplogOperationDoc.toString() : null;
  }

  public OplogOperationUnsupported(Throwable cause) {
    super(cause, ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
    this.oplogOperationDoc = null;
    docAsString = null;
  }

  public OplogOperationUnsupported(@Nullable @Material BsonDocument oplogOperationDoc,
      Throwable cause) {
    super(cause, ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
    this.oplogOperationDoc = oplogOperationDoc;
    docAsString = oplogOperationDoc != null ? oplogOperationDoc.toString() : null;
  }

  @Nullable
  @Material
  public BsonDocument getOplogOperationDoc() {
    return oplogOperationDoc;
  }

  public String getDocAsString() {
    return docAsString;
  }
}
