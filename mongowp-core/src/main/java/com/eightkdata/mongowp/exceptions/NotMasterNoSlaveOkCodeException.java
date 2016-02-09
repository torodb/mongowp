
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class NotMasterNoSlaveOkCodeException extends MongoException {
    private static final long serialVersionUID = 1L;

    public NotMasterNoSlaveOkCodeException(String customMessage) {
        super(customMessage, ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
    }

    public NotMasterNoSlaveOkCodeException(String customMessage, Throwable cause) {
        super(customMessage, cause, ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
    }

    public NotMasterNoSlaveOkCodeException() {
        super(ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
    }

    public NotMasterNoSlaveOkCodeException(Throwable cause) {
        super(cause, ErrorCode.NOT_MASTER_NO_SLAVE_OK_CODE);
    }
}
