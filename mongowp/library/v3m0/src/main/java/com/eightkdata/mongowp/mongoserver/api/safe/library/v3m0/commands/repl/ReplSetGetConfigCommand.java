
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetGetConfigCommand.ReplSetGetConfigReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import org.bson.BsonDocument;

/**
 * Returns the current replica set configuration.
 */
public class ReplSetGetConfigCommand extends AbstractCommand<SimpleArgument, ReplSetGetConfigReply> {

    public static final ReplSetGetConfigCommand INSTANCE = new ReplSetGetConfigCommand();

    private ReplSetGetConfigCommand() {
        super("replSetGetConfig");
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
    public Class<? extends ReplSetGetConfigReply> getReplyClass() {
        return ReplSetGetConfigReply.class;
    }

    @Override
    public BsonDocument marshallReply(ReplSetGetConfigReply reply) throws
            MongoServerException {
        BsonDocument doc;
        if (reply.isOk()) { //it seems this command returns a document whose only key is "config"
            doc = new BsonDocument("config", reply.getConfig().toBSON());
        }
        else {
            doc = SimpleReplyMarshaller.marshall(reply);
        }

        return doc;
    }

    public static class ReplSetGetConfigReply extends SimpleReply {

        private final ReplicaSetConfig config;

        public ReplSetGetConfigReply(Command command, ReplicaSetConfig config) {
            super(command);
            this.config = config;
        }

        public ReplicaSetConfig getConfig() {
            return config;
        }

    }

}
