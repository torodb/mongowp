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
package com.torodb.mongowp.messages.utils;

import com.google.common.collect.FluentIterable;
import com.torodb.mongowp.annotations.Ethereal;
import com.torodb.mongowp.annotations.Material;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.utils.BsonDocumentReader;
import com.torodb.mongowp.bson.utils.BsonDocumentReader.AllocationType;

import java.util.Collections;

/**
 * This class is a factory that creates stream like iterables of type BsonDocument.
 *
 * The generated iterables can be configured to use different
 * {@linkplain AllocationType heap allocations}.
 *
 * @param <E>
 */
public abstract class IterableDocumentProvider<E extends BsonDocument> {

  private static final IterableDocumentProvider<?> EMPTY =
      new SimpleIterableDocumentProvider<>(Collections.<BsonDocument>emptyList());

  @Ethereal("my-own-context")
  public abstract FluentIterable<E> getIterable(BsonDocumentReader.AllocationType algorithm);

  @Material
  @SuppressWarnings("unchecked")
  public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of() {
    return (IterableDocumentProvider<E1>) EMPTY;
  }

  @Ethereal("argument-context")
  public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of(
      @Ethereal("whatever") E1 element) {
    return new SimpleIterableDocumentProvider<>(Collections.singleton(element));
  }

  @Ethereal("argument-context")
  public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of(
      @Ethereal("whatever") Iterable<E1> elements) {
    return new SimpleIterableDocumentProvider<>(elements);
  }
}
