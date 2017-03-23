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
package com.torodb.mongowp.server.api.pojos;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import com.torodb.mongowp.exceptions.MongoException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 */
public class IteratorMongoCursor<E> implements MongoCursor<E> {

  private final String database;
  private final String collection;
  private final long id;
  private final HostAndPort remoteAddress;
  private final Iterator<E> iterator;
  private boolean closed = false;
  private int batchSize = 1000;

  public IteratorMongoCursor(String database, String collection, long id, HostAndPort remoteAddress,
      Iterator<E> iterator) {
    this.database = database;
    this.collection = collection;
    this.id = id;
    this.remoteAddress = remoteAddress;
    this.iterator = iterator;
  }

  @Override
  public String getDatabase() {
    return database;
  }

  @Override
  public String getCollection() {
    return collection;
  }

  @Override
  public Batch<E> fetchBatch() throws MongoException, DeadCursorException {
    return new CollectionBatch<>(Lists.newArrayList(Iterators.limit(iterator, batchSize)), System
        .currentTimeMillis());
  }

  @Override
  public Batch<E> tryFetchBatch() throws MongoException, DeadCursorException {
    if (!iterator.hasNext()) {
      return null;
    }
    return fetchBatch();
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public void setMaxBatchSize(int newBatchSize) {
    batchSize = newBatchSize;
  }

  @Override
  public int getMaxBatchSize() {
    return batchSize;
  }

  @Override
  public boolean isTailable() {
    return false;
  }

  @Override
  public E next() {
    return iterator.next();
  }

  @Override
  public E tryNext() {
    if (!iterator.hasNext()) {
      throw new NoSuchElementException();
    }
    return iterator.next();
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public HostAndPort getServerAddress() {
    return remoteAddress;
  }

  @Override
  public void close() {
    iterator.forEachRemaining((e) -> {
    });
    closed = true;
  }

  @Override
  public boolean isClosed() {
    return closed;
  }

}
