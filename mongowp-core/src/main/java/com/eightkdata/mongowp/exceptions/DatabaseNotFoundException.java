
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class DatabaseNotFoundException extends MongoException {
    private static final long serialVersionUID = 1L;
    private final String database;

    public DatabaseNotFoundException(String database, String customMessage) {
        super(customMessage, ErrorCode.DATABASE_NOT_FOUND);
        this.database = database;
    }

    public DatabaseNotFoundException(String database, String customMessage, Throwable cause) {
        super(customMessage, cause, ErrorCode.DATABASE_NOT_FOUND);
        this.database = database;
    }

    public DatabaseNotFoundException(String database) {
        super(ErrorCode.DATABASE_NOT_FOUND, database);
        this.database = database;
    }

    public DatabaseNotFoundException(String database, Throwable cause) {
        super(cause, ErrorCode.DATABASE_NOT_FOUND, database);
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }
}
