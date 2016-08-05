
package com.eightkdata.mongowp.server.api.pojos;

import com.eightkdata.mongowp.exceptions.MongoException;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import java.util.Iterator;
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

    public static <I,O> TransformationMongoCursor<I, O> create(
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
        public List<? extends O> asList() {
            return Lists.transform(innerBatch.asList(), (i) -> transformationFun.apply(i));
        }

        @Override
        public void close() {
            innerBatch.close();;
        }

    }

}
