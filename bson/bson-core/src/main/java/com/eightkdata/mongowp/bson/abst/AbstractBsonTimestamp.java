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

import com.eightkdata.mongowp.bson.BsonTimestamp;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;

/**
 *
 */
public abstract class AbstractBsonTimestamp extends AbstractBsonValue<BsonTimestamp> implements BsonTimestamp {

    @Override
    public Class<? extends BsonTimestamp> getValueClass() {
        return this.getClass();
    }

    @Override
    public BsonTimestamp getValue() {
        return this;
    }

    @Override
    public BsonType getType() {
        return BsonType.TIMESTAMP;
    }

    @Override
    public BsonTimestamp asTimestamp() {
        return this;
    }

    @Override
    public boolean isTimestamp() {
        return true;
    }

    @Override
    public int compareTo(BsonTimestamp o) {
        int diff = this.getSecondsSinceEpoch() - o.getSecondsSinceEpoch();
        if (diff != 0) {
            return diff;
        }
        return this.getOrdinal() - o.getOrdinal();
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BsonTimestamp)) {
            return false;
        }
        BsonTimestamp other = (BsonTimestamp) obj;
        if (this.getOrdinal() != other.getOrdinal()) {
            return false;
        }
        return this.getSecondsSinceEpoch() == other.getSecondsSinceEpoch();
    }

    @Override
    public final int hashCode() {
        return getSecondsSinceEpoch() << 4 | (getOrdinal() & 0xF);
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
