
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandReply;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import org.bson.BsonDocument;

/**
 *
 */
public abstract class AbstractCommand<Arg extends CommandArgument, Rep extends CommandReply> implements Command<Arg, Rep> {

    private final String commandName;

    public AbstractCommand(String commandName) {
        this.commandName = commandName;
    }
    
    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public boolean isAdminOnly() {
        return false;
    }

    @Override
    public boolean isSlaveOk() {
        return false;
    }

    @Override
    public boolean isSlaveOverrideOk() {
        return false;
    }

    @Override
    public boolean shouldAffectCommandCounter() {
        return true;
    }

    @Override
    public boolean isAllowedOnMaintenance() {
        return true;
    }

    @Override
    public BsonDocument marshallArg(Arg request) 
            throws MongoServerException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Rep unmarshallReply(BsonDocument replyDoc) 
            throws MongoServerException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported.");
    }
}
