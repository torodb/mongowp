
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class InvalidNamespaceException extends MongoException {

    private static final long serialVersionUID = 1L;

    private final String namespace;

    public InvalidNamespaceException(String namespace) {
        super(ErrorCode.INVALID_NAMESPACE, namespace, "unspecified");
        this.namespace = namespace;
    }

    public InvalidNamespaceException(String namespace, String reason) {
        super(ErrorCode.INVALID_NAMESPACE, namespace, reason);
        this.namespace = namespace;
    }

    public String getNamespace() {
        return namespace;
    }
}
