package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberState;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplSetProtocolVersion;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.*;
import com.google.common.collect.Sets;
import com.google.common.net.HostAndPort;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.*;

/**
 *
 */
public class ReplSetHeartbeatCommand extends AbstractCommand<ReplSetHeartbeatArgument, ReplSetHeartbeatReply>{

    public static final ReplSetHeartbeatCommand INSTANCE = new ReplSetHeartbeatCommand();

    private ReplSetHeartbeatCommand() {
        super("replSetHeartbeat");
    }

    @Override
    public boolean isReadyToReplyResult(ReplSetHeartbeatReply r) {
        return true;
    }

    @Override
    public Class<? extends ReplSetHeartbeatArgument> getArgClass() {
        return ReplSetHeartbeatArgument.class;
    }

    @Override
    public ReplSetHeartbeatArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return ReplSetHeartbeatArgument.fromDocument(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(ReplSetHeartbeatArgument request) {
        return request.toBSON();
    }

    @Override
    public Class<? extends ReplSetHeartbeatReply> getResultClass() {
        return ReplSetHeartbeatReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetHeartbeatReply reply) {
        return reply.toBSON();
    }

    @Override
    public ReplSetHeartbeatReply unmarshallResult(BsonDocument replyDoc) throws NoSuchKeyException, TypesMismatchException, FailedToParseException, MongoException {
        return ReplSetHeartbeatReply.fromDocument(replyDoc);
    }

    public static class ReplSetHeartbeatArgument {

        private static final String CHECK_EMPTY_FIELD_NAME = "checkEmpty";
        private static final String PROTOCOL_VERSION_FIELD_NAME = "pv";
        private static final String CONFIG_VERSION_FIELD_NAME = "v";
        private static final String SENDER_ID_FIELD_NAME = "fromId";
        private static final String SET_NAME_FIELD_NAME = "replSetHeartbeat";
        private static final String SENDER_HOST_FIELD_NAME = "from";

        private static final Set<String> VALID_FIELD_NAMES = Sets.newHashSet(
                CHECK_EMPTY_FIELD_NAME,
                PROTOCOL_VERSION_FIELD_NAME,
                CONFIG_VERSION_FIELD_NAME,
                SENDER_ID_FIELD_NAME,
                SET_NAME_FIELD_NAME,
                SENDER_HOST_FIELD_NAME
        );

        private final @Nonnull ReplSetProtocolVersion protocolVersion;
        private final long configVersion;
        private final Long senderId;
        private final String setName;
        private final @Nullable HostAndPort senderHost;
        private final boolean checkEmpty;

        public ReplSetHeartbeatArgument(
                boolean checkEmpty,
                @Nonnull ReplSetProtocolVersion protocolVersion,
                long configVersion,
                long senderId,
                String setName,
                @Nullable HostAndPort senderHost) {
            this.checkEmpty = checkEmpty;
            this.protocolVersion = protocolVersion;
            this.configVersion = configVersion;
            this.senderId = senderId;
            this.setName = setName;
            this.senderHost = senderHost;
        }

        /**
         *
         * @return the protocol version the sender is using
         */
        @Nonnull
        public ReplSetProtocolVersion getProtocolVersion() {
            return protocolVersion;
        }

        /**
         *
         * @return the replica set configuration version the sender is using
         */
        public long getConfigVersion() {
            return configVersion;
        }

        /**
         *
         * @return the id of the sender on the replica set configuration
         */
        public long getSenderId() {
            return senderId;
        }

        /**
         *
         * @return the replica set name of the sender
         */
        public String getSetName() {
            return setName;
        }

        /**
         *
         * @return the host and port of the sender node
         */
        @Nullable
        public HostAndPort getSenderHost() {
            return senderHost;
        }

        public BsonDocument toBSON() {
            BsonDocument result = new BsonDocument();

            result.append(SET_NAME_FIELD_NAME, new BsonString(setName));
            result.append(PROTOCOL_VERSION_FIELD_NAME, new BsonInt64(protocolVersion.getVersionId()));
            result.append(CONFIG_VERSION_FIELD_NAME, new BsonInt64(configVersion));
            result.append(SENDER_HOST_FIELD_NAME, new BsonString(senderHost != null ? senderHost.toString() : ""));

            if (senderId != null) {
                result.append(SENDER_ID_FIELD_NAME, new BsonInt64(senderId));
            }
            if (checkEmpty) {
                result.append(CHECK_EMPTY_FIELD_NAME, BsonBoolean.TRUE);
            }
            return result;
        }

        /**
         *
         * @param bson
         * @param command
         * @return
         * @throws MongoException
         */
        public static ReplSetHeartbeatArgument fromDocument(BsonDocument bson) 
                throws BadValueException, TypesMismatchException, NoSuchKeyException {

            BsonReaderTool.checkOnlyHasFields("ReplSetHeartbeatArgs", bson, VALID_FIELD_NAMES);

            boolean checkEmpty = BsonReaderTool.getBoolean(bson, CHECK_EMPTY_FIELD_NAME, false);

            ReplSetProtocolVersion protocolVersion = ReplSetProtocolVersion.fromVersionId(
                    BsonReaderTool.getLong(bson, PROTOCOL_VERSION_FIELD_NAME)
            );

            long configVersion = BsonReaderTool.getLong(bson, CONFIG_VERSION_FIELD_NAME);

            long senderId = BsonReaderTool.getLong(bson, SENDER_ID_FIELD_NAME, -1);

            String setName = BsonReaderTool.getString(bson, SET_NAME_FIELD_NAME);

            HostAndPort senderHost = BsonReaderTool.getHostAndPort(bson, SENDER_HOST_FIELD_NAME, null);
            
            return new ReplSetHeartbeatArgument(checkEmpty, protocolVersion, configVersion, senderId, setName, senderHost);
        }
    }

    public static class ReplSetHeartbeatReply {

        private static final BsonField<BsonDocument> CONFIG_FIELD_NAME =  BsonField.create("config");
        private static final BsonField<Long> CONFIG_VERSION_FIELD_NAME = BsonField.create("v");
        private static final BsonField<OpTime> ELECTION_TIME_FIELD_NAME = BsonField.create("electionTime");
        private static final BsonField<String> ERR_MSG_FIELD_NAME = BsonField.create("errmsg");
        private static final BsonField<Integer> ERROR_CODE_FIELD_NAME = BsonField.create("code");
        private static final BsonField<Boolean> HAS_DATA_FIELD_NAME = BsonField.create("hasData");
        private static final BsonField<Boolean> HAS_STATE_DISAGREEMENT_FIELD_NAME = BsonField.create("stateDisagreement");
        private static final BsonField<String> HB_MESSAGE_FIELD_NAME = BsonField.create("hbmsg");
        private static final BsonField<Boolean> IS_ELECTABLE_FIELD_NAME = BsonField.create("e");
        private static final BsonField<Boolean> IS_REPL_SET_FIELD_NAME = BsonField.create("rs");
        private static final BsonField<Integer> MEMBER_STATE_FIELD_NAME = BsonField.create("state");
        private static final BsonField<Boolean> MISMATCH_FIELD_NAME = BsonField.create("mismatch");
        private static final BsonField<Double> OK_FIELD_NAME = BsonField.create("ok");
        private static final BsonField<OpTime> OP_TIME_FIELD = BsonField.create("opTime");
        private static final BsonField<String> REPL_SET_FIELD_NAME = BsonField.create("set");
        private static final BsonField<HostAndPort> SYNC_SOURCE_FIELD_NAME = BsonField.create("syncingTo");
        private static final BsonField<Long> TIME_FIELD_NAME = BsonField.create("time");

        private final @Nonnull OpTime electionTime;
        private final @Nullable Long time;
        private final @Nullable OpTime opTime;
        private final boolean electable;
        private final @Nullable Boolean hasData;
        private final boolean mismatch;
        private final boolean isReplSet;
        private final boolean stateDisagreement;
        private final @Nullable MemberState state;
        private final long configVersion;
        private final @Nonnull String setName;
        private final @Nonnull String hbmsg;
        private final @Nullable HostAndPort syncingTo;
        private final @Nullable ReplicaSetConfig config;

        public ReplSetHeartbeatReply(
                @Nonnull OpTime electionTime,
                @Nullable Long time,
                @Nullable OpTime opTime,
                boolean electable,
                @Nullable Boolean hasData,
                boolean mismatch,
                boolean isReplSet,
                boolean stateDisagreement,
                @Nullable MemberState state,
                long configVersion,
                @Nonnull String setName,
                @Nonnull String hbmsg,
                @Nullable HostAndPort syncingTo,
                @Nullable ReplicaSetConfig config) {
            this.electionTime = electionTime;
            this.time = time;
            this.opTime = opTime;
            this.electable = electable;
            this.hasData = hasData;
            this.mismatch = mismatch;
            this.isReplSet = isReplSet;
            this.stateDisagreement = stateDisagreement;
            this.state = state;
            this.configVersion = configVersion;
            this.setName = setName;
            this.hbmsg = hbmsg;
            this.syncingTo = syncingTo;
            this.config = config;
        }

        public OpTime getElectionTime() {
            return electionTime;
        }

        public Long getTime() {
            return time;
        }

        public OpTime getOpTime() {
            return opTime;
        }

        public boolean isElectable() {
            return electable;
        }

        public Boolean getHasData() {
            return hasData;
        }

        public boolean isMismatch() {
            return mismatch;
        }

        public boolean isIsReplSet() {
            return isReplSet;
        }

        public boolean isStateDisagreement() {
            return stateDisagreement;
        }

        public MemberState getState() {
            return state;
        }

        public long getConfigVersion() {
            return configVersion;
        }

        public String getSetName() {
            return setName;
        }

        @Nonnull
        public String getHbmsg() {
            return hbmsg;
        }

        public HostAndPort getSyncingTo() {
            return syncingTo;
        }

        public ReplicaSetConfig getConfig() {
            return config;
        }

        public BsonDocument toBSON() {
            BsonDocumentBuilder doc = new BsonDocumentBuilder();
            if (mismatch) {
                doc.append(OK_FIELD_NAME, MongoWP.KO)
                        .append(MISMATCH_FIELD_NAME, true);
                return doc.build();
            }

            doc.append(OK_FIELD_NAME, MongoWP.OK);

            if (opTime != null) {
                doc.append(OP_TIME_FIELD, opTime);
            }
            if (time != null) {
                doc.append(TIME_FIELD_NAME, time);
            }
            doc.append(ELECTION_TIME_FIELD_NAME, electionTime);
            if (config != null) {
                doc.append(CONFIG_FIELD_NAME, config.toBSON());
            }

            doc.append(IS_ELECTABLE_FIELD_NAME, electable);
            doc.append(IS_REPL_SET_FIELD_NAME, isReplSet);
            doc.append(HAS_STATE_DISAGREEMENT_FIELD_NAME, stateDisagreement);
            if (state != null) {
                doc.append(MEMBER_STATE_FIELD_NAME, state.getId());
            }
            doc.append(CONFIG_VERSION_FIELD_NAME, configVersion);
            doc.append(HB_MESSAGE_FIELD_NAME, hbmsg);
            doc.append(REPL_SET_FIELD_NAME, setName);
            if (syncingTo != null) {
                doc.append(SYNC_SOURCE_FIELD_NAME, syncingTo);
            }
            if (hasData != null) {
                doc.append(HAS_DATA_FIELD_NAME, hasData);
            }

            return doc.build();
        }

        public static ReplSetHeartbeatReply fromDocument(BsonDocument bson) 
                throws TypesMismatchException, NoSuchKeyException, BadValueException, FailedToParseException, MongoException {

            // Old versions set this even though they returned not "ok"
            boolean mismatch = BsonReaderTool.getBoolean(bson, MISMATCH_FIELD_NAME, false);
            if (mismatch) {
                throw new InconsistentReplicaSetNamesException();
            }

            // Old versions sometimes set the replica set name ("set") but ok:0
            // That means that setName != null => ok == true
            String setName;
            try {
                setName = BsonReaderTool.getString(bson, REPL_SET_FIELD_NAME, null);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + REPL_SET_FIELD_NAME
                        + "\" field in response to replSetHeartbeat to have type "
                        + "String, but found " + ex.getFoundType()
                );
            }

            boolean ok = MongoWP.OK.equals(BsonReaderTool.getDouble(bson, OK_FIELD_NAME)) || setName != null;
            if (!ok) {
                String errMsg = BsonReaderTool.getString(bson, ERR_MSG_FIELD_NAME, "");

                try {
                    int errorCode;
                    errorCode = BsonReaderTool.getNumeric(bson, ERROR_CODE_FIELD_NAME).intValue();
                    throw new MongoException(errMsg, MongoWP.ErrorCode.fromErrorCode(errorCode));
                } catch (TypesMismatchException ex) {
                    throw new BadValueException(ERROR_CODE_FIELD_NAME + " is not a number");
                } catch (NoSuchKeyException ex) {
                    throw new UnknownErrorException();
                }
            }

            Boolean hasData;
            if (!bson.containsKey(HAS_DATA_FIELD_NAME.getFieldName())) {
                hasData = null;
            }
            else {
                hasData = BsonReaderTool.getBoolean(bson, HAS_DATA_FIELD_NAME);
            }

            OpTime electionTime;
            try {
                electionTime = BsonReaderTool.getOpTime(bson, ELECTION_TIME_FIELD_NAME);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + ELECTION_TIME_FIELD_NAME
                        + "\" field in response to replSetHeartbeat command to "
                        + "have type Date or Timestamp, but found type " + ex.getFoundType()
                );
            }

            Long time;
            BsonNumber timeNumber = BsonReaderTool.getNumeric(bson, TIME_FIELD_NAME, null);
            time = timeNumber == null ? null : timeNumber.longValue();

            boolean isReplSet = BsonReaderTool.getBoolean(bson, IS_REPL_SET_FIELD_NAME, false);

            OpTime opTime;
            if (!bson.containsKey(OP_TIME_FIELD.getFieldName())) {
                opTime = null;
            }
            else {
                try {
                    opTime = BsonReaderTool.getOpTime(bson, OP_TIME_FIELD);
                } catch (TypesMismatchException ex) {
                    throw ex.newWithMessage("Expected \"" + OP_TIME_FIELD
                            + "\" field in response to replSetHeartbeat "
                            + "command to have type Date or Timestamp, but found "
                            + "type " + ex.getFoundType().toString().toLowerCase(Locale.ROOT));
                }
            }

            boolean electable = BsonReaderTool.getBoolean(bson, IS_ELECTABLE_FIELD_NAME, false);

            MemberState state;
            if (bson.containsKey(MEMBER_STATE_FIELD_NAME.getFieldName())) {
                state = null;
            } else {
                int memberId = BsonReaderTool.getNumeric(bson, MEMBER_STATE_FIELD_NAME).intValue();
                try {
                    state = MemberState.fromId(memberId);
                } catch (IllegalArgumentException ex) {
                    throw new BadValueException("Value for \""
                            + MEMBER_STATE_FIELD_NAME + "\" in response to "
                            + "replSetHeartbeat is out of range; legal values are "
                            + "non-negative and no more than " + MemberState.RS_MAX.getId()
                    );
                }
            }

            boolean stateDisagreement = BsonReaderTool.getBoolean(bson, HAS_STATE_DISAGREEMENT_FIELD_NAME, false);

            long configVersion;
            try {
                configVersion = BsonReaderTool.getNumeric(bson, CONFIG_VERSION_FIELD_NAME).longValue();
            } catch (NoSuchKeyException ex) {
                if (opTime != null) {
                    throw new NoSuchKeyException(
                            CONFIG_VERSION_FIELD_NAME.getFieldName(),
                            "Response to replSetHeartbeat missing required \""
                            + CONFIG_VERSION_FIELD_NAME + "\" field even though "
                            + "initialized"
                    );
                }
                else {
                    configVersion = 0;
                }
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + CONFIG_VERSION_FIELD_NAME
                        + "\" field in response to replSetHeartbeat to have "
                        + "type NumberInt, but found " + ex.getFoundType().toString().toLowerCase(Locale.ROOT));
            }

            String hbMsg;
            try {
                hbMsg = BsonReaderTool.getString(bson, HB_MESSAGE_FIELD_NAME, "");
                assert hbMsg != null;
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + HB_MESSAGE_FIELD_NAME + "\" field in "
                        + "response to replSetHeartbeat to have type String, but "
                        + "found " + ex.getFoundType()
                );
            }

            HostAndPort syncincTo;
            try {
                syncincTo = BsonReaderTool.getHostAndPort(bson, SYNC_SOURCE_FIELD_NAME, null);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + SYNC_SOURCE_FIELD_NAME
                        + "\" field in response to replSetHeartbeat to have type "
                        + "String, but found " + ex.getFoundType()
                );
            }

            ReplicaSetConfig config;
            BsonDocument uncastedConf;
            try {
                uncastedConf = BsonReaderTool.getDocument(bson, CONFIG_FIELD_NAME, null);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + CONFIG_FIELD_NAME + "\" in "
                        + "response to replSetHeartbeat to have type Object, but "
                        + "found " + ex.getFoundType());
            }
            if (uncastedConf == null) {
                config = null;
            }
            else {
                config = ReplicaSetConfig.fromDocument(uncastedConf);
            }

            return new ReplSetHeartbeatReply(
                    electionTime,
                    time,
                    opTime,
                    electable,
                    hasData,
                    mismatch,
                    isReplSet,
                    stateDisagreement,
                    state,
                    configVersion,
                    setName,
                    hbMsg,
                    syncincTo,
                    config
            );
        }
    }

}
