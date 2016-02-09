
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class NotMasterOrSecondaryException extends MongoException {
    private static final long serialVersionUID = 1L;

    public NotMasterOrSecondaryException() {
        super(ErrorCode.NOT_MASTER_OR_SECONDARY_CODE);
    }

    public NotMasterOrSecondaryException(String customMessage) {
        super(customMessage, ErrorCode.NOT_MASTER_OR_SECONDARY_CODE);
    }

    public NotMasterOrSecondaryException(String customMessage, Throwable cause) {
        super(customMessage, cause, ErrorCode.NOT_MASTER_OR_SECONDARY_CODE);
    }

    public NotMasterOrSecondaryException(Object... args) {
        super(ErrorCode.NOT_MASTER_OR_SECONDARY_CODE, args);
    }

    public NotMasterOrSecondaryException(Throwable cause, Object... args) {
        super(cause, ErrorCode.NOT_MASTER_OR_SECONDARY_CODE, args);
    }

}
