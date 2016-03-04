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

import com.google.common.primitives.UnsignedInteger;
import javax.annotation.Nonnegative;
import org.threeten.bp.Instant;

/**
 *
 */
public interface BsonObjectId extends BsonValue<BsonObjectId>, Comparable<BsonObjectId> {

    byte[] toByteArray();

    UnsignedInteger getUnsignedTimestamp();

    Instant getTimestamp();

    /**
     * A machine identifier.
     *
     * From 0 to 2^24 - 1.
     *
     * @return
     */
    @Nonnegative
    int getMachineIdentifier();

    /**
     * A process identifier.
     *
     * From 0 to 2^16 - 1.
     * @return
     */
    @Nonnegative
    int getProcessId();

    /**
     * A incremental counter value.
     *
     * From 0 to 2^24 -1.
     * @return
     */
    @Nonnegative
    int getCounter();

    /**
     * A 24th length string where {@link #toByteArray()} is written in hex format.
     * @return
     */
    public String toHexString();

    /**
     * Two BsonObjectId are equal if their timestamp, machineIdentifier,
     * processId and counter properties are equal.
     *
     * This is the same than to say that
     * Arrays.equals(this.toByteArray(), other.toByteArray()) are equal.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj);

    /**
     * The hashCode of a BsonObjectId is <code>getIntTimestamp() &lt;&lt; 16 | (getCounter() &amp; 0xffff)</code>.
     * @return
     */
    @Override
    public int hashCode();

    /**
     * The string representation of a BsonObjectId should (but may not) be the
     * same as the returned by {@link #toHexString() }
     * @return
     */
    @Override
    public String toString();

}
