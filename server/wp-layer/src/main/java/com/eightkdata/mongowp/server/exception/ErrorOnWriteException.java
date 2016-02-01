
package com.eightkdata.mongowp.server.exception;

import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.server.callback.WriteOpResult;
import com.google.common.base.Preconditions;

/**
 *
 */
public class ErrorOnWriteException extends MongoException {
    private static final long serialVersionUID = 1L;

    private transient final WriteOpResult writeOpResult;

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
