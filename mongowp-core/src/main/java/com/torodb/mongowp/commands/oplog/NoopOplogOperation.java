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
import com.torodb.mongowp.fields.DocField;
import com.torodb.mongowp.fields.StringField;
import com.torodb.mongowp.utils.BsonDocumentBuilder;

import javax.annotation.Nullable;

/**
 *
 */
public class NoopOplogOperation extends OplogOperation {

  private static final long serialVersionUID = 1L;

  private static final StringField NS_FIELD = new StringField("ns");
  private static final DocField MSG_FIELD = new DocField("o");
  @Nullable
  private final BsonDocument msg;

  public NoopOplogOperation(@Nullable BsonDocument msg, String database, OpTime optime, long h,
      OplogVersion version, boolean fromMigrate) {
    super(database, optime, h, version, fromMigrate);
    this.msg = msg;
  }

  @Override
  public OplogOperationType getType() {
    return OplogOperationType.NOOP;
  }

  @Override
  public BsonDocumentBuilder toDescriptiveBson() {
    BsonDocumentBuilder doc = super.toDescriptiveBson()
        .append(OP_FIELD, getType().getOplogName())
        .append(NS_FIELD, "");
    if (msg != null) {
      doc.append(MSG_FIELD, msg);
    }
    return doc;
  }

  public BsonDocument getMsg() {
    return msg;
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
    NoopOplogOperation other = (NoopOplogOperation) obj;
    return other.getMsg().equals(this.getMsg());
  }

  @Override
  public <R, A> R accept(OplogOperationVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
