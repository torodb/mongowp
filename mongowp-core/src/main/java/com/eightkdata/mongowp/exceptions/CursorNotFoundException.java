
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class CursorNotFoundException extends MongoException {
    private static final long serialVersionUID = 1L;

    private final long cursorId;

    public CursorNotFoundException(String customMessage, long cursorId) {
        super(customMessage, ErrorCode.CURSOR_NOT_FOUND);
        this.cursorId = cursorId;
    }

    public CursorNotFoundException(long cursorId) {
        super(ErrorCode.CURSOR_NOT_FOUND, cursorId);
        this.cursorId = cursorId;
    }

    public long getCursorId() {
        return cursorId;
    }

}
