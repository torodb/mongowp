
package com.eightkdata.mongowp.server.api;

import java.net.InetAddress;
import javax.annotation.Nonnull;

/**
 *
 */
public class CommandRequest<Arg> extends Request {

    @Nonnull
    private final Arg commandArg;
    private final boolean slaveOk;

    public CommandRequest(
            Connection connection,
            int requestId,
            String database,
            InetAddress clientAddress,
            int clientPort,
            @Nonnull Arg commandArg,
            boolean slaveOk) {
        super(connection, requestId, database, clientAddress, clientPort);
        this.commandArg = commandArg;
        this.slaveOk = slaveOk;
    }

    @Nonnull
    public Arg getCommandArgument() {
        return commandArg;
    }

    public boolean isSlaveOk() {
        return slaveOk;
    }
    
}
