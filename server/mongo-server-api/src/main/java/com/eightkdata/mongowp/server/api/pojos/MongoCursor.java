
package com.eightkdata.mongowp.server.api.pojos;

import com.eightkdata.mongowp.exceptions.MongoException;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public interface MongoCursor<E> extends Iterable<E> {

    public String getDatabase();

    public String getCollection();

    public long getId();

    public void setMaxBatchSize(int newBatchSize);

    public int getMaxBatchSize();

    public boolean isTailable();

    /**
     * A cursor is dead when it is not possible to fetch new batches.
     * @return 
     */
    public boolean isDead();

    @Nonnull
    public Batch<E> fetchBatch() throws MongoException, DeadCursorException;

    /**
     * Returns one element of the cursor and close it.
     * @return 
     * @throws com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoException
     */
    @Nullable
    public E getOne() throws MongoException, DeadCursorException;

    public void close();

    public static interface Batch<T> extends Iterator<T> {

        /**
         * Not supported.
         */
        @Override
        public void remove() throws UnsupportedOperationException;

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
         * @return
         */
        public List<? extends T> asList();

        public void close();
    }

    public static class MongoCursorIterator<E> extends UnmodifiableIterator<E> {
        private static final Logger LOGGER
                = LoggerFactory.getLogger(MongoCursorIterator.class);
        private final MongoCursor<E> cursor;
        private Batch<E> _batch;

        public MongoCursorIterator(MongoCursor<E> cursor) {
            this.cursor = cursor;
        }

        @Nullable
        private Batch<E> getLiveBatch() {
            if (_batch != null && _batch.hasNext()) {
                return _batch;
            }
            try {
                _batch = cursor.fetchBatch();
            } catch (MongoException ex) {
                LOGGER.warn("Error while retrieving more data form a cursor", ex);
            }
            if (_batch != null && _batch.hasNext()) {
                return _batch;
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            Batch<E> batch = getLiveBatch();
            return batch != null && batch.hasNext();
        }

        @Override
        public E next() {
            Batch<E> batch = getLiveBatch();
            if (batch == null) {
                throw new NoSuchElementException();
            }
            return batch.next();
        }
    }

    public static class DeadCursorException extends RuntimeException {
        private static final long serialVersionUID = 1L;

    }


}
