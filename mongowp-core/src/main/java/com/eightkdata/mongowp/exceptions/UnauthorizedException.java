
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class UnauthorizedException extends MongoException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String customMessage) {
        super(customMessage, ErrorCode.UNAUTHORIZED);
    }

}
