package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;

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
        return new BsonDocument(getCommandName(), BsonBoolean.valueOf(request));
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
