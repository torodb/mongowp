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
 * along with bson. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.abst.AbstractBsonArray;
import com.eightkdata.mongowp.bson.annotations.NotMutable;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class ListBsonArray extends AbstractBsonArray {

    private static final long serialVersionUID = 6400879352530123227L;

    private final List<BsonValue<?>> list;

    public ListBsonArray(@NotMutable List<BsonValue<?>> list) {
        this.list = list;
    }

    @Override
    public BsonValue<?> get(int index) {
        return list.get(index);
    }

    @Override
    public boolean contains(BsonValue<?> element) {
        return list.contains(element);
    }

    @Override
    public int size() {
        return list.size();
    }

    public boolean containsAll(Iterable<BsonValue<?>> iterable) {
        for (BsonValue<?> bsonValue : iterable) {
            if (!contains(bsonValue)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(Collection<BsonValue<?>> iterable) {
        return list.containsAll(iterable);
    }

    @Override
    public UnmodifiableIterator<BsonValue<?>> iterator() {
        return Iterators.unmodifiableIterator(list.listIterator());
    }
}
