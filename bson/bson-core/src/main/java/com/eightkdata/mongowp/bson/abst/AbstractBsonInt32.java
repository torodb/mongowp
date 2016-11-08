/*
 * MongoWP - MongoWP: Bson
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;

/**
 *
 */
public abstract class AbstractBsonInt32 extends AbstractBsonNumber<Integer> implements BsonInt32 {

    @Override
    public Class<? extends Integer> getValueClass() {
        return Integer.class;
    }

    @Override
    public BsonType getType() {
        return BsonType.INT32;
    }

    @Override
    public long longValue() {
        return intValue();
    }

    @Override
    public double doubleValue() {
        return intValue();
    }

    @Override
    public BsonInt32 asInt32() {
        return this;
    }

    @Override
    public boolean isInt32() {
        return true;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BsonInt32)) {
            return false;
        }
        return intValue() == ((BsonInt32) obj).intValue();
    }

    @Override
    public final int hashCode() {
        return intValue();
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
