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

import com.eightkdata.mongowp.exceptions.MongoException;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
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

    public IteratorMongoCursor(String database, String collection, long id, HostAndPort remoteAddress, Iterator<E> iterator) {
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
        return new CollectionBatch<>(Lists.newArrayList(Iterators.limit(iterator, batchSize)), System.currentTimeMillis());
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
        iterator.forEachRemaining((e) -> {});
        closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

}
