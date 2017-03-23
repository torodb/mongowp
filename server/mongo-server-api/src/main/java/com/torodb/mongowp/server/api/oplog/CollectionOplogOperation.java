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
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.fields.StringField;
import com.torodb.mongowp.utils.BsonDocumentBuilder;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class CollectionOplogOperation extends OplogOperation {

  private static final long serialVersionUID = 1L;

  private static final StringField NS_FIELD = new StringField("ns");
  private final String collection;

  public CollectionOplogOperation(
      String database,
      String collection,
      OpTime optime,
      long h,
      OplogVersion version,
      boolean fromMigrate) {
    super(database, optime, h, version, fromMigrate);
    this.collection = collection;
  }

  public String getCollection() {
    return collection;
  }

  @Override
  public BsonDocumentBuilder toDescriptiveBson() {
    BsonDocumentBuilder result = super.toDescriptiveBson();
    result.append(NS_FIELD, getDatabase() + '.' + collection);
    return result;
  }

  @Nullable
  public abstract BsonValue<?> getDocId();

}
