package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.FailedToParseException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.mongoserver.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetReconfigCommand.ReplSetReconfigArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonReaderTool;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonType;

/**
 *
 */
public class ReplSetReconfigCommand extends AbstractCommand<ReplSetReconfigArgument, Empty>{

    public static final ReplSetReconfigCommand INSTANCE = new ReplSetReconfigCommand();

    private ReplSetReconfigCommand() {
        super("replSetReconfig");
    }

    @Override
    public Class<? extends ReplSetReconfigArgument> getArgClass() {
        return ReplSetReconfigArgument.class;
    }

    @Override
    public boolean canChangeReplicationState() {
        return true;
    }

    @Override
    public ReplSetReconfigArgument unmarshallArg(BsonDocument requestDoc)
            throws BadValueException, TypesMismatchException, NoSuchKeyException, FailedToParseException {
        if (requestDoc.get(getCommandName()).getBsonType().equals(BsonType.DOCUMENT)) {
            throw new BadValueException("no configuration specified");
        }
        ReplicaSetConfig config = ReplicaSetConfig.fromDocument(requestDoc.getDocument(getCommandName()));
        boolean force = BsonReaderTool.getBoolean(requestDoc, "force", false);

        return new ReplSetReconfigArgument(config, force);
    }

    @Override
    public BsonDocument marshallArg(ReplSetReconfigArgument request) {
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

    @Immutable
    public static class ReplSetReconfigArgument {

        private final ReplicaSetConfig config;
        private final boolean force;

        public ReplSetReconfigArgument(ReplicaSetConfig config, boolean force) {
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
