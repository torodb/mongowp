
package com.eightkdata.mongowp.server.api.pojos;

import com.eightkdata.mongowp.exceptions.MongoException;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.net.HostAndPort;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public interface MongoCursor<E> extends Iterator<E> {

    public String getDatabase();

    public String getCollection();

    @Nonnull
    public Batch<E> fetchBatch() throws MongoException, DeadCursorException;

    public long getId();

    public void setMaxBatchSize(int newBatchSize);

    public int getMaxBatchSize();

    public boolean isTailable();

    /**
     * Like {@link #next() } but returns null when there should be more elements on the remote side
     * but their are not fetch yet.
     *
     * It also returns null when this cursor is tailable and it is waiting for more documents.
     * @return
     */
    @Nullable
    public E tryNext();

    @Nonnull
    @Override
    public E next();

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
    public boolean hasNext();

    HostAndPort getServerAddress();

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
        private static final Logger LOGGER = LogManager.getLogger(MongoCursorIterator.class);
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
