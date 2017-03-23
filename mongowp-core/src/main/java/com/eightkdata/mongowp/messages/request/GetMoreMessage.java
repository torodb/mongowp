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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 */
public class GetMoreMessage extends AbstractRequestMessage {

  public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_GET_MORE;

  @Nonnull
  private final String database;
  @Nonnull
  private final String collection;
  @Nonnegative
  private final int numberToReturn;
  @Nonnegative
  private final long cursorId;

  public GetMoreMessage(
      @Nonnull RequestBaseMessage requestBaseMessage,
      @Nonnull String database,
      @Nonnull String collection,
      int numberToReturn,
      long cursorId
  ) {
    super(requestBaseMessage, EmptyBsonContext.getInstance());
    this.database = database;
    this.collection = collection;
    this.numberToReturn = numberToReturn;
    this.cursorId = cursorId;
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

  public int getNumberToReturn() {
    return numberToReturn;
  }

  public long getCursorId() {
    return cursorId;
  }

  @Override
  public void close() {
  }

  @Override
  public String toString() {
    return "GetMoreMessage{" + super.toString() + ", database='" + database + '\''
        + ", collection='" + collection + '\'' + ", numberToReturn=" + numberToReturn
        + ", cursorId=" + cursorId + '}';
  }

}
