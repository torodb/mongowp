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

import com.eightkdata.mongowp.bson.abst.AbstractBsonTimestamp;

/**
 *
 */
public class DefaultBsonTimestamp extends AbstractBsonTimestamp {

    private static final long serialVersionUID = 8902213292168089542L;
    private final int secondsSinceEpoch;
    private final int ordinal;

    public DefaultBsonTimestamp(int secondsSinceEpoch, int ordinal) {
        this.secondsSinceEpoch = secondsSinceEpoch;
        this.ordinal = ordinal;
    }

    @Override
    public int getSecondsSinceEpoch() {
        return secondsSinceEpoch;
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }
}