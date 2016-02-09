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

import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.abst.AbstractBsonDocument;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

/**
 *
 */
public class SingleEntryBsonDocument extends AbstractBsonDocument {

    private static final long serialVersionUID = -2025309751059819455L;

    private final SimpleEntry<?> entry;

    public SingleEntryBsonDocument(String key, BsonValue<?> value) {
        entry =  new SimpleEntry<>(key, value);
    }

    @Override
    public BsonValue<?> get(String key) {
        if (key.equals(entry.getKey())) {
            return entry.getValue();
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return key.equals(entry.getKey());
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public UnmodifiableIterator<Entry<?>> iterator() {
        return Iterators.<Entry<?>>singletonIterator(entry);
    }

}
