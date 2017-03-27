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
package com.torodb.mongowp.bson.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import com.torodb.mongowp.bson.BsonDocument.Entry;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.abst.AbstractBsonDocument;

import java.util.Collections;

/**
 *
 */
public final class EmptyBsonDocument extends AbstractBsonDocument {

  private static final long serialVersionUID = -8555042328029670425L;

  private EmptyBsonDocument() {
  }

  @Override
  public BsonValue<?> get(String key) {
    return null;
  }

  @Override
  public boolean containsKey(String key) {
    return false;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public Entry<?> getEntry(String key) {
    return null;
  }

  @Override
  public UnmodifiableIterator<Entry<?>> iterator() {
    return Iterators.unmodifiableIterator(Collections.<Entry<?>>emptyIterator());
  }

  public static EmptyBsonDocument getInstance() {
    return EmptyBsonDocumentHolder.INSTANCE;
  }

  private static class EmptyBsonDocumentHolder {

    private static final EmptyBsonDocument INSTANCE = new EmptyBsonDocument();
  }

  // @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
  private Object readResolve() {
    return EmptyBsonDocument.getInstance();
  }
}
