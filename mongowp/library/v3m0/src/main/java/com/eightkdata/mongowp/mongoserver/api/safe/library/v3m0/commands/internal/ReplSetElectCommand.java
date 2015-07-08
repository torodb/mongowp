
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetElectCommand.ReplSetElectArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetElectCommand.ReplSetElectReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import javax.annotation.Nonnull;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonObjectId;
import org.bson.types.ObjectId;

/**
 *
 */
public class ReplSetElectCommand extends AbstractCommand<ReplSetElectArgument, ReplSetElectReply>{

    public static final ReplSetElectCommand INSTANCE = new ReplSetElectCommand();

    private ReplSetElectCommand() {
        super("replSetElect");
    }

    @Override
    public Class<? extends ReplSetElectArgument> getArgClass() {
        return ReplSetElectArgument.class;
    }

    @Override
    public ReplSetElectArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        return ReplSetElectArgument.fromDocument(requestDoc, this);
    }

    @Override
    public Class<? extends ReplSetElectReply> getReplyClass() {
        return ReplSetElectReply.class;
    }

    @Override
    public BsonDocument marshallReply(ReplSetElectReply reply) throws
            MongoServerException {

        BsonDocument doc = SimpleReplyMarshaller.marshall(reply);

        if (reply.isOk()) {
            doc.put(ReplSetElectReply.VOTE_FIELD_NAME, new BsonInt32(reply.getVote()));
            doc.put(ReplSetElectReply.ROUND_FIELD_NAME, new BsonObjectId(reply.getRound()));
        }
        return doc;
    }

    public static class ReplSetElectArgument extends SimpleArgument {

        private static final String SET_NAME_FIELD_NAME = "set";
        private static final String CLIENT_ID_FIELD_NAME = "whoid";
        private static final String CFG_VER_FIELD_NAME = "cfgver";
        private static final String ROUND_FIELD_NAME = "round";

        private final @Nonnull String setName;
        private final int clientId;  
        private final int cfgVersion;
        private final @Nonnull ObjectId round;

        public ReplSetElectArgument(
                ReplSetElectCommand command,
                @Nonnull String setName,
                int clientId,
                int cfgVersion,
                @Nonnull ObjectId round) {
            super(command);
            this.setName = setName;
            this.clientId = clientId;
            this.cfgVersion = cfgVersion;
            this.round = round;
        }

        /**
         * The name of the set
         * @return 
         */
        @Nonnull
        public String getSetName() {
            return setName;
        }

        /**
         * @return replSet id of the member that sent the replSetFresh command
         */
        public int getClientId() {
            return clientId;
        }

        /**
         * 
         * @return replSet config version that the member who sent the command thinks it has
         */
        public int getCfgVersion() {
            return cfgVersion;
        }

        /**
         * 
         * @return unique ID for this election
         */
        @Nonnull
        public ObjectId getRound() {
            return round;
        }

        public static ReplSetElectArgument fromDocument(BsonDocument bson, ReplSetElectCommand command) throws TypesMismatchException, NoSuchKeyException {
            String setName = BsonReaderTool.getString(bson, SET_NAME_FIELD_NAME, "");
            int cliendId = BsonReaderTool.getInteger(bson, CLIENT_ID_FIELD_NAME);
            int cfgversion = BsonReaderTool.getNumeric(bson, CFG_VER_FIELD_NAME).intValue();
            ObjectId round = BsonReaderTool.getObjectId(bson, ROUND_FIELD_NAME);

            return new ReplSetElectArgument(command, setName, cliendId, cfgversion, round);
        }

    }
    public static class ReplSetElectReply extends SimpleReply {

        private static final String VOTE_FIELD_NAME = "vote";
        private static final String ROUND_FIELD_NAME = "round";

        private final int vote;
        private final ObjectId round;

        public ReplSetElectReply(ReplSetElectCommand command, int vote, ObjectId round) {
            super(command);
            this.vote = vote;
            this.round = round;
        }

        public ReplSetElectReply(ReplSetElectCommand command, MongoWP.ErrorCode errorCode, String errorMessage) {
            super(command, errorCode, errorMessage);
            this.vote = 0;
            this.round = null;
        }

        public ReplSetElectReply(ReplSetElectCommand command, MongoWP.ErrorCode errorCode, Object... args) {
            super(command, errorCode, args);
            this.vote = 0;
            this.round = null;
        }

        public ReplSetElectReply(ReplSetElectCommand command, BsonDocument bson) throws TypesMismatchException,
                NoSuchKeyException {
            super(command, bson);
            this.vote = BsonReaderTool.getInteger(bson, VOTE_FIELD_NAME);
            this.round = BsonReaderTool.getObjectId(bson, ROUND_FIELD_NAME);
        }

        public int getVote() {
            if (!getErrorCode().equals(MongoWP.ErrorCode.OK)) {
                throw new IllegalStateException("There is no vote when "
                    + "replSetElect produces an error");
            }
            return vote;
        }

        public ObjectId getRound() {
            if (!getErrorCode().equals(MongoWP.ErrorCode.OK)) {
                throw new IllegalStateException("There is no round when "
                    + "replSetElect produces an error");
            }
            return round;
        }
    }

}
