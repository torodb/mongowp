
package com.eightkdata.mongowp.server.api;

import java.net.InetAddress;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class Request {

    private final Connection connection;
    private final int requestId;
    private final String database;
    @Nullable private final InetAddress clientAddress;
    @Nonnegative private final int clientPort;

    /**
     *
     * @param connection
     * @param requestId
     * @param database
     * @param clientAddress the client address if this is a remote request or
     *                      null if this is a request
     * @param clientPort    a non negative value that represents the port of the
     *                      client in a remote request or its ignored in a local
     *                      request
     * @see #isLocal()
     */
    public Request(
            @Nonnull Connection connection,
            int requestId,
            @Nullable String database,
            @Nullable InetAddress clientAddress,
            @Nonnegative int clientPort) {
        this.connection = connection;
        this.requestId = requestId;
        this.database = database;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }

    @Nonnull
    public Connection getConnection() {
        return connection;
    }

    public int getRequestId() {
        return requestId;
    }

    @Nullable
    public String getDatabase() {
        return database;
    }

    /**
     * Returns true iff the request is done locally.
     * @return true iff the request is done locally
     */
    public boolean isLocal() {
        return clientAddress == null;
    }

    @Nullable
    public InetAddress getClientAddress() {
        return clientAddress;
    }

    @Nonnegative
    public int getClientPort() {
        return clientPort;
    }
}
