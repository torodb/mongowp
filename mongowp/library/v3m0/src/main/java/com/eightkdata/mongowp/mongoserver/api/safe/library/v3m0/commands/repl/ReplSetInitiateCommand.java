package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetInitiateCommand.ReplSetInitiateArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import org.bson.BsonDocument;

/**
 *
 */
public class ReplSetInitiateCommand extends AbstractCommand<ReplSetInitiateArgument, SimpleReply>{

    public static final ReplSetInitiateCommand INSTANCE = new ReplSetInitiateCommand();

    private ReplSetInitiateCommand() {
        super("replSetInitiate");
    }

    @Override
    public Class<? extends ReplSetInitiateArgument> getArgClass() {
        return ReplSetInitiateArgument.class;
    }

    @Override
    public ReplSetInitiateArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        BsonDocument configDoc = BsonReaderTool.getDocument(requestDoc, getCommandName());
        return new ReplSetInitiateArgument(this, ReplicaSetConfig.fromDocument(configDoc));
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

    public static class ReplSetInitiateArgument extends SimpleArgument {

        private final ReplicaSetConfig config;

        public ReplSetInitiateArgument(Command command, ReplicaSetConfig config) {
            super(command);
            this.config = config;
        }

        public ReplicaSetConfig getConfig() {
            return config;
        }
    }

}
