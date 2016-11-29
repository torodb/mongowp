/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.ObjectIdField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;

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
