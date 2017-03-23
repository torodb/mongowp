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
package com.eightkdata.mongowp.messages.utils;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.google.common.collect.FluentIterable;

/**
 *
 * @param <E>
 */
class SimpleIterableDocumentProvider<E extends BsonDocument> extends IterableDocumentProvider<E> {

  private final FluentIterable<E> documents;

  SimpleIterableDocumentProvider(Iterable<E> documents) {
    this.documents = FluentIterable.from(documents);
  }

  @Override
  public FluentIterable<E> getIterable(AllocationType algorithm) {
    return documents;
  }
}
