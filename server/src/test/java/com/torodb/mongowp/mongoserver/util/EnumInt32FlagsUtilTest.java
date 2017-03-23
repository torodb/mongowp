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
package com.torodb.mongowp.mongoserver.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.torodb.mongowp.server.util.EnumBitFlags;
import com.torodb.mongowp.server.util.EnumInt32FlagsUtil;
import org.junit.Test;

import java.util.EnumSet;

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
    assertEquals(BasicEnum.NON_COMPUTED_MASK, EnumInt32FlagsUtil.getInt32AllFlagsMask(
        BasicEnum.class));
  }

  @Test
  public void getActiveFlagsBasicCorrectnessTest() {
    int basicEnumMask = EnumInt32FlagsUtil.getInt32AllFlagsMask(BasicEnum.class);

    EnumSet<BasicEnum> enumFlags = EnumInt32FlagsUtil.getActiveFlags(BasicEnum.class, basicEnumMask,
        0x00000002);
    assertTrue(AssertSetsUtil.assertSetsEqual(EnumSet.of(BasicEnum.A), enumFlags));

    enumFlags = EnumInt32FlagsUtil.getActiveFlags(BasicEnum.class, basicEnumMask, 0x20000000);
    assertTrue(AssertSetsUtil.assertSetsEqual(EnumSet.of(BasicEnum.B), enumFlags));

    enumFlags = EnumInt32FlagsUtil.getActiveFlags(BasicEnum.class, basicEnumMask, 0x20000002);
    assertTrue(AssertSetsUtil.assertSetsEqual(EnumSet.of(BasicEnum.B, BasicEnum.A), enumFlags));
  }
}
