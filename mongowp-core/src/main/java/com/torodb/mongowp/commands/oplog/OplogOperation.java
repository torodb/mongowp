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

import com.torodb.mongowp.OpTime;
import com.torodb.mongowp.fields.BooleanField;
import com.torodb.mongowp.fields.IntField;
import com.torodb.mongowp.fields.LongField;
import com.torodb.mongowp.fields.StringField;
import com.torodb.mongowp.fields.TimestampField;
import com.torodb.mongowp.utils.BsonDocumentBuilder;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class OplogOperation implements Serializable {

  private static final long serialVersionUID = 1L;

  static final StringField OP_FIELD = new StringField("op");
  private static final TimestampField OPTIME_FIELD = new TimestampField("ts");
  private static final LongField HASH_FIELD = new LongField("h");
  private static final IntField VERSION_FIELD = new IntField("v");
  private static final BooleanField FROM_MIGRATE_FIELD = new BooleanField("fromMigrate");

  @Nonnull
  private final String database;
  /**
   * when this operation was performed
   */
  @Nonnull
  private final OpTime optime;
  private final long hash;
  @Nonnull
  private final OplogVersion version;
  private final boolean fromMigrate;

  public OplogOperation(
      @Nonnull String database,
      @Nonnull OpTime optime,
      long h,
      @Nonnull OplogVersion version,
      boolean fromMigrate) {
    this.database = database;
    this.optime = optime;
    this.hash = h;
    this.version = version;
    this.fromMigrate = fromMigrate;
  }

  public abstract OplogOperationType getType();

  public abstract <R, A> R accept(OplogOperationVisitor<R, A> visitor, A arg);

  public String getDatabase() {
    return database;
  }

  public OpTime getOpTime() {
    return optime;
  }

  public long getHash() {
    return hash;
  }

  public OplogVersion getVersion() {
    return version;
  }

  public boolean isFromMigrate() {
    return fromMigrate;
  }

  public BsonDocumentBuilder toDescriptiveBson() {
    BsonDocumentBuilder result = new BsonDocumentBuilder()
        .append(OPTIME_FIELD, optime)
        .append(HASH_FIELD, hash)
        .append(VERSION_FIELD, version.getNumericValue());
    if (fromMigrate) {
      result.append(FROM_MIGRATE_FIELD, true);
    }
    return result;
  }

  @Override
  public String toString() {
    BsonDocumentBuilder bson = toDescriptiveBson();
    return bson.build().toString();
  }
}
