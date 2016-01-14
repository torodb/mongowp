
package com.eightkdata.mongowp.mongoserver.protocol.exceptions;

import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;

/**
 *
 */
public class IllegalOperationException extends MongoException {

    private static final long serialVersionUID = 1L;

    int opCode;

    public IllegalOperationException(int opCode) {
        super(ErrorCode.ILLEGAL_OPERATION);
        this.opCode = opCode;
    }

    public int getOpCode() {
        return opCode;
    }

}
