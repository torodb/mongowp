package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.DateTimeField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetFreshCommand.ReplSetFreshArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetFreshCommand.ReplSetFreshReply;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.net.HostAndPort;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.threeten.bp.Instant;

/**
 *
 */
public class ReplSetFreshCommand extends AbstractCommand<ReplSetFreshArgument, ReplSetFreshReply>{

    public static final ReplSetFreshCommand INSTANCE = new ReplSetFreshCommand();

    private ReplSetFreshCommand() {
        super("replSetFresh");
    }

    @Override
    public Class<? extends ReplSetFreshArgument> getArgClass() {
        return ReplSetFreshArgument.class;
    }

    @Override
    public ReplSetFreshArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return ReplSetFreshArgument.fromDocument(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(ReplSetFreshArgument request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends ReplSetFreshReply> getResultClass() {
        return ReplSetFreshReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetFreshReply reply) {
        return reply.marshall();
    }

    @Override
    public ReplSetFreshReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    public static class ReplSetFreshArgument {

        private static final String SET_NAME_FIELD_NAME = "set";
        private static final String WHO_FIELD_NAME = "who";
        private static final String ID_FIELD_NAME = "id";
        private static final String CFG_VER_FIELD_NAME = "cfgver";
        private static final String OPTIME_FIELD_NAME = "optime";

        private final String setName;
        private final HostAndPort who;
        private final int clientId;
        private final long cfgVersion;
        private final OpTime opTime;

        public ReplSetFreshArgument(
                String setName,
                HostAndPort who,
                int clientId,
                long cfgVersion,
                OpTime opTime) {
            this.setName = setName;
            this.who = who;
            this.clientId = clientId;
            this.cfgVersion = cfgVersion;
            this.opTime = opTime;
        }

        /**
         *
         * @return the name of the set
         */
        public String getSetName() {
            return setName;
        }

        /**
         *
         * @return the host and port of the member that sent the request
         */
        public HostAndPort getWho() {
            return who;
        }

        /**
         *
         * @return the repl set of the member that sent the request
         */
        public int getClientId() {
            return clientId;
        }

        /**
         *
         * @return replSet config version that the member who sent the request
         */
        public long getCfgVersion() {
            return cfgVersion;
        }

        /**
         *
         * @return last optime seen by the member who sent the request
         */
        public OpTime getOpTime() {
            return opTime;
        }

        public static ReplSetFreshArgument fromDocument(BsonDocument bson) throws
                TypesMismatchException, NoSuchKeyException {
            int clientId = BsonReaderTool.getInteger(bson, ID_FIELD_NAME);
            String setName = BsonReaderTool.getString(bson, SET_NAME_FIELD_NAME);
            HostAndPort who = BsonReaderTool.getHostAndPort(bson, WHO_FIELD_NAME);
            long cfgver = BsonReaderTool.getNumeric(bson, CFG_VER_FIELD_NAME).longValue();
            Instant optimeInstant = BsonReaderTool.getInstant(bson, OPTIME_FIELD_NAME);

            return new ReplSetFreshArgument(setName, who, clientId, cfgver, new OpTime(optimeInstant));
        }

    }

    public static class ReplSetFreshReply {

        private static final BooleanField FRESHER_FIELD = new BooleanField("fresher");
        private static final StringField INFO_FIELD = new StringField("info");
        private static final StringField ERRMSG_FIELD = new StringField("errmsg");
        //Note: MongoDB usually stores and send optimes as datetimes
        private static final DateTimeField OPTIME_FIELD = new DateTimeField("optime");
        private static final BooleanField VETO_FIELD = new BooleanField("veto");

        private final String info;
        private final String vetoMessage;
        private final OpTime opTime;
        private final boolean weAreFresher;
        private final boolean doVeto;

        /**
         *
         * @param info
         * @param vetoMessage the reason why the votation is vetoed. If null, then this node is not vetoing.
         * @param opTime
         * @param weAreFresher
         */
        public ReplSetFreshReply(
                @Nullable String info,
                @Nullable String vetoMessage,
                @Nonnull OpTime opTime,
                boolean weAreFresher) {
            this.info = info;
            this.vetoMessage = vetoMessage;
            this.opTime = opTime;
            this.weAreFresher = weAreFresher;
            this.doVeto = vetoMessage != null;
        }

        @Nullable
        public String getInfo() {
            return info;
        }

        @Nullable
        public String getVetoMessage() {
            return vetoMessage;
        }

        @Nonnull
        public OpTime getOpTime() {
            return opTime;
        }

        public boolean isWeAreFresher() {
            return weAreFresher;
        }

        public boolean isDoVeto() {
            return doVeto;
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder result = new BsonDocumentBuilder();

            result.append(FRESHER_FIELD, isWeAreFresher());
            if (getInfo() != null) {
                result.append(INFO_FIELD, getInfo());
            }
            if (getVetoMessage() != null) {
                result.append(ERRMSG_FIELD, getVetoMessage());
            }
            result.appendInstant(OPTIME_FIELD, getOpTime().asLong());
            result.append(VETO_FIELD, isDoVeto());

            return result.build();
        }
    }

}
