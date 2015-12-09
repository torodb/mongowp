package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.*;
import org.bson.BsonDocument;

/**
 *
 */
public class ReplSetInitiateCommand extends AbstractCommand<ReplicaSetConfig, Empty>{

    public static final ReplSetInitiateCommand INSTANCE = new ReplSetInitiateCommand();

    private ReplSetInitiateCommand() {
        super("replSetInitiate");
    }

    @Override
    public Class<? extends ReplicaSetConfig> getArgClass() {
        return ReplicaSetConfig.class;
    }

    @Override
    public boolean canChangeReplicationState() {
        return true;
    }

    @Override
    public ReplicaSetConfig unmarshallArg(BsonDocument requestDoc) throws TypesMismatchException, BadValueException, NoSuchKeyException, FailedToParseException {
        ReplicaSetConfig config;
        BsonDocument configDoc;
        configDoc = BsonReaderTool.getDocument(requestDoc, getCommandName());
        config = ReplicaSetConfig.fromDocument(configDoc);
        return config;
    }

    @Override
    public BsonDocument marshallArg(ReplicaSetConfig request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends Empty> getResultClass() {
        return Empty.class;
    }

    @Override
    public BsonDocument marshallResult(Empty reply) {
        return null;
    }

    @Override
    public Empty unmarshallResult(BsonDocument resultDoc) {
        return Empty.getInstance();
    }

}
