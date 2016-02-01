
package com.eightkdata.mongowp.server.api.pojos;

import com.eightkdata.mongowp.server.api.pojos.MongoCursor.Batch;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class SimpleBatch<T> implements Batch<T> {
    private final Iterator<? extends T> it;
    private final int batchSize;
    private final long fetchTime;

    public SimpleBatch(Collection<? extends T> collection, long fetchTime) {
        this.it = collection.iterator();
        batchSize = collection.size();
        this.fetchTime = fetchTime;
    }

    public SimpleBatch(Iterator<T> it, int batchSize, long fetchTime) {
        this.it = it;
        this.batchSize = batchSize;
        this.fetchTime = fetchTime;
    }

    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public T next() {
        return it.next();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public long getFetchTime() {
        return fetchTime;
    }

    @Override
    public List<? extends T> asList() {
        return Lists.newArrayList(it);
    }

    @Override
    public void close() {
    }

}
