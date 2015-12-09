
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetElectCommand.ReplSetElectArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetElectCommand.ReplSetElectReply;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
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
    public ReplSetElectArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return ReplSetElectArgument.fromDocument(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(ReplSetElectArgument request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends ReplSetElectReply> getResultClass() {
        return ReplSetElectReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetElectReply reply) {

        BsonDocument doc = new BsonDocument();

        doc.put(ReplSetElectReply.VOTE_FIELD_NAME, new BsonInt32(reply.getVote()));
        doc.put(ReplSetElectReply.ROUND_FIELD_NAME, new BsonObjectId(reply.getRound()));
        return doc;
    }

    @Override
    public ReplSetElectReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    public static class ReplSetElectArgument {

        private static final String SET_NAME_FIELD_NAME = "set";
        private static final String CLIENT_ID_FIELD_NAME = "whoid";
        private static final String CFG_VER_FIELD_NAME = "cfgver";
        private static final String ROUND_FIELD_NAME = "round";

        private final @Nonnull String setName;
        private final int clientId;  
        private final int cfgVersion;
        private final @Nonnull ObjectId round;

        public ReplSetElectArgument(
                @Nonnull String setName,
                int clientId,
                int cfgVersion,
                @Nonnull ObjectId round) {
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

        public static ReplSetElectArgument fromDocument(
                BsonDocument bson) throws TypesMismatchException, NoSuchKeyException {
            String setName = BsonReaderTool.getString(bson, SET_NAME_FIELD_NAME, "");
            int cliendId = BsonReaderTool.getInteger(bson, CLIENT_ID_FIELD_NAME);
            int cfgversion = BsonReaderTool.getNumeric(bson, CFG_VER_FIELD_NAME).intValue();
            ObjectId round = BsonReaderTool.getObjectId(bson, ROUND_FIELD_NAME);

            return new ReplSetElectArgument(setName, cliendId, cfgversion, round);
        }

    }
    public static class ReplSetElectReply {

        private static final String VOTE_FIELD_NAME = "vote";
        private static final String ROUND_FIELD_NAME = "round";

        private final int vote;
        private final ObjectId round;

        public ReplSetElectReply(int vote, ObjectId round) {
            this.vote = vote;
            this.round = round;
        }

        public int getVote() {
            return vote;
        }

        public ObjectId getRound() {
            return round;
        }
    }

}
