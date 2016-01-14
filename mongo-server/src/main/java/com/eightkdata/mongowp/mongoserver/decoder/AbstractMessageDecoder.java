
package com.eightkdata.mongowp.mongoserver.decoder;

import com.eightkdata.mongowp.messages.request.RequestMessage;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.InvalidNamespaceException;

/**
 *
 */
public abstract class AbstractMessageDecoder<T extends RequestMessage> implements MessageDecoder<T>{

    protected String getDatabase(String namespace) throws InvalidNamespaceException {
        return namespace.substring(0, getAndCheckFirstDot(namespace));
    }

    protected String getCollection(String namespace) throws InvalidNamespaceException {
        return namespace.substring(getAndCheckFirstDot(namespace) + 1);
    }

    private int getAndCheckFirstDot(String namespace) throws InvalidNamespaceException {
        int firstDot = namespace.indexOf('.');
        if (firstDot == 0) {
            throw new InvalidNamespaceException(namespace, "The first character shall not be a dot");
        }
        if (firstDot < 0) {
            throw new InvalidNamespaceException(namespace, "Does not contain a dot");
        }
        if (firstDot == namespace.length() - 1) {
            throw new InvalidNamespaceException(namespace, "The last character shall not be the first dot");
        }
        return firstDot;
    }
}
