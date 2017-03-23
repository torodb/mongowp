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
import com.torodb.mongowp.fields.DoubleField;
import com.torodb.mongowp.fields.IntField;
import com.torodb.mongowp.fields.StringField;
import com.torodb.mongowp.fields.TimestampField;
import com.torodb.mongowp.server.callback.WriteOpResult;
import com.torodb.mongowp.utils.BsonDocumentBuilder;

import java.io.Serializable;
import java.text.MessageFormat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class SimpleWriteOpResult implements WriteOpResult {

  private static final StringField ERR_FIELD_NAME = new StringField("err");
  private static final IntField CODE_FIELD_NAME = new IntField("code");
  private static final DoubleField N_FIELD_NAME = new DoubleField("n");
  private static final long serialVersionUID = 1L;

  @Nonnull
  private final ErrorCode error;
  @Nullable
  private final String errorDesc;
  @Nullable
  private final ReplicationInformation replInfo;
  @Nullable
  private final ShardingInformation shardInfo;
  @Nonnull
  private final OpTime optime;

  public SimpleWriteOpResult(
      @Nonnull ErrorCode error,
      @Nullable String errorDesc,
      @Nullable ReplicationInformation replInfo,
      @Nullable ShardingInformation shardInfo,
      @Nonnull OpTime optime) {
    this.error = error;
    this.replInfo = replInfo;
    this.shardInfo = shardInfo;

    if (errorDesc != null && error.equals(ErrorCode.OK)) {
      throw new IllegalArgumentException("Error description must be "
          + "null when the given error code is OK");
    }
    this.errorDesc = errorDesc;
    this.optime = optime;
  }

  public SimpleWriteOpResult(
      @Nonnull ErrorCode error,
      @Nullable ReplicationInformation replInfo,
      @Nullable ShardingInformation shardInfo,
      @Nonnull OpTime optime,
      Object... args) {
    this.error = error;
    this.replInfo = replInfo;
    this.shardInfo = shardInfo;

    if (error.equals(ErrorCode.OK)) {
      if (args.length != 0) {
        throw new IllegalArgumentException("Error description must be "
            + "null when the given error code is OK");
      }
      this.errorDesc = null;
    } else {
      errorDesc = MessageFormat.format(error.getErrorMessage(), args);
    }
    this.optime = optime;
  }

  @Override
  public OpTime getOpTime() {
    return optime;
  }

  @Override
  public ErrorCode getErrorCode() {
    return error;
  }

  public String getErrorDesc() {
    return errorDesc;
  }

  public ReplicationInformation getReplInfo() {
    return replInfo;
  }

  public ShardingInformation getShardInfo() {
    return shardInfo;
  }

  public long getN() {
    return 0;
  }

  protected void marshall(BsonDocumentBuilder builder) {
    if (errorDesc == null) {
      assert error.equals(ErrorCode.OK);
      builder.appendNull(ERR_FIELD_NAME);
    } else {
      builder.append(ERR_FIELD_NAME, errorDesc);
      builder.append(CODE_FIELD_NAME, error.getErrorCode());
    }
    builder.append(N_FIELD_NAME, getN());

    if (replInfo != null) {
      replInfo.marshall(builder);
    }
    if (shardInfo != null) {
      shardInfo.marshall(builder);
    }
  }

  @Override
  public BsonDocument marshall() {
    BsonDocumentBuilder builder = new BsonDocumentBuilder();

    marshall(builder);

    return builder.build();
  }

  @Override
  public boolean errorOcurred() {
    return !error.equals(ErrorCode.OK);
  }

  public static class ReplicationInformation implements Serializable {

    private static final TimestampField LAST_OP_FIELD = new TimestampField("lastOp");
    private static final long serialVersionUID = 1L;
    @Nonnull
    private final OpTime precedingOpTime;

    public ReplicationInformation(@Nonnull OpTime precedingOpTime) {
      this.precedingOpTime = precedingOpTime;
    }

    public OpTime getPrecedingOpTime() {
      return precedingOpTime;
    }

    private void marshall(BsonDocumentBuilder builder) {
      builder.append(LAST_OP_FIELD, precedingOpTime);
    }
  }

  //TODO: Implement this class
  public static class ShardingInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    private ShardingInformation() {
    }

    private void marshall(BsonDocumentBuilder builder) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }

}
