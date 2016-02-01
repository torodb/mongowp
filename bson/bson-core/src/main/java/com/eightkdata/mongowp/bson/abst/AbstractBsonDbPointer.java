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

import com.eightkdata.mongowp.bson.BsonDbPointer;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
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
    public BsonType getType() {
        return BsonType.DB_POINTER;
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
