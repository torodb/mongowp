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

package com.eightkdata.mongowp.bson;

import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import javax.annotation.Nonnull;

/**
 *
 */
public interface BsonArray extends BsonValue<BsonArray>, Iterable<BsonValue<?>> {

    @Nonnull
    BsonValue<?> get(int index) throws IndexOutOfBoundsException;

    boolean contains(BsonValue<?> element);

    boolean isEmpty();

    int size();

    /**
     * @return {@link IntBaseHasher#hash(int) IntBaseHasher.hash(this.size())}
     */
    @Override
    public int hashCode();

    /**
     * Two BsonArray values are equal if their contains equal elements in the
     * same position.
     *
     * An easy way to implement that is to delegate on
     * {@link Iterators#elementsEqual(java.lang.Iterator, java.lang.Iterator) }
     *
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj);

    @Override
    UnmodifiableIterator<BsonValue<?>> iterator();
}
