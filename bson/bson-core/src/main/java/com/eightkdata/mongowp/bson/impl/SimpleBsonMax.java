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

import com.eightkdata.mongowp.bson.abst.AbstractBsonMax;

/**
 *
 */
public class SimpleBsonMax extends AbstractBsonMax {

    private static final long serialVersionUID = 6559102896469243159L;

    private SimpleBsonMax() {
    }

    public static SimpleBsonMax getInstance() {
        return SimpleBsonMaxHolder.INSTANCE;
    }

    private static class SimpleBsonMaxHolder {
        private static final SimpleBsonMax INSTANCE = new SimpleBsonMax();
    }

    //@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
    private Object readResolve()  {
        return SimpleBsonMax.getInstance();
    }
 }
