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

import com.eightkdata.mongowp.bson.BsonRegex;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 */
public abstract class AbstractBsonRegex extends AbstractBsonValue<BsonRegex> implements BsonRegex {

    @Override
    public Class<? extends BsonRegex> getValueClass() {
        return this.getClass();
    }

    @Override
    public String getOptionsAsText() {
        Set<Options> options = getOptions();
        if (options.isEmpty()) {
            return "";
        }
        if (options.size() == 1) {
            return Character.toString(options.iterator().next().getCharId());
        }

        SortedSet<Options> sortedOptions = new TreeSet<>(Options.getLexicographicalComparator());
        sortedOptions.addAll(options);

        StringBuilder sb = new StringBuilder(6);
        for (Options option : sortedOptions) {
            sb.append(option.getCharId());
        }
        return sb.toString();
    }

    @Override
    public BsonRegex getValue() {
        return this;
    }

    @Override
    public BsonType getType() {
        return BsonType.REGEX;
    }

    @Override
    public BsonRegex asRegex() {
        return this;
    }

    @Override
    public boolean isRegex() {
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
        if (!(obj instanceof BsonRegex)) {
            return false;
        }
        BsonRegex other = (BsonRegex) obj;
        if (!this.getOptions().equals(other.getOptions())) {
            return false;
        }
        return this.getPattern().equals(other.getPattern());
    }

    @Override
    public final int hashCode() {
        return getPattern().hashCode();
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }
}
