
package com.eightkdata.mongowp.mongoserver.api.safe;

import java.net.InetAddress;

/**
 *
 */
public class CommandRequest<Arg extends CommandArgument> extends Request {

    private final Arg commandArg;

    public CommandRequest(
            Connection connection,
            int requestId,
            String database,
            InetAddress clientAddress,
            int clientPort,
            Arg commandArg) {
        super(connection, requestId, database, clientAddress, clientPort);
        this.commandArg = commandArg;
    }
    
    public Arg getCommandArgument() {
        return commandArg;
    }
    
}
