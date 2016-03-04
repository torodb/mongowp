
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class InitialSyncFailure extends MongoException {
    private static final long serialVersionUID = 1L;

    public InitialSyncFailure(String customMessage) {
        super(customMessage, ErrorCode.INITIAL_SYNC_FAILURE);
    }

    public InitialSyncFailure(String customMessage, Throwable cause) {
        super(customMessage, cause, ErrorCode.INITIAL_SYNC_FAILURE);
    }

    public InitialSyncFailure() {
        super(ErrorCode.INITIAL_SYNC_FAILURE);
    }

    public InitialSyncFailure(Throwable cause) {
        super(cause, ErrorCode.INITIAL_SYNC_FAILURE);
    }

}
