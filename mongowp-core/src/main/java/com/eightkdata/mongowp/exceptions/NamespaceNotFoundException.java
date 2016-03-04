
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class NamespaceNotFoundException extends MongoException {
    private static final long serialVersionUID = 1L;

    private final String database;
    private final String collection;

    public NamespaceNotFoundException(String database, String collection) {
        super(ErrorCode.NAMESPACE_NOT_FOUND, database + '.' + collection);
        this.database = database;
        this.collection = collection;
    }

    public NamespaceNotFoundException(String database, String collection, Throwable cause) {
        super(cause, ErrorCode.NAMESPACE_NOT_FOUND, database + '.' + collection);
        this.database = database;
        this.collection = collection;
    }

    public NamespaceNotFoundException(String database, String collection, String customMessage) {
        super(customMessage, ErrorCode.NAMESPACE_NOT_FOUND);
        this.database = database;
        this.collection = collection;
    }

    public NamespaceNotFoundException(String database, String collection, String customMessage, Throwable cause) {
        super(customMessage, cause, ErrorCode.NAMESPACE_NOT_FOUND);
        this.database = database;
        this.collection = collection;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

}
