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
package com.torodb.mongowp.bson.netty;

import static org.junit.Assert.*;

import com.torodb.mongowp.bson.BsonType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gortiz
 */
public class ParsingToolsTest {

  public ParsingToolsTest() {
  }

  /**
   * Test that there is a byte value for each {@link BsonType}
   *
   * @throws NettyBsonReaderException
   */
  @Test
  public void testTotality() throws NettyBsonReaderException {
    for (BsonType value : BsonType.values()) {
      ParsingTools.getByte(value);
    }
  }

  /**
   * Tests that if b1 != b2, where both are ByteBuf, then their associated bytes are different.
   *
   * @throws NettyBsonReaderException
   */
  @Test
  public void testInjective() throws NettyBsonReaderException {
    BsonType[] types = BsonType.values();
    Map<Byte, BsonType> foundBytes = new HashMap<>(types.length);
    for (BsonType value : types) {
      byte asByte = ParsingTools.getByte(value);
      BsonType old = foundBytes.put(asByte, value);

      assertNull(old + " and " + value + " has the same associated byte (" + asByte + ")", old);
    }
    assert foundBytes.size() == types.length : "The invariant of the test has been broken";
  }

  /**
   * Tests that for each type b1, getBsonType(getByte(b1)).equals(b1).
   *
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
