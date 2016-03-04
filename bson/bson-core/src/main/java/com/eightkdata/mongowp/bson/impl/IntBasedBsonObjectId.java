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

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonObjectId;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInteger;
import org.threeten.bp.Instant;

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

        Preconditions.checkArgument(machineId >= 0, "Illegal machine id %s. It must be higher or equal than 0", machineId);
        Preconditions.checkArgument(machineId < 2 << 24, "Illegal machine id %s. It must be lower than %s", machineId, 2 << 24);
        this.machineId = machineId;

        Preconditions.checkArgument(processId >= 0, "Illegal process id %s. It must be higher or equal than 0", processId);
        Preconditions.checkArgument(processId < 2 << 16, "Illegal process id %s. It must be lower than %s", processId, 2 << 12);
        this.processId = processId;

        Preconditions.checkArgument(counter >= 0, "Illegal counter %s. It must be higher or equal than 0", counter);
        Preconditions.checkArgument(counter < 2 << 24, "Illegal counter %s. It must be lower than %s", 2 << 24);
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
