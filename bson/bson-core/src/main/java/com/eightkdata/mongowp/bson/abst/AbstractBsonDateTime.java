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

import com.eightkdata.mongowp.bson.BsonDateTime;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;
import java.time.Instant;

/**
 *
 */
public abstract class AbstractBsonDateTime extends AbstractBsonValue<Instant> implements BsonDateTime {

    @Override
    public Class<? extends Instant> getValueClass() {
        return Instant.class;
    }

    @Override
    public BsonType getType() {
        return BsonType.DATETIME;
    }

    @Override
    public BsonDateTime asDateTime() {
        return this;
    }

    @Override
    public boolean isDateTime() {
        return true;
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

        assert o instanceof BsonDateTime;
        BsonDateTime other = o.asDateTime();
        return this.getValue().compareTo(other.getValue());
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BsonDateTime)) {
            return false;
        }
        return this.getMillisFromUnix() == ((BsonDateTime) obj).getMillisFromUnix();
    }

    @Override
    public final int hashCode() {
        return getValue().hashCode();
    }

    @Override
    public String toString() {
        return "{$date: " + getValue()+ "}";
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }
}
