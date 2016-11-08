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

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

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
    public int compareTo(BsonValue<?> o) {
        if (o == this) {
            return 0;
        }
        int diff = BsonTypeComparator.INSTANCE.compare(getType(), o.getType());
        if (diff != 0) {
            return 0;
        }

        assert o instanceof BsonArray;
        BsonArray other = o.asArray();

        diff = this.size() - other.size();
        if (diff != 0) {
            return diff;
        }

        UnmodifiableIterator<BsonValue<?>> myIt = this.iterator();
        UnmodifiableIterator<BsonValue<?>> otherIt = other.iterator();

        while (myIt.hasNext() && otherIt.hasNext()) {
            diff = myIt.next().compareTo(otherIt.next());
            if (diff != 0) {
                return diff;
            }
        }
        assert !myIt.hasNext() : "the other array has more entries than ourself!";
        assert !otherIt.hasNext() : "the other array has less entries than ourself!";

        return 0;
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
    public String toString() {
        StringBuilder sb = new StringBuilder(size() * 20);
        sb.append('[');

        for (BsonValue<?> value : this) {
            sb.append(value);
            sb.append(", ");
        }
        if (!isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append(']');

        return sb.toString();
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
