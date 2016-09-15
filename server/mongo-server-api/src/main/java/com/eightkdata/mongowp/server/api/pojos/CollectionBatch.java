
package com.eightkdata.mongowp.server.api.pojos;

import com.eightkdata.mongowp.bson.annotations.NotMutable;
import com.eightkdata.mongowp.server.api.pojos.MongoCursor.Batch;
import com.google.common.base.Preconditions;
import java.util.*;

/**
 *
 */
public class CollectionBatch<T> implements Batch<T> {
    private final List<T> list;
    private final Iterator<? extends T> it;
    private final int batchSize;
    private final long fetchTime;
    private boolean closed = false;

    public CollectionBatch(@NotMutable Collection<? extends T> collection, long fetchTime) {
        if (collection instanceof List) {
            this.list = Collections.unmodifiableList((List<? extends T>) collection);
        } else {
            this.list = Collections.unmodifiableList(new ArrayList<>(collection));
        }
        this.it = collection.iterator();
        this.batchSize = collection.size();
        this.fetchTime = fetchTime;
    }

    @Override
    public T next() {
        Preconditions.checkState(!closed, "This batch has been closed");
        return it.next();
    }

    @Override
    public boolean hasNext() {
        Preconditions.checkState(!closed, "This batch has been closed");
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
    public List<T> asList() {
        Preconditions.checkState(!closed, "This batch has been closed");
        return list;
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
        }
    }

}
