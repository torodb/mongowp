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

import com.eightkdata.mongowp.server.util.EnumInt32FlagsUtil;
import com.eightkdata.mongowp.server.util.EnumBitFlags;
import java.util.EnumSet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class EnumInt32FlagsUtilTest {
    private enum BasicEnum implements EnumBitFlags {
        A(1),
        B(29);

        private final int position;

        private BasicEnum(int position) {
            this.position = position;
        }

        @Override
        public int getFlagBitPosition() {
            return position;
        }

        public static final int NON_COMPUTED_MASK = 0x20000002;
    }

    @Test
    public void getInt32PossibleMaskBasicCorrectnessTest() {
        assertEquals(BasicEnum.NON_COMPUTED_MASK, EnumInt32FlagsUtil.getInt32AllFlagsMask(BasicEnum.class));
    }

    @Test
    public void getActiveFlagsBasicCorrectnessTest() {
        int basicEnumMask = EnumInt32FlagsUtil.getInt32AllFlagsMask(BasicEnum.class);

        EnumSet<BasicEnum> enumFlags = EnumInt32FlagsUtil.getActiveFlags(BasicEnum.class, basicEnumMask, 0x00000002);
        assertTrue(AssertSetsUtil.assertSetsEqual(EnumSet.of(BasicEnum.A), enumFlags));

        enumFlags = EnumInt32FlagsUtil.getActiveFlags(BasicEnum.class, basicEnumMask, 0x20000000);
        assertTrue(AssertSetsUtil.assertSetsEqual(EnumSet.of(BasicEnum.B), enumFlags));

        enumFlags = EnumInt32FlagsUtil.getActiveFlags(BasicEnum.class, basicEnumMask, 0x20000002);
        assertTrue(AssertSetsUtil.assertSetsEqual(EnumSet.of(BasicEnum.B, BasicEnum.A), enumFlags));
    }
}
