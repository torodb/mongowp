
package com.eightkdata.mongowp.mongoserver.api.impl;

import com.eightkdata.mongowp.mongoserver.api.Command;
import com.eightkdata.mongowp.mongoserver.api.CommandReply;
import com.eightkdata.mongowp.mongoserver.api.CommandResult;
import com.eightkdata.mongowp.mongoserver.api.MarshalException;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.exceptions.FailedToParseException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.google.common.base.Preconditions;
import org.bson.BsonDocument;

import static com.eightkdata.mongowp.mongoserver.api.CommandReply.*;

/**
 *
 */
public class CorrectCommandReply<R> implements CommandReply<R>{

    private final Command<?, ? super R> command;
    private final CommandResult<R> result;

    public CorrectCommandReply(Command<?, ? super R> command, CommandResult<R> result) {
        Preconditions.checkArgument(!command.isReadyToReplyResult(result.getResult()));
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
        assert !command.isReadyToReplyResult(result.getResult());
        BsonDocument marshalled;
        try {
            marshalled = command.marshallResult(result.getResult());
        } catch (MarshalException ex) {
            throw new FailedToParseException(ex.getMessage());
        }
        if (marshalled == null) {
            marshalled = new BsonDocument();
        }
        return new BsonDocumentBuilder(marshalled)
            .append(OK_FIELD, MongoConstants.OK)
            .build();
    }

}
