
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class NotSecondaryException extends MongoException {
    private static final long serialVersionUID = 1L;

    public NotSecondaryException() {
        super(ErrorCode.NOT_SECONDARY);
    }

    public NotSecondaryException(String customMessage) {
        super(customMessage, ErrorCode.NOT_SECONDARY);
    }

    public NotSecondaryException(String customMessage, Throwable cause) {
        super(customMessage, cause, ErrorCode.NOT_SECONDARY);
    }

    public NotSecondaryException(Object... args) {
        super(ErrorCode.NOT_SECONDARY, args);
    }

    public NotSecondaryException(Throwable cause, Object... args) {
        super(cause, ErrorCode.NOT_SECONDARY, args);
    }

}
