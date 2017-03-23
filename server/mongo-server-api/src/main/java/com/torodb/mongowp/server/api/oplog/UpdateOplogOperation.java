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
package com.torodb.mongowp.server.api.oplog;

import com.torodb.mongowp.OpTime;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.fields.BooleanField;
import com.torodb.mongowp.fields.DocField;
import com.torodb.mongowp.utils.BsonDocumentBuilder;

import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class UpdateOplogOperation extends CollectionOplogOperation {

  private static final long serialVersionUID = 1L;

  private static final DocField FILTER_FIELD = new DocField("o");
  private static final DocField MODIFICATION_FIELD = new DocField("o2");
  private static final BooleanField UPSERT_FIELD = new BooleanField("b");

  private final BsonDocument filter;
  //TODO: Change to a type safe object
  private final BsonDocument modification;
  private final boolean upsert;

  public UpdateOplogOperation(
      BsonDocument filter,
      String database,
      String collection,
      OpTime optime,
      long h,
      OplogVersion version,
      boolean fromMigrate,
      BsonDocument modification,
      boolean upsert) {
    super(database, collection, optime, h, version, fromMigrate);
    this.filter = filter;
    this.modification = modification;
    this.upsert = upsert;
  }

  public BsonDocument getFilter() {
    return filter;
  }

  public BsonDocument getModification() {
    return modification;
  }

  public boolean isUpsert() {
    return upsert;
  }

  @Override
  public OplogOperationType getType() {
    return OplogOperationType.UPDATE;
  }

  @Override
  public BsonValue<?> getDocId() {
    return getFilter().get("_id");
  }

  @Override
  public BsonDocumentBuilder toDescriptiveBson() {
    return super.toDescriptiveBson()
        .append(OP_FIELD, getType().getOplogName())
        .append(FILTER_FIELD, filter)
        .append(MODIFICATION_FIELD, modification)
        .append(UPSERT_FIELD, upsert);
  }

  @Override
  public <R, A> R accept(OplogOperationVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
