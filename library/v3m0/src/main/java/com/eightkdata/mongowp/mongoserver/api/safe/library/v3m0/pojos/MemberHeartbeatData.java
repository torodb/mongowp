

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatReply;
import com.google.common.net.HostAndPort;
import java.time.Instant;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class contains the data returned from a heartbeat command for one member
 * of a replica set.
 */
public class MemberHeartbeatData {
    private static final Logger LOGGER = LogManager.getLogger(MemberHeartbeatData.class);
    private Health health;
    private @Nullable Instant upSince;
    private Instant lastHeartbeat;
    private @Nullable Instant lastHeartbeatRecv;
    private boolean authIssue;
    private @Nonnull ReplSetHeartbeatReply lastResponse;

    public MemberHeartbeatData() {
        this.health = Health.NOT_CHECKED;
        this.upSince = Instant.EPOCH;
        this.lastHeartbeat = Instant.EPOCH;
        this.lastHeartbeatRecv = Instant.EPOCH;
        this.authIssue = false;

        lastResponse = new ReplSetHeartbeatReply.Builder()
        		.setSetName("_unnamed")
        		.setElectionTime(OpTime.EPOCH)
                .setState(MemberState.RS_UNKNOWN)
                .setOpTime(OpTime.EPOCH)
                .build();
    }

    public MemberHeartbeatData(
            Health health,
            @Nullable Instant upSince,
            Instant lastHeartbeat,
            @Nullable Instant lastHeartbeatRecv,
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

    /**
     * Returns the instant when we recived the last heartbeat from this node.
     * @return the instant when we recived the last heartbeat from this node.
     */
    @Nullable
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

    @Nullable
    public OpTime getOpTime() {
        return lastResponse.getOpTime();
    }

    @Nonnull
    public String getLastHeartbeatMessage() {
        return lastResponse.getHbMsg();
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

    /**
     * @return true iff the member is up or if no heartbeat has been received from him yet.
     */
    public boolean maybeUp() {
        return health != Health.NOT_CHECKED;
    }

    public boolean isUp() {
        return health == Health.UP;
    }

    public boolean isUnelectable() {
        return lastResponse.isElectable();
    }

    public void setUpValues(@Nonnull Instant now, @Nonnull HostAndPort host,
            @Nonnull ReplSetHeartbeatReply hbResponse) {
        health = Health.UP;
        if (upSince.equals(Instant.EPOCH)) {
            upSince = now;
        }
        authIssue = false;
        lastHeartbeat = now;

        ReplSetHeartbeatReply.Builder lastResponseBuilder = new ReplSetHeartbeatReply.Builder(hbResponse);
        if (hbResponse.getState() == null) {
            lastResponseBuilder.setState(MemberState.RS_UNKNOWN);
        }
        if (hbResponse.getElectionTime() == null) {
            lastResponseBuilder.setElectionTime(lastResponse.getElectionTime());
        }
        if (hbResponse.getOpTime() == null) {
            lastResponseBuilder.setOpTime(lastResponse.getOpTime());
        }

        // Log if the state changes
        if (lastResponse.getState() != hbResponse.getState()) {
            LOGGER.info("Member {} is now in state {}", host, hbResponse.getState());
        }

        lastResponse = lastResponseBuilder.build();
    }

    public void setAuthIssue(Instant now) {
        health = Health.UNREACHABLE;  // set health to 0 so that this doesn't count towards majority.
        upSince = Instant.EPOCH;
        lastHeartbeat = now;
        authIssue = true;

        lastResponse = new ReplSetHeartbeatReply.Builder()
        		.setSetName(lastResponse.getSetName())
        		.setElectionTime(OpTime.EPOCH)
                .setState(MemberState.RS_UNKNOWN)
                .setOpTime(OpTime.EPOCH)
                .setSyncingTo(null)
                .build();
    }

    public void setDownValues(Instant now, @Nonnull String errorDesc) {
        health = Health.UNREACHABLE;  // set health to 0 so that this doesn't count towards majority.
        upSince = Instant.EPOCH;
        lastHeartbeat = now;
        authIssue = false;

        lastResponse = new ReplSetHeartbeatReply.Builder()
        		.setSetName(lastResponse.getSetName())
                .setElectionTime(OpTime.EPOCH)
                .setHbmsg(errorDesc)
                .setState(MemberState.RS_DOWN)
                .setOpTime(OpTime.EPOCH)
                .setSyncingTo(null)
                .build();
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

        public Builder() {
        }

        public Builder(MemberHeartbeatData other) {
            this.health = other.health;
            this.upSince = other.upSince;
            this.lastHeartbeat = other.lastHeartbeat;
            this.lastHeartbeatRecv = other.lastHeartbeatRecv;
            this.authIssue = other.authIssue;
            this.lastResponse = other.lastResponse;
        }

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

        public MemberHeartbeatData build() {
            return new MemberHeartbeatData(health, upSince, lastHeartbeat, lastHeartbeatRecv,
                    authIssue, lastResponse);
        }
    }
}
