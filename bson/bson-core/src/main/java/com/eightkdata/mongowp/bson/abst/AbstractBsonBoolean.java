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

import com.eightkdata.mongowp.bson.BsonBoolean;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.google.common.primitives.Booleans;


/**
 *
 */
public abstract class AbstractBsonBoolean extends AbstractBsonValue<Boolean> implements BsonBoolean {

    @Override
    public Class<? extends Boolean> getValueClass() {
        return Boolean.class;
    }

    @Override
    public Boolean getValue() {
        return getPrimitiveValue();
    }

    @Override
    public BsonType getType() {
        return BsonType.BOOLEAN;
    }

    @Override
    public BsonBoolean asBoolean() {
        return this;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    @Override
    public final boolean equals(Object obj) {
        return this == obj
                || obj != null && obj instanceof BsonBoolean
                && ((BsonBoolean) obj).getPrimitiveValue() == getPrimitiveValue();
    }

    @Override
    public final int hashCode() {
        return Booleans.hashCode(getPrimitiveValue());
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
