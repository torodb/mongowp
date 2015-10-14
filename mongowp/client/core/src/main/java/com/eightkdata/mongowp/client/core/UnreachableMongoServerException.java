
package com.eightkdata.mongowp.client.core;

import com.google.common.net.HostAndPort;

/**
 *
 */
public class UnreachableMongoServerException extends Exception {
    private static final long serialVersionUID = 1L;

    private final HostAndPort hostAndPort;

    public UnreachableMongoServerException(HostAndPort hostAndPort) {
        this.hostAndPort = hostAndPort;
    }

    public UnreachableMongoServerException(HostAndPort hostAndPort, String message) {
        super(message);
        this.hostAndPort = hostAndPort;
    }

    public UnreachableMongoServerException(HostAndPort hostAndPort, String message, Throwable cause) {
        super(message, cause);
        this.hostAndPort = hostAndPort;
    }

    public UnreachableMongoServerException(HostAndPort hostAndPort, Throwable cause) {
        super(cause);
        this.hostAndPort = hostAndPort;
    }

    public HostAndPort getHostAndPort() {
        return hostAndPort;
    }
}
