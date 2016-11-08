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

import com.eightkdata.mongowp.bson.BsonMax;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;

/**
 *
 */
public abstract class AbstractBsonMax extends AbstractBsonValue<BsonMax> implements BsonMax {

    @Override
    public Class<? extends BsonMax> getValueClass() {
        return this.getClass();
    }

    @Override
    public BsonMax getValue() {
        return this;
    }

    @Override
    public BsonType getType() {
        return BsonType.MAX;
    }

    @Override
    public int compareTo(BsonValue<?> o) {
        if (o == this) {
            return 0;
        }
        int diff = BsonTypeComparator.INSTANCE.compare(getType(), o.getType());
        if (diff != 0) {
            return diff;
        }

        assert o instanceof BsonMax;
        return 0;
    }

    @Override
    public final boolean equals(Object obj) {
        return this == obj || obj != null && obj instanceof BsonMax;
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
