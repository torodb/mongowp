/*
 * MongoWP - Mongo Server: API
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
