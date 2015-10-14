
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetGetRBIDCommand.ReplSetGetRBIDReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import org.bson.BsonDocument;

/**
 *
 */
public class ReplSetGetRBIDCommand extends AbstractCommand<Empty, ReplSetGetRBIDReply>{

    public static final ReplSetGetRBIDCommand INSTANCE = new ReplSetGetRBIDCommand();

    private ReplSetGetRBIDCommand() {
        super("replSetGetRBID");
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
    public Class<? extends ReplSetGetRBIDReply> getResultClass() {
        return ReplSetGetRBIDReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetGetRBIDReply reply) {
        return reply.marshall();
    }

    @Override
    public ReplSetGetRBIDReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    public static class ReplSetGetRBIDReply {
        private static final BsonField<Integer> RBID_FIELD = BsonField.create("rbid");

        private final int rbid;

        public ReplSetGetRBIDReply(int rbid) {
            this.rbid = rbid;
        }

        public int getRBID() {
            return rbid;
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            builder.append(RBID_FIELD, rbid);
            return builder.build();
        }
    }

}
