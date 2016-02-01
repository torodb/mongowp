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

package com.eightkdata.mongowp.bson.abst;

/**
 *
 */
abstract class CachedHashAbstractBsonValue<V> extends AbstractBsonValue<V> {

    int hash = 0;

    /**
     * Calculates the hash of this object.
     * 
     * The hash must be different than 0.
     * @return 
     */
    abstract int calculateHash();

    @Override
    abstract public boolean equals(Object obj);

    @Override
    public final int hashCode() {
        if (hash == 0) {
            hash = calculateHash();
            assert hash != 0;
        }
        return hash;
    }

}
