
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.CommandReply;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.server.callback.WriteOpResult;
import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.MongoException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.eightkdata.mongowp.server.api.CommandReply.ERR_MSG_FIELD;
import static com.eightkdata.mongowp.server.api.CommandReply.OK_FIELD;

/**
 *
 */
public class FailedCommandReply<R> implements CommandReply<R> {

    private final MongoException exception;
    private final WriteOpResult writeOpResult;

    public FailedCommandReply(@Nonnull MongoException exception) {
        this(exception, null);
    }

    public FailedCommandReply(@Nonnull MongoException exception, @Nullable WriteOpResult writeOpResult) {
        this.exception = exception;
        this.writeOpResult = writeOpResult;
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public ErrorCode getErrorCode() {
        return exception.getErrorCode();
    }

    @Override
    public String getErrorMessage() throws IllegalStateException {
        return exception.getMessage();
    }

    @Override
    public MongoException getErrorAsException() throws IllegalStateException {
        return exception;
    }

    @Override
    public WriteOpResult getWriteOpResult() {
        return writeOpResult;
    }

    @Override
    public R getResult() throws IllegalStateException {
        throw new IllegalStateException("The command did not finish correctly");
    }

    @Override
    public BsonDocument marshall() {
        return new BsonDocumentBuilder()
                .append(ERR_MSG_FIELD, getErrorMessage())
                .append(OK_FIELD, MongoConstants.KO)
                .build();
    }

}
