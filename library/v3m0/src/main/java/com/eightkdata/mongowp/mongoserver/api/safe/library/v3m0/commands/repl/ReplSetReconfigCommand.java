package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import javax.annotation.concurrent.Immutable;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.InvalidReplicaSetConfigException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetReconfigCommand.ReplSetReconfigArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.server.api.impl.AbstractNotAliasableCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonReaderTool;

/**
 *
 */
public class ReplSetReconfigCommand extends AbstractNotAliasableCommand<ReplSetReconfigArgument, Empty>{

	private static final String COMMAND_FIELD_NAME = "replSetReconfig";
	
    public static final ReplSetReconfigCommand INSTANCE = new ReplSetReconfigCommand();

    private ReplSetReconfigCommand() {
        super(COMMAND_FIELD_NAME);
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
            throws InvalidReplicaSetConfigException, BadValueException, TypesMismatchException {
    	return ReplSetReconfigArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(ReplSetReconfigArgument request) {
        throw new UnsupportedOperationException("Not supported.");
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
        private static final BooleanField FORCE_FIELD = new BooleanField("force");

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

        private static ReplSetReconfigArgument unmarshall(BsonDocument doc) 
        		throws BadValueException, InvalidReplicaSetConfigException, TypesMismatchException {
            if (doc.get(COMMAND_FIELD_NAME).getType().equals(BsonType.DOCUMENT)) {
                throw new BadValueException("no configuration specified");
            }
            ReplicaSetConfig config;
            try {
                config = ReplicaSetConfig.fromDocument(
                        BsonReaderTool.getDocument(doc, COMMAND_FIELD_NAME)
                );
            } catch (MongoException ex) {
                throw new InvalidReplicaSetConfigException(ex.getLocalizedMessage(), ex);
            }
            boolean force = BsonReaderTool.getBoolean(doc, FORCE_FIELD, false);

            return new ReplSetReconfigArgument(config, force);
        }
    }

}
