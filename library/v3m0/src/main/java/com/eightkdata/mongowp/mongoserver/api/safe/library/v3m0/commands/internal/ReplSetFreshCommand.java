package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.DateTimeField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetFreshCommand.ReplSetFreshArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetFreshCommand.ReplSetFreshReply;
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

    private static final BooleanField FRESHER_FIELD = new BooleanField("fresher");
    private static final StringField INFO_FIELD = new StringField("fresher");
    //TODO: CHECK IF THIS FIELD SHOULD BE A TIMESTAMP
    private static final DateTimeField OPTIME_FIELD = new DateTimeField("optime");
    private static final BooleanField VETO_FIELD = new BooleanField("veto");

    @Override
    public BsonDocument marshallResult(ReplSetFreshReply reply) {

        BsonDocumentBuilder result = new BsonDocumentBuilder();

        result.append(FRESHER_FIELD, reply.isWeAreFresher());
        if (reply.getInfo() != null) {
            result.append(INFO_FIELD, reply.getInfo());
        }
        result.append(OPTIME_FIELD, reply.getOpTime());
        result.append(VETO_FIELD, reply.isDoVeto());

        return result.build();
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
        private final Instant opTime;

        public ReplSetFreshArgument(
                String setName,
                HostAndPort who,
                int clientId,
                long cfgVersion,
                Instant opTime) {
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
        public Instant getOpTime() {
            return opTime;
        }

        public static ReplSetFreshArgument fromDocument(BsonDocument bson) throws
                TypesMismatchException, NoSuchKeyException {
            int clientId = BsonReaderTool.getInteger(bson, ID_FIELD_NAME);
            String setName = BsonReaderTool.getString(bson, SET_NAME_FIELD_NAME);
            HostAndPort who = BsonReaderTool.getHostAndPort(bson, WHO_FIELD_NAME);
            long cfgver = BsonReaderTool.getNumeric(bson, CFG_VER_FIELD_NAME).longValue();
            Instant optime = BsonReaderTool.getInstant(bson, OPTIME_FIELD_NAME);

            return new ReplSetFreshArgument(setName, who, clientId, cfgver, optime);
        }

    }

    public static class ReplSetFreshReply {

        private final String info;
        private final Instant opTime;
        private final boolean weAreFresher;
        private final boolean doVeto;

        public ReplSetFreshReply(
                @Nullable String info,
                @Nonnull Instant opTime,
                boolean weAreFresher,
                boolean doVeto) {
            this.info = info;
            this.opTime = opTime;
            this.weAreFresher = weAreFresher;
            this.doVeto = doVeto;
        }

        @Nullable
        public String getInfo() {
            return info;
        }

        @Nonnull
        public Instant getOpTime() {
            return opTime;
        }

        public boolean isWeAreFresher() {
            return weAreFresher;
        }

        public boolean isDoVeto() {
            return doVeto;
        }

    }

}
