
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

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
