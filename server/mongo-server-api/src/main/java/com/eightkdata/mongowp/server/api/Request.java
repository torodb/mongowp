
package com.eightkdata.mongowp.server.api;

import java.net.InetAddress;
import java.time.Duration;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class Request {

    private final String database;
    @Nullable
    private final ExternalClientInfo externalClientInfo;
    private final boolean slaveOk;
    private final Duration timeout;

    public Request(String database, ExternalClientInfo externalClientInfo,
            boolean slaveOk, @Nullable Duration timeout) {
        this.database = database;
        this.externalClientInfo = externalClientInfo;
        this.slaveOk = slaveOk;
        this.timeout = timeout;
    }

    public String getDatabase() {
        return database;
    }

    @Nullable
    public ExternalClientInfo getExternalClientInfo() {
        return externalClientInfo;
    }

    public boolean isSlaveOk() {
        return slaveOk;
    }

    @Nullable
    public Duration getTimeout() {
        return timeout;
    }

    public static class ExternalClientInfo {
        @Nonnull private final InetAddress clientAddress;
        @Nonnegative private final int clientPort;

        public ExternalClientInfo(InetAddress clientAddress, int clientPort) {
            this.clientAddress = clientAddress;
            this.clientPort = clientPort;
        }

        public InetAddress getClientAddress() {
            return clientAddress;
        }

        public int getClientPort() {
            return clientPort;
        }
    }
}
