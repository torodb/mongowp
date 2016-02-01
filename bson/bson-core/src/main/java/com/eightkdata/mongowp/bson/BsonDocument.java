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

import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

/**
 *
 */
public interface BsonDocument extends BsonValue<BsonDocument>, Iterable<Entry<?>>{

    @Nullable
    BsonValue<?> get(String key);

    boolean containsKey(String key);

    boolean isEmpty();

    int size();

    /**
     * @return the first entry of this document
     * @throws NoSuchElementException if it is {@linkplain #isEmpty() empty}
     */
    Entry<?> getFirstEntry() throws NoSuchElementException;

    @Override
    public UnmodifiableIterator<Entry<?>> iterator();

    /**
     * Two documents are equal if they contain the same entries in the same
     * order.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj);

    /**
     * The hashCode of a BsonDocument is calculated by calling
     * {@linkplain IntBaseHasher#hash(int) IntBaseHasher.hash(this.size())}.
     * @return 
     */
    @Override
    public int hashCode();

    public static interface Entry<V> extends Serializable {

        public String getKey();

        public BsonValue<V> getValue();

        /**
         * Two entries are equals if their keys and values are equal.
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o);

        /**
         * The hashCode of a entry is the hash of its key.
         * @return 
         */
        @Override
        public int hashCode();
    }
}
