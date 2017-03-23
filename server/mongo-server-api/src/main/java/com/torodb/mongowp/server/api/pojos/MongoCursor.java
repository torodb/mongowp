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

import com.google.common.collect.UnmodifiableIterator;
import com.google.common.net.HostAndPort;
import com.torodb.mongowp.exceptions.MongoException;
import com.torodb.mongowp.server.api.MongoRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public interface MongoCursor<E> extends Iterator<E> {

  public String getDatabase();

  public String getCollection();

  @Nonnull
  public Batch<E> fetchBatch() throws MongoException, DeadCursorException, MongoRuntimeException;

  /**
   * Like {@link #fetchBatch() } but do not block when there are no more elements on the remote
   * side.
   *
   *
   * @return
   * @throws MongoException
   * @throws com.eightkdata.mongowp.server.api.pojos.MongoCursor.DeadCursorException
   * @see #tryNext()
   */
  public Batch<E> tryFetchBatch() throws MongoException, DeadCursorException, MongoRuntimeException;

  public long getId();

  public void setMaxBatchSize(int newBatchSize);

  public int getMaxBatchSize();

  public boolean isTailable();

  @Nonnull
  @Override
  public E next() throws MongoRuntimeException;

  /**
   * Like {@link #next() } but do not block when there are not more elements on the remote side,
   * returning null instead.
   *
   * The main use of this method is to not wait indefinitely on a tailable cursor when there are no
   * new elements on the remote side. When the cursor is not tailable and there are no more elements
   * on the remote side, it could return null or a runtime exception.
   *
   * @return
   */
  @Nullable
  public E tryNext() throws MongoRuntimeException;

  /**
   * Returns {@code true} if the cursor has more elements.
   *
   * {@inheritDoc }
   *
   * This method could block until more elements are fetch from the remote server.
   *
   * @return
   */
  @Override
  public boolean hasNext() throws MongoRuntimeException;

  HostAndPort getServerAddress();

  public void close();

  public boolean isClosed();

  public static interface Batch<T> extends Iterator<T> {

    /**
     * Not supported.
     */
    @Override
    public default void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException("remove");
    }

    @Override
    public T next();

    @Override
    public boolean hasNext();

    public int getBatchSize();

    /**
     * @return the milliseconds since Java epoch when this batch has been fetch
     * @see System#currentTimeMillis()
     */
    public long getFetchTime();

    /**
     * Return a list that contains all elements of this batch and consumes it.
     *
     * @return
     */
    public List<T> asList();

    public void close();
  }

  public static class MongoCursorIterator<E> extends UnmodifiableIterator<E> {

    private static final Logger LOGGER = LogManager.getLogger(MongoCursorIterator.class);
    private final MongoCursor<E> cursor;
    private Batch<E> batch;

    public MongoCursorIterator(MongoCursor<E> cursor) {
      this.cursor = cursor;
    }

    @Nullable
    private Batch<E> getLiveBatch() {
      if (batch != null && batch.hasNext()) {
        return batch;
      }
      try {
        batch = cursor.fetchBatch();
      } catch (MongoException ex) {
        LOGGER.warn("Error while retrieving more data form a cursor", ex);
      }
      if (batch != null && batch.hasNext()) {
        return batch;
      }
      return null;
    }

    @Override
    public boolean hasNext() {
      Batch<E> liveBatch = getLiveBatch();
      return liveBatch != null && liveBatch.hasNext();
    }

    @Override
    public E next() {
      Batch<E> liveBatch = getLiveBatch();
      if (liveBatch == null) {
        throw new NoSuchElementException();
      }
      return liveBatch.next();
    }
  }

  public static class DeadCursorException extends MongoRuntimeException {

    private static final long serialVersionUID = 848130492319548920L;

  }

}
