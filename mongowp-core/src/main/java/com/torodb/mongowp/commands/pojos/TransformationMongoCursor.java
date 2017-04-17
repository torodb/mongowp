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
package com.torodb.mongowp.commands.pojos;

import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import com.torodb.mongowp.exceptions.MongoException;

import java.util.List;
import java.util.function.Function;

/**
 *
 */
public class TransformationMongoCursor<I, O> implements MongoCursor<O> {

  private final MongoCursor<I> innerCursor;
  private final Function<I, O> transformationFun;

  private TransformationMongoCursor(MongoCursor<I> innerCursor, Function<I, O> transformationFun) {
    this.innerCursor = innerCursor;
    this.transformationFun = transformationFun;
  }

  public static <I, O> TransformationMongoCursor<I, O> create(
      MongoCursor<I> innerCursor,
      Function<I, O> transformationFun) {
    return new TransformationMongoCursor<>(innerCursor, transformationFun);
  }

  @Override
  public String getDatabase() {
    return innerCursor.getDatabase();
  }

  @Override
  public String getCollection() {
    return innerCursor.getCollection();
  }

  @Override
  public long getId() {
    return innerCursor.getId();
  }

  @Override
  public void setMaxBatchSize(int newBatchSize) {
    innerCursor.setMaxBatchSize(newBatchSize);
  }

  @Override
  public int getMaxBatchSize() {
    return innerCursor.getMaxBatchSize();
  }

  @Override
  public boolean isTailable() {
    return innerCursor.isTailable();
  }

  @Override
  public Batch<O> tryFetchBatch() throws MongoException, DeadCursorException {
    Batch<I> innerBatch = innerCursor.tryFetchBatch();
    if (innerBatch == null) {
      return null;
    }
    return new TransformationBatch<>(innerBatch, transformationFun);
  }

  @Override
  public Batch<O> fetchBatch() throws MongoException, DeadCursorException {
    return new TransformationBatch<>(innerCursor.fetchBatch(), transformationFun);
  }

  @Override
  public O tryNext() {
    I next = innerCursor.tryNext();
    if (next == null) {
      return null;
    }
    return transformationFun.apply(next);
  }

  @Override
  public HostAndPort getServerAddress() {
    return innerCursor.getServerAddress();
  }

  @Override
  public boolean hasNext() {
    return innerCursor.hasNext();
  }

  @Override
  public O next() {
    return transformationFun.apply(innerCursor.next());
  }

  @Override
  public boolean isClosed() {
    return innerCursor.isClosed();
  }

  @Override
  public void close() {
    innerCursor.close();
  }

  private static class TransformationBatch<I, O> implements Batch<O> {

    private final Batch<I> innerBatch;
    private final Function<I, O> transformationFun;

    public TransformationBatch(Batch<I> innerBatch, Function<I, O> transformationFun) {
      this.innerBatch = innerBatch;
      this.transformationFun = transformationFun;
    }

    @Override
    public void remove() throws UnsupportedOperationException {
      innerBatch.remove();
    }

    @Override
    public O next() {
      return transformationFun.apply(innerBatch.next());
    }

    @Override
    public boolean hasNext() {
      return innerBatch.hasNext();
    }

    @Override
    public int getBatchSize() {
      return innerBatch.getBatchSize();
    }

    @Override
    public long getFetchTime() {
      return innerBatch.getFetchTime();
    }

    @Override
    public List<O> asList() {
      return Lists.transform(innerBatch.asList(), (i) -> transformationFun.apply(i));
    }

    @Override
    public void close() {
      innerBatch.close();;
    }

  }

}
