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
 * along with v3m0. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonTimestamp;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberState;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.google.common.net.HostAndPort;
import java.time.Duration;
import java.util.Optional;
import java.util.OptionalInt;

/**
 *
 */
public interface ReplSetHeartbeatReply {

    public ErrorCode getErrorCode();

    public Optional<String> getErrMsg();

    public Optional<BsonTimestamp> getElectionTime();

    public Optional<Duration> getTime();

    public Optional<OpTime> getAppliedOptime();

    public Optional<OpTime> getDurableOptime();

    public Optional<Boolean> getElectable();

    public Optional<Boolean> getHasData();

    public boolean isMismatch();

    public boolean isStateDisagreement();

    public Optional<MemberState> getState();

    public long getConfigVersion();

    public Optional<String> getSetName();

    public String getHbmsg();

    public Optional<HostAndPort> getSyncingTo();

    public Optional<ReplicaSetConfig> getConfig();

    public OptionalInt getPrimaryId();

    public long getTerm();

    /**
     * Returns an optional that indicates if the node is on a replica set or on
     * a master slave replication.
     *
     * @return an optional whose value is true if the node is on a replica set,
     *         false if it is on master slave or empty if it is unknown
     */
    public Optional<Boolean> getIsReplSet();
    

}
