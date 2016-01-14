
package com.eightkdata.mongowp.mongoserver.protocol.exceptions;

import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;

/**
 *
 */
public class OperationFailedException extends MongoException {

    private static final long serialVersionUID = 1L;

    private final String reason;

    public OperationFailedException() {
        super(ErrorCode.OPERATION_FAILED);
        this.reason = "not specified";
    }

    public OperationFailedException(String reason) {
        super(ErrorCode.OPERATION_FAILED, reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

}
