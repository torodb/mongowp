
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.server.api.tools.Empty;

/**
 * Returns the current replica set configuration.
 */
public class ReplSetGetConfigCommand extends AbstractCommand<Empty, ReplicaSetConfig> {

    public static final ReplSetGetConfigCommand INSTANCE = new ReplSetGetConfigCommand();

    private ReplSetGetConfigCommand() {
        super("replSetGetConfig");
    }

    @Override
    public boolean isReadyToReplyResult(ReplicaSetConfig r) {
        //it seems this command returns a document whose only key is "config"
        return true;
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
    public Class<? extends ReplicaSetConfig> getResultClass() {
        return ReplicaSetConfig.class;
    }

    @Override
    public BsonDocument marshallResult(ReplicaSetConfig reply) {
        return DefaultBsonValues.newDocument("config", reply.toBSON());
    }

    @Override
    public ReplicaSetConfig unmarshallResult(BsonDocument resultDoc) throws
            MongoException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

}
