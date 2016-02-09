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

import com.eightkdata.mongowp.bson.BsonBinary;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;

/**
 *
 */
public abstract class AbstractBsonBinary extends CachedHashAbstractBsonValue<BsonBinary> implements BsonBinary {

    @Override
    public Class<? extends BsonBinary> getValueClass() {
        return this.getClass();
    }

    @Override
    public BsonBinary getValue() {
        return this;
    }

    @Override
    public BsonType getType() {
        return BsonType.BINARY;
    }

    @Override
    public BsonBinary asBinary() {
        return this;
    }

    @Override
    public boolean isBinary() {
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
        if (!(obj instanceof BsonBinary)) {
            return false;
        }
        BsonBinary other = (BsonBinary) obj;
        if (this.getSubtype() != other.getSubtype()) {
            return false;
        }
        return this.getByteSource().contentEquals(other.getByteSource());
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
