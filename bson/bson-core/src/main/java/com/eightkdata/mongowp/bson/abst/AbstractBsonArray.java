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

package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.collect.Iterators;

/**
 *
 */
public abstract class AbstractBsonArray extends CachedHashAbstractBsonValue<BsonArray> implements BsonArray {

    @Override
    public Class<? extends BsonArray> getValueClass() {
        return this.getClass();
    }

    @Override
    public BsonType getType() {
        return BsonType.ARRAY;
    }

    @Override
    public BsonArray getValue() {
        return this;
    }

    @Override
    public BsonArray asArray() {
        return this;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BsonArray)) {
            return false;
        }
        BsonArray other = (BsonArray) obj;
        if (this.size() != other.size()){
            return false;
        }
        return Iterators.elementsEqual(this.iterator(), other.iterator());
    }

    @Override
    final int calculateHash() {
        return IntBaseHasher.hash(size());
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
