
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetGetRBIDCommand.ReplSetGetRBIDReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import org.bson.BsonDocument;

/**
 *
 */
public class ReplSetGetRBIDCommand extends AbstractCommand<SimpleArgument, ReplSetGetRBIDReply>{

    public static final ReplSetGetRBIDCommand INSTANCE = new ReplSetGetRBIDCommand();

    private ReplSetGetRBIDCommand() {
        super("replSetGetRBID");
    }

    @Override
    public Class<? extends SimpleArgument> getArgClass() {
        return SimpleArgument.class;
    }

    @Override
    public SimpleArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        return new SimpleArgument(this);
    }

    @Override
    public Class<? extends ReplSetGetRBIDReply> getReplyClass() {
        return ReplSetGetRBIDReply.class;
    }

    @Override
    public BsonDocument marshallReply(ReplSetGetRBIDReply reply) throws
            MongoServerException {
        return reply.marshall();
    }

    public static class ReplSetGetRBIDReply extends SimpleReply {
        private static final BsonField<Integer> RBID_FIELD = BsonField.create("rbid");

        private final int rbid;

        public ReplSetGetRBIDReply(ReplSetGetRBIDCommand command, int rbid) {
            super(command);
            this.rbid = rbid;
        }

        public ReplSetGetRBIDReply(ReplSetGetRBIDCommand command, MongoWP.ErrorCode errorCode, String errorMessage) {
            super(command, errorCode, errorMessage);
            this.rbid = 0;
        }

        public ReplSetGetRBIDReply(ReplSetGetRBIDCommand command, MongoWP.ErrorCode errorCode, Object... args) {
            super(command, errorCode, args);
            this.rbid = 0;
        }

        public int getRBID() {
            if (getErrorCode().equals(MongoWP.ErrorCode.OK)) {
                return rbid;
            }
            throw new IllegalStateException("There is no rollback id when "
                    + "replSetGetRBID produces an error");
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            SimpleReplyMarshaller.marshall(this, builder);

            if (isOk()) {
                builder.append(RBID_FIELD, rbid);
            }
            return builder.build();
        }
    }

}
