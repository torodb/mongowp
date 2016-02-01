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

import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonUndefined;
import com.eightkdata.mongowp.bson.BsonValueVisitor;

/**
 *
 */
public abstract class AbstractBsonUndefined extends AbstractBsonValue<BsonUndefined> implements BsonUndefined {

    @Override
    public Class<? extends BsonUndefined> getValueClass() {
        return this.getClass();
    }

    @Override
    public BsonUndefined getValue() {
        return this;
    }

    @Override
    public BsonType getType() {
        return BsonType.UNDEFINED;
    }

    @Override
    public BsonUndefined asUndefined() {
        return this;
    }

    @Override
    public boolean isUndefined() {
        return true;
    }

    @Override
    public final boolean equals(Object obj) {
        return this == obj || obj != null && obj instanceof BsonUndefined;
    }

    @Override
    public final int hashCode() {
        return HASH;
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
