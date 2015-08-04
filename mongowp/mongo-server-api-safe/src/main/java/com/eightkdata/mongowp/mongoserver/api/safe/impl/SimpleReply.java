package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandReply;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import java.text.MessageFormat;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.BsonDocument;

/**
 *
 */
public class SimpleReply implements CommandReply {
    @Nonnull
    private final Command command;
    @Nonnull
    private final MongoWP.ErrorCode errorCode;
    private final @Nullable String errorMessage;

    public SimpleReply(@Nonnull Command command) {
        this.command = command;
        this.errorCode = MongoWP.ErrorCode.OK;
        this.errorMessage = null;
    }

    public SimpleReply(@Nonnull Command command, @Nonnull MongoWP.ErrorCode errorCode, @Nonnull String errorMessage) {
        this.command = command;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public SimpleReply(@Nonnull Command command, @Nonnull MongoWP.ErrorCode errorCode, Object... args) {
        this.command = command;
        this.errorCode = errorCode;
        this.errorMessage = MessageFormat.format(errorCode.getErrorMessage(), args);
    }

    public SimpleReply(@Nonnull Command command, BsonDocument bson) throws
            TypesMismatchException, NoSuchKeyException {
        this.command = command;
        this.errorCode
                = MongoWP.ErrorCode.fromErrorCode(BsonReaderTool.getNumeric(bson, "ok").intValue());
        this.errorMessage = BsonReaderTool.getString(bson, "errmsg", null);
    }

    @Override
    public MongoWP.ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean isOk() {
        return errorCode.equals(MongoWP.ErrorCode.OK);
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Nullable
    public String getErrorMessage() {
        if (errorCode.equals(MongoWP.ErrorCode.OK)) {
            return null;
        }
        return errorMessage;
    }

    @Override
    public WriteOpResult getWriteOpResult() {
        return null;
    }
}
