package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetReconfigCommand.ReplSetReconfigArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonType;

/**
 *
 */
public class ReplSetReconfigCommand extends AbstractCommand<ReplSetReconfigArgument, SimpleReply>{

    public static final ReplSetReconfigCommand INSTANCE = new ReplSetReconfigCommand();

    private ReplSetReconfigCommand() {
        super("replSetReconfig");
    }

    @Override
    public Class<? extends ReplSetReconfigArgument> getArgClass() {
        return ReplSetReconfigArgument.class;
    }

    @Override
    public ReplSetReconfigArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        if (requestDoc.get(getCommandName()).getBsonType().equals(BsonType.DOCUMENT)) {
            throw new BadValueException("no configuration specified");
        }
        ReplicaSetConfig config = ReplicaSetConfig.fromDocument(requestDoc.getDocument(getCommandName()));
        boolean force = BsonReaderTool.getBoolean(requestDoc, "force", false);

        return new ReplSetReconfigArgument(this, config, force);
    }

    @Override
    public Class<? extends SimpleReply> getReplyClass() {
        return SimpleReply.class;
    }

    @Override
    public BsonDocument marshallReply(SimpleReply reply) throws
            MongoServerException {
        return SimpleReplyMarshaller.marshall(reply);
    }

    @Immutable
    public static class ReplSetReconfigArgument extends SimpleArgument {

        private final ReplicaSetConfig config;
        private final boolean force;

        public ReplSetReconfigArgument(ReplSetReconfigCommand command, ReplicaSetConfig config, boolean force) {
            super(command);
            this.config = config;
            this.force = force;
        }

        public ReplicaSetConfig getConfig() {
            return config;
        }

        public boolean isForce() {
            return force;
        }

    }

}
