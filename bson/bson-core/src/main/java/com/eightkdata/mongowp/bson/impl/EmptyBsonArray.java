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
 * along with bson-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
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
public final class EmptyBsonArray extends AbstractBsonArray {

    private static final long serialVersionUID = -7588861924958681618L;

    private EmptyBsonArray() {
    }

    public static EmptyBsonArray getInstance() {
        return EmptyBsonArrayHolder.INSTANCE;
    }

    @Override
    public BsonValue<?> get(int index) {
        throw new IndexOutOfBoundsException("Requested index '" + index + "' is higher than the array size (which is 0)");
    }

    @Override
    public boolean contains(BsonValue<?> element) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public UnmodifiableIterator<BsonValue<?>> iterator() {
        return Iterators.unmodifiableIterator(Collections.<BsonValue<?>>emptyIterator());
    }

    private static class EmptyBsonArrayHolder {
        private static final EmptyBsonArray INSTANCE = new EmptyBsonArray();
    }

    //@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
    private Object readResolve()  {
        return EmptyBsonArray.getInstance();
    }
 }
