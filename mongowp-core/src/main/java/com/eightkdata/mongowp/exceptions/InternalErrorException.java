
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class InternalErrorException extends MongoException {
    private static final long serialVersionUID = 1L;

    public InternalErrorException(String customMessage) {
        super(customMessage, ErrorCode.INTERNAL_ERROR);
    }

    public InternalErrorException(String customMessage, Throwable cause) {
        super(customMessage, cause, ErrorCode.INTERNAL_ERROR);
    }

    public InternalErrorException(Object... args) {
        super(ErrorCode.INTERNAL_ERROR, args);
    }

    public InternalErrorException(Throwable cause) {
        super(cause, ErrorCode.INTERNAL_ERROR);
    }

}
