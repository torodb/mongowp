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

import com.eightkdata.mongowp.bson.BsonDouble;
import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.BsonInt64;
import com.eightkdata.mongowp.bson.BsonNumber;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonDouble;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonInt32;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonInt64;

/**
 *
 */
public abstract class AbstractBsonNumber<V extends Number> extends AbstractBsonValue<V> implements BsonNumber<V> {

    @Override
    public BsonInt64 asInt64() {
        return PrimitiveBsonInt64.newInstance(longValue());
    }

    @Override
    public BsonInt32 asInt32() {
        return PrimitiveBsonInt32.newInstance(intValue());
    }

    @Override
    public BsonDouble asDouble() {
        return PrimitiveBsonDouble.newInstance(doubleValue());
    }

    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public BsonNumber asNumber() throws UnsupportedOperationException {
        return this;
    }
}
