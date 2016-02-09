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

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonMin;

/**
 *
 */
public class SimpleBsonMin extends AbstractBsonMin {

    private static final long serialVersionUID = -8265726813245342245L;

    private SimpleBsonMin() {
    }

    public static SimpleBsonMin getInstance() {
        return SimpleBsonMinHolder.INSTANCE;
    }

    private static class SimpleBsonMinHolder {
        private static final SimpleBsonMin INSTANCE = new SimpleBsonMin();
    }

    //@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
    private Object readResolve()  {
        return SimpleBsonMin.getInstance();
    }
 }
