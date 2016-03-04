

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatReply;
import com.google.common.net.HostAndPort;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.threeten.bp.Instant;

/**
 * This class contains the data returned from a heartbeat command for one member
 * of a replica set.
 */
@Immutable
public class MemberHeartbeatData {

    private final Health health;
    private final @Nullable Instant upSince;
    private final Instant lastHeartbeat;
    private final Instant lastHeartbeatRecv;
    private final boolean authIssue;
    private final @Nonnull ReplSetHeartbeatReply lastResponse;

    public MemberHeartbeatData(
            Health health,
            @Nullable Instant upSince,
            Instant lastHeartbeat,
            Instant lastHeartbeatRecv,
            boolean authIssue,
            ReplSetHeartbeatReply lastResponse) {
        this.health = health;
        this.upSince = upSince;
        this.lastHeartbeat = lastHeartbeat;
        this.lastHeartbeatRecv = lastHeartbeatRecv;
        this.authIssue = authIssue;
        this.lastResponse = lastResponse;
    }

    public Health getHealth() {
        return health;
    }

    @Nullable
    public Instant getUpSince() {
        return upSince;
    }

    public Instant getLastHeartbeat() {
        return lastHeartbeat;
    }

    public Instant getLastHeartbeatRecv() {
        return lastHeartbeatRecv;
    }

    public boolean isAuthIssue() {
        return authIssue;
    }

    public ReplSetHeartbeatReply getLastResponse() {
        return lastResponse;
    }

    public MemberState getState() {
        return lastResponse.getState();
    }

    public OpTime getOpTime() {
        return lastResponse.getOpTime();
    }

    @Nonnull
    public String getLastHeartbeatMessage() {
        return lastResponse.getHbmsg();
    }

    public HostAndPort getSyncSource() {
        return lastResponse.getSyncingTo();
    }

    public OpTime getElectionTime() {
        return lastResponse.getElectionTime();
    }

    public long getConfigVersion() {
        return lastResponse.getConfigVersion();
    }

    public static enum Health {
        NOT_CHECKED(-1),
        UNREACHABLE(0),
        UP(1);

        private final double id;

        private Health(double id) {
            this.id = id;
        }

        public double getId() {
            return id;
        }

        public static Health fromId(double id) throws IllegalArgumentException {
            for (Health value : Health.values()) {
                if (Double.compare(value.getId(), id) == 0) {
                    return value;
                }
            }
            throw new IllegalArgumentException("There is no valid health element whose id is '"+id+"'");
        }
    }

    public static class Builder {
        private Health health;
        private Instant upSince;
        private Instant lastHeartbeat;
        private Instant lastHeartbeatRecv;
        private boolean authIssue;
        private ReplSetHeartbeatReply lastResponse;

        public Health getHealth() {
            return health;
        }

        public Builder setHealth(Health health) {
            this.health = health;
            return this;
        }

        public Instant getUpSince() {
            return upSince;
        }

        public Builder setUpSince(Instant upSince) {
            this.upSince = upSince;
            return this;
        }

        public Instant getLastHeartbeat() {
            return lastHeartbeat;
        }

        public Builder setLastHeartbeat(Instant lastHeartbeat) {
            this.lastHeartbeat = lastHeartbeat;
            return this;
        }

        public Instant getLastHeartbeatRecv() {
            return lastHeartbeatRecv;
        }

        public Builder setLastHeartbeatRecv(Instant lastHeartbeatRecv) {
            this.lastHeartbeatRecv = lastHeartbeatRecv;
            return this;
        }

        public boolean isAuthIssue() {
            return authIssue;
        }

        public Builder setAuthIssue(boolean authIssue) {
            this.authIssue = authIssue;
            return this;
        }

        public ReplSetHeartbeatReply getLastResponse() {
            return lastResponse;
        }

        public Builder setLastResponse(ReplSetHeartbeatReply lastResponse) {
            this.lastResponse = lastResponse;
            return this;
        }
    }
}
