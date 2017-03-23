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
package com.eightkdata.mongowp.messages.request;

import com.eightkdata.mongowp.annotations.Ethereal;
import com.eightkdata.mongowp.bson.BsonDocument;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class UpdateMessage extends AbstractRequestMessage {

  public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_UPDATE;

  @Nonnull
  private final String database;
  @Nonnull
  private final String collection;
  @Ethereal("getDataContext")
  @Nonnull
  private final BsonDocument selector;
  @Ethereal("getDataContext")
  @Nonnull
  private final BsonDocument update;
  private final boolean upsert;
  private final boolean multiUpdate;

  public UpdateMessage(
      @Nonnull RequestBaseMessage requestBaseMessage,
      @Nonnull BsonContext dataContext,
      @Nonnull String database,
      @Nonnull String collection,
      @Nonnull @Ethereal("dataContext") BsonDocument selector,
      @Nonnull @Ethereal("dataContext") BsonDocument update,
      boolean upsert,
      boolean multiUpdate) {
    super(requestBaseMessage, dataContext);
    this.database = database;
    this.collection = collection;
    this.selector = selector;
    this.update = update;
    this.upsert = upsert;
    this.multiUpdate = multiUpdate;
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
  public BsonDocument getSelector() {
    return selector;
  }

  @Nonnull
  @Ethereal("this")
  public BsonDocument getUpdate() {
    return update;
  }

  public boolean isUpsert() {
    return upsert;
  }

  public boolean isMultiUpdate() {
    return multiUpdate;
  }

  @Override
  public void close() {
  }

  @Override
  public String toString() {
    //TODO: This must be changed to preserve privacy on logs
    return "UpdateMessage{" + super.toString() + ", database='" + database + '\'' + ", collection='"
        + collection + '\'' + ", selector=" + (getDataContext().isValid() ? selector :
        "<not available>") + ", update=" + (getDataContext().isValid() ? update : "<not avaiable>")
        + '}';
  }
}
