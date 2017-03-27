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
package com.torodb.mongowp.messages.request;

import com.torodb.mongowp.annotations.Ethereal;
import com.torodb.mongowp.bson.BsonDocument;

import javax.annotation.Nonnull;

public class DeleteMessage extends AbstractRequestMessage {

  public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_DELETE;

  @Nonnull
  private final String database;
  @Nonnull
  private final String collection;
  @Nonnull
  private final BsonDocument document;
  private final boolean singleRemove;

  public DeleteMessage(
      @Nonnull RequestBaseMessage requestBaseMessage,
      @Nonnull BsonContext dataContext,
      @Nonnull String database,
      @Nonnull String collection,
      @Nonnull @Ethereal("dataContext") BsonDocument document,
      boolean singleRemove) {
    super(requestBaseMessage, dataContext);
    this.database = database;
    this.collection = collection;
    this.document = document;
    this.singleRemove = singleRemove;
  }

  @Override
  public RequestOpCode getOpCode() {
    return REQUEST_OP_CODE;
  }

  @Nonnull
  public String getDatabase() {
    return database;
  }

  @Nonnull
  public String getCollection() {
    return collection;
  }

  @Nonnull
  @Ethereal("this")
  public BsonDocument getDocument() {
    return document;
  }

  public boolean isSingleRemove() {
    return singleRemove;
  }

  @Override
  public String toString() {
    //TODO: This must be changed to preserve privacy on logs
    return "DeleteMessage{" + super.toString() + ", database='" + database + '\'' + ", collection='"
        + collection + '\'' + ", document=" + (getDataContext().isValid() ? document :
        "<not available>") + '}';
  }
}
