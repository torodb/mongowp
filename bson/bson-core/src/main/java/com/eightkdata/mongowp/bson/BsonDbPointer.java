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

package com.eightkdata.mongowp.bson;

import java.util.Arrays;

/**
 *
 */
public interface BsonDbPointer extends BsonValue<BsonDbPointer> {

    String getNamespace();

    BsonObjectId getId();

    /**
     * The hashCode of a BsonDbPointer is the hash code of its object id.
     * @return
     * @see Arrays#hashCode(byte[]) 
     */
    @Override
    public int hashCode();

    /**
     * Two BsonDbPointer values are equal if their namespace and id properties
     * are the equal.
     *
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj);

}
