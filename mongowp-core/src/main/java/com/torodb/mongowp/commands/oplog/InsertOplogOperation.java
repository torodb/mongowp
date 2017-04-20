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
import com.torodb.mongowp.fields.DocField;
import com.torodb.mongowp.utils.BsonDocumentBuilder;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class InsertOplogOperation extends CollectionOplogOperation {

  private static final long serialVersionUID = 1L;

  private static final DocField DOC_TO_INSERT_FIELD = new DocField("o");

  private final BsonDocument docToInsert;

  public InsertOplogOperation(
      BsonDocument docToInsert,
      String database,
      String collection,
      OpTime optime,
      long h,
      OplogVersion version,
      boolean fromMigrate) {
    super(database, collection, optime, h, version, fromMigrate);
    this.docToInsert = docToInsert;
  }

  public BsonDocument getDocToInsert() {
    return docToInsert;
  }

  @Override
  public OplogOperationType getType() {
    return OplogOperationType.INSERT;
  }

  @Override
  public BsonValue<?> getDocId() {
    return getDocToInsert().get("_id");
  }

  @Override
  public BsonDocumentBuilder toDescriptiveBson() {
    return super.toDescriptiveBson()
        .append(OP_FIELD, getType().getOplogName())
        .append(DOC_TO_INSERT_FIELD, docToInsert);
  }

  @Override
  public int hashCode() {
    //This is here to explicity say we know this hashCode is compatible with equals
    //to avoid static check warnings
    return super.hashCode();
  }

  @Override
  @SuppressFBWarnings("BC_EQUALS_METHOD_SHOULD_WORK_FOR_ALL_OBJECTS")
  public boolean equals(Object obj) {
    if (!generalEquals(obj)) {
      return false;
    }
    InsertOplogOperation other = (InsertOplogOperation) obj;
    return other.getDocToInsert().equals(this.getDocToInsert());
  }

  @Override
  public <R, A> R accept(OplogOperationVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
