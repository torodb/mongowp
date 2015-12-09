
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandReply;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandResult;
import com.eightkdata.mongowp.mongoserver.api.safe.MarshalException;
import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.FailedToParseException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoException;
import com.google.common.base.Preconditions;
import org.bson.BsonDocument;

/**
 *
 */
public class DelegateCommandReply<R> implements CommandReply<R>{

    private final Command<?, ? super R> command;
    private final CommandResult<R> result;

    public DelegateCommandReply(Command<?, ? super R> command, CommandResult<R> result) {
        Preconditions.checkArgument(command.isReadyToReplyResult(result.getResult()));
        this.command = command;
        this.result = result;
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.OK;
    }

    @Override
    public WriteOpResult getWriteOpResult() {
        return result.getWriteOpResult();
    }

    @Override
    public String getErrorMessage() throws IllegalStateException {
        throw new IllegalStateException("The command finished correctly");
    }

    @Override
    public MongoException getErrorAsException() throws IllegalStateException {
        throw new IllegalStateException("The command finished correctly");
    }

    @Override
    public R getResult() {
        return result.getResult();
    }

    @Override
    public BsonDocument marshall() throws MongoException {
        BsonDocument doc;
        assert command.isReadyToReplyResult(result.getResult());
        try {
            doc = command.marshallResult(result.getResult());
        } catch (MarshalException ex) {
            throw new FailedToParseException(ex.getMessage());
        }
        assert doc != null;
        return doc;
    }

}