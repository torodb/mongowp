
package com.eightkdata.mongowp.mongoserver.protocol.exceptions;

import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;

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
