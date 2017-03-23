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
package com.torodb.mongowp.exceptions;

import com.torodb.mongowp.ErrorCode;

/**
 *
 */
public class NamespaceNotFoundException extends MongoException {

  private static final long serialVersionUID = 1L;

  private final String database;
  private final String collection;

  public NamespaceNotFoundException(String database, String collection) {
    super(ErrorCode.NAMESPACE_NOT_FOUND, database + '.' + collection);
    this.database = database;
    this.collection = collection;
  }

  public NamespaceNotFoundException(String database, String collection, Throwable cause) {
    super(cause, ErrorCode.NAMESPACE_NOT_FOUND, database + '.' + collection);
    this.database = database;
    this.collection = collection;
  }

  public NamespaceNotFoundException(String database, String collection, String customMessage) {
    super(customMessage, ErrorCode.NAMESPACE_NOT_FOUND);
    this.database = database;
    this.collection = collection;
  }

  public NamespaceNotFoundException(String database, String collection, String customMessage,
      Throwable cause) {
    super(customMessage, cause, ErrorCode.NAMESPACE_NOT_FOUND);
    this.database = database;
    this.collection = collection;
  }

  public String getDatabase() {
    return database;
  }

  public String getCollection() {
    return collection;
  }

}
