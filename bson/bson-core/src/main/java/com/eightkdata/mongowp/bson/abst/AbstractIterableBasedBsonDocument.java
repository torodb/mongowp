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

import com.eightkdata.mongowp.bson.BsonValue;
import com.google.common.collect.Iterables;

/**
 *
 */
public abstract class AbstractIterableBasedBsonDocument extends AbstractBsonDocument {

    private int cachedSize = -1;

    @Override
    public BsonValue<?> get(String key) {
        for (Entry<?> entry : this) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        for (Entry<?> entry : this) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        if (cachedSize < -1) {
            cachedSize = Iterables.size(this);
        }
        return cachedSize;
    }

}
