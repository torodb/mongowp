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
package com.torodb.mongowp.bson.impl;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInteger;
import com.torodb.mongowp.bson.abst.AbstractBsonObjectId;

import java.time.Instant;

/**
 *
 */
public class IntBasedBsonObjectId extends AbstractBsonObjectId {

  private static final long serialVersionUID = -7388021857492952479L;

  private final int timestamp;
  private final int machineId;
  private final int processId;
  private final int counter;

  public IntBasedBsonObjectId(int unsignedTimestamp, int machineId, int processId, int counter) {
    this.timestamp = unsignedTimestamp;

    Preconditions.checkArgument(machineId >= 0,
        "Illegal machine id %s. It must be higher or equal than 0", machineId);
    Preconditions.checkArgument(machineId < 2 << 24,
        "Illegal machine id %s. It must be lower than %s", machineId, 2 << 24);
    this.machineId = machineId;

    Preconditions.checkArgument(processId >= 0,
        "Illegal process id %s. It must be higher or equal than 0", processId);
    Preconditions.checkArgument(processId < 2 << 16,
        "Illegal process id %s. It must be lower than %s", processId, 2 << 12);
    this.processId = processId;

    Preconditions.checkArgument(counter >= 0,
        "Illegal counter %s. It must be higher or equal than 0", counter);
    Preconditions.checkArgument(counter < 2 << 24, "Illegal counter %s. It must be lower than %s",
        2 << 24);
    this.counter = counter;
  }

  @Override
  public UnsignedInteger getUnsignedTimestamp() {
    return UnsignedInteger.fromIntBits(timestamp);
  }

  @Override
  public Instant getTimestamp() {
    return Instant.ofEpochMilli(getUnsignedTimestamp().longValue());
  }

  @Override
  public int getMachineIdentifier() {
    return machineId;
  }

  @Override
  public int getProcessId() {
    return processId;
  }

  @Override
  public int getCounter() {
    return counter;
  }

}
