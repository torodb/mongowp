
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetGetStatusCommand.ReplSetGetStatusReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberHeartbeatData;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberState;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.BsonValueComparator;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import com.google.common.primitives.UnsignedInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.*;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

/**
 * Report status of a replica set from the POV of this server.
 */
@Immutable
public class ReplSetGetStatusCommand extends AbstractCommand<Empty, ReplSetGetStatusReply>{

    public static final ReplSetGetStatusCommand INSTANCE = new ReplSetGetStatusCommand();

    private ReplSetGetStatusCommand() {
        super("replSetGetStatus");
    }

    @Override
    public Class<? extends Empty> getArgClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallArg(BsonDocument requestDoc) {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallArg(Empty request) {
        return EmptyCommandArgumentMarshaller.marshallEmptyArgument(this);
    }

    @Override
    public Class<? extends ReplSetGetStatusReply> getResultClass() {
        return ReplSetGetStatusReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetGetStatusReply reply) {
        return reply.marshall();
    }

    @Override
    public ReplSetGetStatusReply unmarshallResult(BsonDocument resultDoc) throws
            MongoException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Immutable
    public static abstract class ReplSetGetStatusReply {
        protected abstract BsonDocument marshall();
    }

    @Immutable
    public static class InvalidReplSetGetStatusReply extends ReplSetGetStatusReply {
        private final @Nonnull MemberState state;
        private final @Nonnull Duration uptime;
        private final @Nonnull OpTime optime;
        private final @Nonnegative int maintenanceModeCalls;
        private final @Nullable String heartbeatMessage;

        public InvalidReplSetGetStatusReply(
                MemberState state,
                Duration uptime,
                OpTime optime,
                int maintenanceModeCalls,
                String heartbeatMessage) {
            this.state = state;
            this.uptime = uptime;
            this.optime = optime;
            this.maintenanceModeCalls = maintenanceModeCalls;
            this.heartbeatMessage = heartbeatMessage;
        }

        private static final BsonField<Integer> STATE_FIELD = BsonField.create("state");
        private static final BsonField<String> STATE_STR_FIELD = BsonField.create("stateStr");
        private static final BsonField<Integer> UPTIME_FIELD = BsonField.create("uptime");
        private static final BsonField<OpTime> OPTIME_FIELD = BsonField.create("optime");
        private static final BsonField<Instant> OPTIME_DATE_FIELD = BsonField.create("optimeDate");
        private static final BsonField<Integer> MAINTENANCE_MODE_FIELD = BsonField.create("maintenanceMode");
        private static final BsonField<String> INFO_MESSAGE_FIELD = BsonField.create("infoMessage");

        @Override
        protected BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();
            builder.append(STATE_FIELD, state.getId())
                    .append(STATE_STR_FIELD, state.name())
                    .append(UPTIME_FIELD, (int) uptime.getSeconds()) //TODO: Check if cast to int is correct
                    .append(OPTIME_FIELD, optime)
                    .appendInstant(OPTIME_DATE_FIELD, optime.toEpochMilli());
            if (maintenanceModeCalls != 0) {
                builder.append(MAINTENANCE_MODE_FIELD, maintenanceModeCalls);
            }
            if (heartbeatMessage != null) {
                builder.append(INFO_MESSAGE_FIELD, heartbeatMessage);
            }

            return builder.build();
        }
    }

    @Immutable
    public static class CorrectReplSetGetStatusReply extends ReplSetGetStatusReply {
        private static final BsonField<String> SET_NAME_FIELD = BsonField.create("set");
        private static final BsonField<Instant> DATE_FIELD = BsonField.create("date");
        private static final BsonField<Integer> MY_STATE_FIELD = BsonField.create("myState");
        private static final BsonField<HostAndPort> SYNCING_TO_FIELD = BsonField.create("syncingTo");
        private static final BsonField<BsonArray> MEMBERS_FIELD = BsonField.create("members");

        private static final BsonField<Integer> MEMBER_ID_FIELD = BsonField.create("id");
        private static final BsonField<HostAndPort> MEMBER_NAME_FIELD = BsonField.create("name");
        private static final BsonField<Double> MEMBER_HEALTH_FIELD = BsonField.create("health");
        private static final BsonField<Integer> MEMBER_STATE_FIELD = BsonField.create("state");
        private static final BsonField<String> MEMBER_STATE_STR_FIELD = BsonField.create("stateStr");
        private static final BsonField<Integer> MEMBER_UPTIME_FIELD = BsonField.create("uptime");
        private static final BsonField<OpTime> MEMBER_OPTIME_FIELD = BsonField.create("optime");
        private static final BsonField<Instant> MEMBER_OPTIME_DATE_FIELD = BsonField.create("optimeDate");
        private static final BsonField<HostAndPort> MEMBER_SYNCING_TO_FIELD = BsonField.create("syncingTo");
        private static final BsonField<Integer> MEMBER_MAINTENANCE_MODE_FIELD = BsonField.create("maintenanceMode");
        private static final BsonField<String> MEMBER_INFO_MESSAGE_FIELD = BsonField.create("infoMessage");
        private static final BsonField<OpTime> MEMBER_ELECTION_TIME_FIELD = BsonField.create("electionTime");
        private static final BsonField<Instant> MEMBER_ELECTION_DATE_FIELD = BsonField.create("electionDate");
        private static final BsonField<Number> MEMBER_CONFIG_VERSION_FIELD = BsonField.create("configVersion");
        private static final BsonField<Boolean> MEMBER_SELF_FIELD = BsonField.create("self");
        private static final BsonField<Instant> MEMBER_LAST_HEARTBEAT = BsonField.create("lastHeartbeat");
        private static final BsonField<Instant> MEMBER_LAST_HEARTBEAT_RECIVED = BsonField.create("lastHeartbeatRecv");
        private static final BsonField<Integer> MEMBER_PING_MS = BsonField.create("pingMs");
        private static final BsonField<String> MEMBER_LAST_HEARTBEAT_MESSAGE = BsonField.create("lastHeartbeatMessage");
        private static final BsonField<Boolean> MEMBER_AUTHENTICATED = BsonField.create("authenticated");

        private final SelfData selfData;
        private final String setName;
        private final Instant now;
        private final MemberState state;
        private final @Nullable HostAndPort syncTo;
        private final ImmutableMap<MemberConfig, MemberHeartbeatData> membersInfo;
        private final ImmutableMap<MemberConfig, Integer> pings;
        private final ReplicaSetConfig replConfig;

        public CorrectReplSetGetStatusReply(
                SelfData selfData,
                String setName,
                Instant now,
                MemberState state,
                HostAndPort syncTo,
                ImmutableMap<MemberConfig, MemberHeartbeatData> membersInfo,
                ImmutableMap<MemberConfig, Integer> pings,
                ReplicaSetConfig replConfig) {
            this.selfData = selfData;
            this.setName = setName;
            this.now = now;
            this.state = state;
            this.syncTo = syncTo;
            this.membersInfo = membersInfo;
            this.pings = pings;
            this.replConfig = replConfig;
        }


        @Override
        protected BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            builder.append(SET_NAME_FIELD, setName)
                    .append(DATE_FIELD, now)
                    .append(MY_STATE_FIELD, state.getId());

            if (syncTo != null && !state.equals(MemberState.RS_PRIMARY) && !state.equals(MemberState.RS_REMOVED)) {
                builder.append(SYNCING_TO_FIELD, syncTo);
            }

            List<BsonDocument> membersList = Lists.newArrayListWithCapacity(membersInfo.size());
            for (Entry<MemberConfig, MemberHeartbeatData> entrySet : membersInfo.entrySet()) {
                MemberConfig memberConfig = entrySet.getKey();
                MemberHeartbeatData memberData = entrySet.getValue();
                if (memberConfig.getId() == selfData.getId()) {
                    membersList.add(marshallSelfMember());
                }
                else {
                    membersList.add(marshallOtherMember(memberConfig, memberData));
                }
            }
            Collections.sort(membersList, new BsonValueComparator(true));

            builder.append(MEMBERS_FIELD, new BsonArray(membersList));

            return builder.build();
        }
        
        private BsonDocument marshallSelfMember() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            builder.append(MEMBER_ID_FIELD, selfData.getId())
                    .append(MEMBER_NAME_FIELD, selfData.getName())
                    .append(MEMBER_HEALTH_FIELD, 1.0)
                    .append(MEMBER_STATE_FIELD, selfData.getState().getId())
                    .append(MEMBER_STATE_STR_FIELD, selfData.getState().name())
                    .append(MEMBER_UPTIME_FIELD, selfData.getUptime().intValue()); //TODO: Check if cast to int is correct

            if (selfData.getState().equals(MemberState.RS_ARBITER)) {
                    builder.append(MEMBER_OPTIME_FIELD, selfData.getOpTime())
                            .appendInstant(MEMBER_OPTIME_DATE_FIELD, selfData.getOpTime().toEpochMilli());
            }
            if (syncTo != null && !selfData.getState().equals(MemberState.RS_PRIMARY)) {
                builder.append(MEMBER_SYNCING_TO_FIELD, syncTo);
            }
            if (selfData.getMaintenanceModeCalls() != 0) {
                builder.append(MEMBER_MAINTENANCE_MODE_FIELD, selfData.getMaintenanceModeCalls());
            }
            if (selfData.getHeartbeatMessage() != null) {
                builder.append(MEMBER_INFO_MESSAGE_FIELD, selfData.getHeartbeatMessage());
            }
            if (selfData.getState().equals(MemberState.RS_PRIMARY)) {
                builder.append(MEMBER_ELECTION_TIME_FIELD, selfData.getElectionTime());
                builder.appendInstant(MEMBER_ELECTION_DATE_FIELD, selfData.getElectionTime().toEpochMilli());
            }
            builder.appendNumber(MEMBER_CONFIG_VERSION_FIELD, replConfig.getVersion());
            builder.append(MEMBER_SELF_FIELD, true);

            return builder.build();
        }

        private BsonDocument marshallOtherMember(MemberConfig config, MemberHeartbeatData data) {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            builder.append(MEMBER_ID_FIELD, config.getId())
                    .append(MEMBER_NAME_FIELD, config.getHost());
            MemberHeartbeatData.Health h = data.getHealth();
            builder.append(MEMBER_HEALTH_FIELD, h.getId())
                    .append(MEMBER_STATE_FIELD, data.getState().getId());

            if (h.equals(MemberHeartbeatData.Health.UNREACHABLE)) {
                builder.append(MEMBER_STATE_STR_FIELD, "(not reachable/healthy)");
            }
            else {
                builder.append(MEMBER_STATE_STR_FIELD, data.getState().name());
            }
            Instant upSince = data.getUpSince();
            if (upSince == null) {
                builder.append(MEMBER_UPTIME_FIELD, 0);
            }
            else {
                builder.append(MEMBER_UPTIME_FIELD, (int) (now.getEpochSecond() - upSince.getEpochSecond()));
            }
            if (!config.isArbiter()) {
                OpTime opTime = data.getOpTime();
                builder.append(MEMBER_OPTIME_FIELD, opTime);
                builder.appendInstant(MEMBER_OPTIME_DATE_FIELD, opTime.toEpochMilli());
            }
            builder.append(MEMBER_LAST_HEARTBEAT, data.getLastHeartbeat());
            builder.append(MEMBER_LAST_HEARTBEAT_RECIVED, data.getLastHeartbeatRecv());
            Integer ping = pings.get(config);
            if (ping != null) {
                builder.append(MEMBER_PING_MS, ping);
                String hbmsg = data.getLastHeartbeatMessage();
                builder.append(MEMBER_LAST_HEARTBEAT_MESSAGE, hbmsg);
            }
            if (data.isAuthIssue()) {
                builder.append(MEMBER_AUTHENTICATED, true);
            }
            if (!data.getState().equals(MemberState.RS_PRIMARY)) {
                if (data.getSyncSource() != null) {
                    builder.append(MEMBER_SYNCING_TO_FIELD, data.getSyncSource());
                }
            }
            else {
                //is primary
                builder.append(MEMBER_ELECTION_TIME_FIELD, data.getElectionTime());
                builder.appendInstant(MEMBER_ELECTION_DATE_FIELD, data.getElectionTime().toEpochMilli());
            }

            builder.appendNumber(MEMBER_CONFIG_VERSION_FIELD, data.getConfigVersion());

            return builder.build();
        }

        public final static class SelfData {
            private final int id;
            private final HostAndPort name;
            private final MemberState state;
            private final @Nonnull UnsignedInteger uptime;
            private final @Nonnull OpTime opTime;
            private final int maintenanceModeCalls;
            private final @Nullable String heartbeatMessage;
            private final OpTime electionTime;

            public SelfData(int id, HostAndPort name, MemberState state, UnsignedInteger uptime, OpTime opTime, int maintenanceModeCalls, String heartbeatMessage, OpTime electionTime) {
                this.id = id;
                this.name = name;
                this.state = state;
                this.uptime = uptime;
                this.opTime = opTime;
                this.maintenanceModeCalls = maintenanceModeCalls;
                this.heartbeatMessage = heartbeatMessage;
                this.electionTime = electionTime;
            }

            public int getId() {
                return id;
            }

            public HostAndPort getName() {
                return name;
            }

            public MemberState getState() {
                return state;
            }

            public UnsignedInteger getUptime() {
                return uptime;
            }

            public OpTime getOpTime() {
                return opTime;
            }

            public int getMaintenanceModeCalls() {
                return maintenanceModeCalls;
            }

            public String getHeartbeatMessage() {
                return heartbeatMessage;
            }

            public OpTime getElectionTime() {
                return electionTime;
            }
        }
    }
}
