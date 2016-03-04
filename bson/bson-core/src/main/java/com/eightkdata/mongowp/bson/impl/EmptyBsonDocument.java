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

import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.abst.AbstractBsonDocument;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collections;

/**
 *
 */
public final class EmptyBsonDocument extends AbstractBsonDocument {

    private static final long serialVersionUID = -8555042328029670425L;

    private EmptyBsonDocument() {
    }

    @Override
    public BsonValue<?> get(String key) {
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Entry<?> getEntry(String key) {
        return null;
    }

    @Override
    public UnmodifiableIterator<Entry<?>> iterator() {
        return Iterators.unmodifiableIterator(Collections.<Entry<?>>emptyIterator());
    }

    public static EmptyBsonDocument getInstance() {
        return EmptyBsonDocumentHolder.INSTANCE;
    }

    private static class EmptyBsonDocumentHolder {
        private static final EmptyBsonDocument INSTANCE = new EmptyBsonDocument();
    }

    //@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
    private Object readResolve()  {
        return EmptyBsonDocument.getInstance();
    }
 }
