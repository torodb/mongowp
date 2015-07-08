
package com.eightkdata.mongowp.mongoserver.api.safe;

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
    @Nonnull private final InetAddress clientAddress;
    @Nonnegative private final int clientPort;

    public Request(
            @Nonnull Connection connection,
            int requestId,
            @Nullable String database,
            @Nonnull InetAddress clientAddress,
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

    @Nonnull
    public InetAddress getClientAddress() {
        return clientAddress;
    }

    @Nonnegative
    public int getClientPort() {
        return clientPort;
    }
}
