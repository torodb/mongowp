/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.server.util;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class EnumInt32FlagsUtil {

  public static boolean isActive(EnumBitFlags flag, int flags) {
    return (flags & 1 << flag.getFlagBitPosition()) != 0;
  }

  private static <T extends Enum<T> & EnumBitFlags> int getInt32FlagsArray(
      @Nullable Collection<T> flags) {
    if (null == flags || flags.isEmpty()) {
      return 0;
    }

    BitSet bitSet = new BitSet(Ints.BYTES);
    for (T t : flags) {
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
   * @throws java.lang.IndexOutOfBoundsException If the getFlagBitPosition() returned by any of the
   *                                             enum values is greater than 32
   */
  public static <T extends Enum<T> & EnumBitFlags> int getInt32AllFlagsMask(
      @Nonnull Class<T> enumClass) {
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
   * @throws java.lang.IllegalArgumentException If there are flags set outside of the positions
   *                                            denoted by the enum
   */
  public static <T extends Enum<T> & EnumBitFlags> EnumSet<T> getActiveFlags(
      Class<T> enumClass, int flagsMask, int flags
  ) {
    if ((flags & ~flagsMask) != 0) {
      throw new IllegalArgumentException("Unsupported flags within int (" + flags + ")");
    }

    BitSet bitSet = convert(flags);
    EnumSet<T> flagEnumSet = EnumSet.noneOf(enumClass);
    for (T flag : enumClass.getEnumConstants()) {
      if (bitSet.get(flag.getFlagBitPosition())) {
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
