/*
 * MongoWP - MongoWP: Bson Netty
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
package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.BsonType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author gortiz
 */
public class ParsingToolsTest {

    public ParsingToolsTest() {
    }

    /**
     * Test that there is a byte value for each {@link BsonType}
     * @throws NettyBsonReaderException
     */
    @Test
    public void testTotality() throws NettyBsonReaderException {
        for (BsonType value : BsonType.values()) {
            ParsingTools.getByte(value);
        }
    }

    /**
     * Tests that if b1 != b2, where both are ByteBuf, then their associated
     * bytes are different.
     * @throws NettyBsonReaderException
     */
    @Test
    public void testInjective() throws NettyBsonReaderException {
        BsonType[] types = BsonType.values();
        Map<Byte, BsonType> foundBytes = new HashMap<>(types.length);
        for (BsonType value : types) {
            byte asByte = ParsingTools.getByte(value);
            BsonType old = foundBytes.put(asByte, value);

            assertNull(old + " and " + value + " has the same associated byte (" + asByte+")", old);
        }
        assert foundBytes.size() == types.length : "The invariant of the test has been broken";
    }

    /**
     * Tests that for each type b1, getBsonType(getByte(b1)).equals(b1).
     * @throws NettyBsonReaderException
     */
    @Test
    public void testInverse() throws NettyBsonReaderException {
        BsonType[] types = BsonType.values();
        for (BsonType value : types) {
            byte asByte = ParsingTools.getByte(value);
            assertEquals(value, ParsingTools.getBsonType(asByte));
        }
    }

}
