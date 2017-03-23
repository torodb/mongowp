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
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.bson.BsonDocument;

import javax.annotation.Nullable;

/**
 *
 */
public class NoMatchingDocumentException extends MongoException {

  private static final long serialVersionUID = -3667547491696671346L;

  private final String database;
  private final String collection;
  private final BsonDocument query;

  public NoMatchingDocumentException(@Nullable String database, @Nullable String collection,
      @Nullable BsonDocument query) {
    super(ErrorCode.NO_MATCHING_DOCUMENT, database, collection, query);
    this.database = database;
    this.collection = collection;
    this.query = query;
  }

  public NoMatchingDocumentException(String customMessage, @Nullable String database,
      @Nullable String collection, @Nullable BsonDocument query) {
    super(customMessage, ErrorCode.NO_MATCHING_DOCUMENT);
    this.database = database;
    this.collection = collection;
    this.query = query;
  }

  @Nullable
  public String getDatabase() {
    return database;
  }

  @Nullable
  public String getCollection() {
    return collection;
  }

  @Nullable
  public BsonDocument getQuery() {
    return query;
  }
}
