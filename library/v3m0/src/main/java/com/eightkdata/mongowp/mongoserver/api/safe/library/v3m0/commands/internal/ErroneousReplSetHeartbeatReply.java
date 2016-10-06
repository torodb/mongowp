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
class ErroneousReplSetHeartbeatReply implements ReplSetHeartbeatReply {

    private final ErrorCode errorCode;
    private final Optional<String> errMsg;
    private final boolean mismatch;
    private final Optional<String> setName;

    public ErroneousReplSetHeartbeatReply(ErrorCode errorCode,
            String errMsg, boolean mismatch, Optional<String> setName) {
        this.errorCode = errorCode;
        this.errMsg = Optional.of(errMsg);
        this.mismatch = mismatch;
        this.setName = setName;
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public Optional<String> getErrMsg() {
        return errMsg;
    }

    @Override
    public Optional<BsonTimestamp> getElectionTime() {
        return Optional.empty();
    }

    @Override
    public Optional<Duration> getTime() {
        return Optional.empty();
    }

    @Override
    public Optional<OpTime> getAppliedOptime() {
        return Optional.empty();
    }

    @Override
    public Optional<OpTime> getDurableOptime() {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> getElectable() {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> getHasData() {
        return Optional.empty();
    }

    @Override
    public boolean isMismatch() {
        return mismatch;
    }

    @Override
    public boolean isStateDisagreement() {
        return false;
    }

    @Override
    public Optional<MemberState> getState() {
        return Optional.empty();
    }

    @Override
    public long getConfigVersion() {
        return -1;
    }

    @Override
    public Optional<String> getSetName() {
        return Optional.empty();
    }

    @Override
    public String getHbmsg() {
        return "";
    }

    @Override
    public Optional<HostAndPort> getSyncingTo() {
        return Optional.empty();
    }

    @Override
    public Optional<ReplicaSetConfig> getConfig() {
        return Optional.empty();
    }

    @Override
    public OptionalInt getPrimaryId() {
        return OptionalInt.empty();
    }

    @Override
    public long getTerm() {
        return -1;
    }

    @Override
    public Optional<Boolean> getIsReplSet() {
        return Optional.empty();
    }

}
