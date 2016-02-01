package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.bson.BsonBoolean;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonReaderTool;

/**
 *
 */
public class ReplSetMaintenanceCommand extends AbstractCommand<Boolean, Empty>{

    public static final ReplSetMaintenanceCommand INSTANCE = new ReplSetMaintenanceCommand();

    private ReplSetMaintenanceCommand() {
        super("replSetMaintenance");
    }

    @Override
    public Class<? extends Boolean> getArgClass() {
        return Boolean.class;
    }

    @Override
    public boolean canChangeReplicationState() {
        return true;
    }

    @Override
    public Boolean unmarshallArg(BsonDocument requestDoc) throws TypesMismatchException, NoSuchKeyException {
        return BsonReaderTool.getBoolean(requestDoc, getCommandName());
    }

    @Override
    public BsonDocument marshallArg(Boolean request) {
        return DefaultBsonValues.newDocument(getCommandName(), DefaultBsonValues.newBoolean(request));
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
