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

import com.eightkdata.mongowp.bson.BsonDbPointer;
import com.eightkdata.mongowp.bson.BsonJavaScript;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;
import java.util.Objects;

/**
 *
 */
public abstract class AbstractBsonDbPointer extends AbstractBsonValue<BsonDbPointer> implements BsonDbPointer {

    @Override
    public Class<? extends BsonDbPointer> getValueClass() {
        return this.getClass();
    }

    @Override
    public BsonDbPointer getValue() {
        return this;
    }

    @Override
    public BsonDbPointer asDbPointer() {
        return this;
    }

    @Override
    public boolean isDbPointer() {
        return true;
    }

    @Override
    public BsonType getType() {
        return BsonType.DB_POINTER;
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
        
        if (o.isObjectId()) {
        	return 1;
        }

        assert o instanceof BsonDbPointer;
        BsonDbPointer other = (BsonDbPointer) o;
        //TODO: Check how MongoDB compares pointers!

        diff = this.getNamespace().compareTo(other.getNamespace());
        if (diff != 0) {
            return diff;
        }
        return this.getId().compareTo(other.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BsonDbPointer)) {
            return false;
        }
        BsonDbPointer other = (BsonDbPointer) obj;
        if (!Objects.equals(this.getNamespace(), other.getNamespace())) {
            return false;
        }
        return this.getId().equals(other.getId());
    }

    @Override
    public final int hashCode() {
        return getId().hashCode();
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
