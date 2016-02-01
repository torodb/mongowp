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


package com.eightkdata.mongowp.server.util;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class EnumInt32FlagsUtil {

    public static boolean isActive(EnumBitFlags flag, int flags) {
        return (flags & 1 << flag.getFlagBitPosition()) != 0;
    }

    private static <T extends Enum<T> & EnumBitFlags> int getInt32FlagsArray(@Nullable Collection<T> flags) {
        if(null == flags || flags.isEmpty()) {
            return 0;
        }

        BitSet bitSet = new BitSet(Ints.BYTES);
        for(T t : flags) {
            bitSet.set(t.getFlagBitPosition());
        }

        return convert(bitSet);
    }

    public static <T extends Enum<T> & EnumBitFlags> int getInt32Flags(@Nullable Set<T> flags) {
        return getInt32FlagsArray(flags);
    }

    /**
     *
     * @param enumClass
     * @param <T>
     * @return
     * @throws java.lang.IndexOutOfBoundsException If the getFlagBitPosition() returned by any of the enum values
     * is greater than 32
     */
    public static <T extends Enum<T> & EnumBitFlags> int getInt32AllFlagsMask(@Nonnull Class<T> enumClass) {
        Preconditions.checkNotNull(enumClass);

        return getInt32FlagsArray(Arrays.asList(enumClass.getEnumConstants()));
    }

    /**
     *
     * @param enumClass
     * @param flagsMask
     * @param flags
     * @param <T>
     * @return
     * @throws java.lang.IllegalArgumentException If there are flags set outside of the positions denoted by the enum
     */
    public static <T extends Enum<T> & EnumBitFlags> EnumSet<T> getActiveFlags(
            Class<T> enumClass, int flagsMask, int flags
    ) {
        if((flags & ~flagsMask) != 0) {
            throw new IllegalArgumentException("Unsupported flags within int (" + flags + ")");
        }

        BitSet bitSet = convert(flags);
        EnumSet<T> flagEnumSet = EnumSet.noneOf(enumClass);
        for(T flag : enumClass.getEnumConstants()) {
            if(bitSet.get(flag.getFlagBitPosition())) {
                flagEnumSet.add(flag);
            }
        }

        return flagEnumSet;
    }

    public static int convert(BitSet bits) {
        assert bits.length() <= 32;

        int value = 0;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? 1 << i : 0;
        }
        return value;
    }

    public static BitSet convert(int value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0) {
            if (value % 2 != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }
}
