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
package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.abst.AbstractBsonArray;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collections;

/**
 *
 */
public class SingleValueBsonArray extends AbstractBsonArray {

    private static final long serialVersionUID = -1073107257449881416L;

    private final BsonValue<?> child;

    public SingleValueBsonArray(BsonValue<?> child) {
        this.child = child;
    }

    @Override
    public BsonValue<?> get(int index) throws IndexOutOfBoundsException {
        if (index == 0) {
            return child;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean contains(BsonValue<?> element) {
        return child.equals(element);
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public UnmodifiableIterator<BsonValue<?>> iterator() {
        return Iterators.unmodifiableIterator(Collections.<BsonValue<?>>singleton(child).iterator());
    }

}
