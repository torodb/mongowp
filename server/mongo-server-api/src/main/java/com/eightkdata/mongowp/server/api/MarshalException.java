
package com.eightkdata.mongowp.server.api;

/**
 *
 */
public class MarshalException extends Exception {
    private static final long serialVersionUID = 1L;

    public MarshalException() {
    }

    public MarshalException(String message) {
        super(message);
    }

    public MarshalException(String message, Throwable cause) {
        super(message, cause);
    }

    public MarshalException(Throwable cause) {
        super(cause);
    }

}
