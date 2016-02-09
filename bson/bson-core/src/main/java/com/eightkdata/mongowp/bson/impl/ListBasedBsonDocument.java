/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bson-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.abst.AbstractIterableBasedBsonDocument;
import com.eightkdata.mongowp.bson.annotations.NotMutable;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 */
public class ListBasedBsonDocument extends AbstractIterableBasedBsonDocument {

    private static final long serialVersionUID = -6475758693810996556L;

    private final List<BsonDocument.Entry<?>> entries;

    public ListBasedBsonDocument(@NotMutable List<Entry<?>> entries) {
        this.entries = entries;
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public Entry<?> getFirstEntry() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return entries.get(0);
    }

    @Override
    public UnmodifiableIterator<Entry<?>> iterator() {
        return Iterators.unmodifiableIterator(entries.iterator());
    }

}
