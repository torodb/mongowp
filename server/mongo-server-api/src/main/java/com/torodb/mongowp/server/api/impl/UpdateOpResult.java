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
package com.torodb.mongowp.server.api.impl;

import com.torodb.mongowp.ErrorCode;
import com.torodb.mongowp.OpTime;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.BsonObjectId;
import com.torodb.mongowp.fields.BooleanField;
import com.torodb.mongowp.fields.ObjectIdField;
import com.torodb.mongowp.utils.BsonDocumentBuilder;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 *
 */
public class UpdateOpResult extends SimpleWriteOpResult {

  private static final BooleanField UPDATE_EXISTING_FIELD_NAME =
      new BooleanField("updatedExisting");
  private static final ObjectIdField UPSERTED_FIELD_NAME = new ObjectIdField("upserted");
  private static final long serialVersionUID = 1423475743853953021L;

  private final long candidates;
  private final long modified;
  private final boolean docReplacement;
  @Nullable
  private final BsonObjectId upsertedId;

  public UpdateOpResult(
      @Nonnegative long candidates,
      @Nonnegative long modified,
      boolean docReplacement,
      ErrorCode error,
      String errorDesc,
      ReplicationInformation replInfo,
      ShardingInformation shardInfo,
      OpTime optime) {
    super(error, errorDesc, replInfo, shardInfo, optime);
    this.candidates = candidates;
    this.modified = modified;
    this.upsertedId = null;
    this.docReplacement = docReplacement;
  }

  public UpdateOpResult(
      @Nonnegative long candidates,
      @Nonnegative long modified,
      boolean docReplacement,
      ErrorCode error,
      ReplicationInformation replInfo,
      ShardingInformation shardInfo,
      OpTime optime,
      Object... args) {
    super(error, replInfo, shardInfo, optime, args);
    this.candidates = candidates;
    this.modified = modified;
    this.upsertedId = null;
    this.docReplacement = docReplacement;
  }

  public UpdateOpResult(
      BsonObjectId upsertedId,
      boolean docReplacement,
      ErrorCode error,
      String errorDesc,
      ReplicationInformation replInfo,
      ShardingInformation shardInfo,
      OpTime optime) {
    super(error, errorDesc, replInfo, shardInfo, optime);
    this.candidates = 0;
    this.modified = 0;
    this.upsertedId = upsertedId;
    this.docReplacement = docReplacement;
  }

  public UpdateOpResult(
      BsonObjectId upsertedId,
      boolean updateObjects,
      boolean docReplacement,
      ErrorCode error,
      ReplicationInformation replInfo,
      ShardingInformation shardInfo,
      OpTime optime,
      Object... args) {
    super(error, replInfo, shardInfo, optime, args);
    this.candidates = 0;
    this.modified = 0;
    this.upsertedId = upsertedId;
    this.docReplacement = docReplacement;
  }

  @Override
  public long getN() {
    if (upsertedId != null) {
      assert candidates == 0;
      return 1;
    }
    return candidates;
  }

  @Nonnegative
  public long getCandidates() {
    return candidates;
  }

  @Nonnegative
  public long getModified() {
    return modified;
  }

  public boolean isDocReplacement() {
    return docReplacement;
  }

  public BsonObjectId getUpsertedId() {
    return upsertedId;
  }

  @Override
  public BsonDocument marshall() {
    BsonDocumentBuilder builder = new BsonDocumentBuilder();
    marshall(builder);

    builder.append(UPDATE_EXISTING_FIELD_NAME, modified != 0);
    if (upsertedId != null) {
      builder.append(UPSERTED_FIELD_NAME, upsertedId);
    }

    return builder.build();
  }

}
