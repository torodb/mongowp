
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandArgument;

/**
 *
 */
public class SimpleArgument implements CommandArgument {

    private final Command command;

    public SimpleArgument(Command command) {
        this.command = command;
    }

    @Override
    public Command getCommand() {
        return command;
    }
}
