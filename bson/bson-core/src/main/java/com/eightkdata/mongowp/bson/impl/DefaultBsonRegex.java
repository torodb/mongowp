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

import com.eightkdata.mongowp.bson.abst.AbstractBsonRegex;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 */
public class DefaultBsonRegex extends AbstractBsonRegex {

    private static final long serialVersionUID = 3874198083590003304L;
    private final Set<Options> options;
    private final String pattern;

    public DefaultBsonRegex(EnumSet<Options> options, String pattern) {
        this.options = Collections.unmodifiableSet(EnumSet.copyOf(options));
        this.pattern = pattern;
    }

    @Override
    public Set<Options> getOptions() {
        return options;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

}