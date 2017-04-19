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
public class DeleteOplogOperation extends CollectionOplogOperation {

  private static final long serialVersionUID = 1L;

  private static final DocField FILFER_FIELD = new DocField("o");
  private static final BooleanField JUST_ONE_FIELD = new BooleanField("b");

  private final BsonDocument filter;
  private final boolean justOne;

  public DeleteOplogOperation(
      BsonDocument filter,
      String database,
      String collection,
      OpTime optime,
      long h,
      OplogVersion version,
      boolean fromMigrate,
      boolean justOne) {
    super(database, collection, optime, h, version, fromMigrate);
    this.filter = filter;
    this.justOne = justOne;
  }

  public BsonDocument getFilter() {
    return filter;
  }

  public boolean isJustOne() {
    return justOne;
  }

  @Override
  public OplogOperationType getType() {
    return OplogOperationType.DELETE;
  }

  @Override
  public BsonValue<?> getDocId() {
    return getFilter().get("_id");
  }

  @Override
  public BsonDocumentBuilder toDescriptiveBson() {
    return super.toDescriptiveBson()
        .append(OP_FIELD, getType().getOplogName())
        .append(FILFER_FIELD, filter)
        .append(JUST_ONE_FIELD, justOne);
  }

  @Override
  public int hashCode() {
    //This is here to explicity say we know this hashCode is compatible with equals
    //to avoid static check warnings
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!generalEquals(obj)) {
      return false;
    }
    DeleteOplogOperation other = (DeleteOplogOperation) obj;
    return other.isJustOne() == this.isJustOne() && other.filter.equals(this.filter);
  }

  @Override
  public <R, A> R accept(OplogOperationVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
