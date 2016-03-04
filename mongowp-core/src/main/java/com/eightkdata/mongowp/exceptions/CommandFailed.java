
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class CommandFailed extends MongoException {
    private static final long serialVersionUID = 1L;

    private final String commandName;
    private final String reason;

    public CommandFailed(String commandName, String reason) {
        super(createCustomMessage(commandName, reason), ErrorCode.COMMAND_FAILED);
        this.commandName = commandName;
        this.reason = reason;
    }

    public CommandFailed(String commandName, String reason, Throwable cause) {
        super(createCustomMessage(commandName, reason), cause, ErrorCode.COMMAND_FAILED);
        this.commandName = commandName;
        this.reason = reason;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getReason() {
        return reason;
    }

    private static String createCustomMessage(String commandName, String reason) {
        return "Execution of command '" + commandName + "' failed because " + reason;
    }
}
