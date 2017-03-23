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
package com.eightkdata.mongowp.bson;

import com.google.common.primitives.UnsignedInteger;

import java.time.Instant;

import javax.annotation.Nonnegative;

/**
 *
 */
public interface BsonObjectId extends BsonValue<BsonObjectId> {

  byte[] toByteArray();

  UnsignedInteger getUnsignedTimestamp();

  Instant getTimestamp();

  /**
   * A machine identifier.
   * <p>
   * From 0 to 2^24 - 1.
   *
   * @return
   */
  @Nonnegative
  int getMachineIdentifier();

  /**
   * A process identifier.
   * <p>
   * From 0 to 2^16 - 1.
   *
   * @return
   */
  @Nonnegative
  int getProcessId();

  /**
   * A incremental counter value.
   * <p>
   * From 0 to 2^24 -1.
   *
   * @return
   */
  @Nonnegative
  int getCounter();

  /**
   * A 24th length string where {@link #toByteArray()} is written in hex format.
   *
   * @return
   */
  public String toHexString();

  /**
   * Two BsonObjectId are equal if their timestamp, machineIdentifier, processId and counter
   * properties are equal.
   * <p>
   * This is the same than to say that Arrays.equals(this.toByteArray(), other.toByteArray()) are
   * equal.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);

  /**
   * The hashCode of a BsonObjectId is
   * <code>getIntTimestamp() &lt;&lt; 16 | (getCounter() &amp; 0xffff)</code>.
   *
   * @return
   */
  @Override
  public int hashCode();

  /**
   * The string representation of a BsonObjectId should (but may not) be the same as the returned by
   * {@link #toHexString() }
   *
   * @return
   */
  @Override
  public String toString();

}
