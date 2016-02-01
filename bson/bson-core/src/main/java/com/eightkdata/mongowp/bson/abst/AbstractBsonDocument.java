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

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 *
 */
public abstract class AbstractBsonDocument extends CachedHashAbstractBsonValue<BsonDocument> implements BsonDocument {

    @Override
    public Class<? extends BsonDocument> getValueClass() {
        return this.getClass();
    }

    @Override
    public BsonDocument getValue() {
        return this;
    }

    @Override
    public BsonDocument asDocument() {
        return this;
    }

    @Override
    public Entry<?> getFirstEntry() throws NoSuchElementException {
        return iterator().next();
    }

    @Override
    public boolean isDocument() {
        return true;
    }

    @Override
    public BsonType getType() {
        return BsonType.DOCUMENT;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BsonDocument)) {
            return false;
        }
        return Iterators.elementsEqual(this.iterator(), ((BsonDocument) obj).iterator());
    }

    @Override
    int calculateHash() {
        return IntBaseHasher.hash(size());
    }

    @Override
    public <Result, Arg> Result accept(BsonValueVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

    public static class SimpleEntry<V> implements Entry<V> {

        private static final long serialVersionUID = 5338375192630666767L;

        private final String key;
        private final BsonValue<V> value;

        public SimpleEntry(String key, BsonValue<V> value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public BsonValue<V> getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.key);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SimpleEntry<?> other = (SimpleEntry<?>) obj;
            if (!Objects.equals(this.key, other.key)) {
                return false;
            }
            return Objects.equals(this.value, other.value);
        }

    }

    protected static class FromEntryMap implements Function<Map.Entry<String, BsonValue<?>>, Entry<?>> {

        public static final FromEntryMap INSTANCE = new FromEntryMap();

        private FromEntryMap() {}

        @Override
        public Entry<?> apply(@Nonnull Map.Entry<String, BsonValue<?>> input) {
            return new SimpleEntry<>(input.getKey(), input.getValue());
        }

    }
}
