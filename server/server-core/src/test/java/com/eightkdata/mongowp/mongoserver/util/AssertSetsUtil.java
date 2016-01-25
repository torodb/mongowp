/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */


package com.eightkdata.mongowp.mongoserver.util;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 *
 */
public class AssertSetsUtil {
    public static <T> boolean assertSetsEqual(Set<T> a, Set<T> b) {
        if(a == null || b == null) {
            return a == b;
        }
        Set<T> diff1 = Sets.difference(a, b);
        Set<T> diff2 = Sets.difference(b, a);

        return diff1.size() == 0 && diff1.size() == diff2.size();
    }
}
