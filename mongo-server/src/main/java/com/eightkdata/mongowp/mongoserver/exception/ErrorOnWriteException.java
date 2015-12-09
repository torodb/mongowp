
package com.eightkdata.mongowp.mongoserver.exception;

import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoException;
import com.google.common.base.Preconditions;

/**
 *
 */
public class ErrorOnWriteException extends MongoException {
    private static final long serialVersionUID = 1L;

    private final WriteOpResult writeOpResult;

    public ErrorOnWriteException(WriteOpResult writeOpResult, String customMessage) {
        super(customMessage, writeOpResult.getErrorCode());
        Preconditions.checkArgument(writeOpResult.errorOcurred(),
                "trying to create an " + getClass().getName() +" with a "
                        + "WriteOpResult that does not contain an error!");
        this.writeOpResult = writeOpResult;
    }

    public WriteOpResult getWriteOpResult() {
        return writeOpResult;
    }
}
