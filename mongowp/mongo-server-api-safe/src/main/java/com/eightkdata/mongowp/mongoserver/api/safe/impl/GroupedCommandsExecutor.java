
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.*;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.CommandNotSupportedException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import com.google.common.collect.ImmutableList;

/**
 *
 */
public class GroupedCommandsExecutor implements CommandsExecutor {

    private final ImmutableList<CommandsExecutor> subExecutors;

    public GroupedCommandsExecutor(ImmutableList<CommandsExecutor> subExecutors) {
        this.subExecutors = subExecutors;
    }

    @Override
    public <Arg extends CommandArgument, Rep extends CommandReply> Rep execute(
            Command<? extends Arg, ? extends Rep> command,
            CommandRequest<Arg> request) throws MongoServerException, CommandNotSupportedException {
        for (CommandsExecutor subExecutor : subExecutors) {
            try {
                return subExecutor.execute(command, request);
            } catch (CommandNotSupportedException ex) {
            }
        }
        throw new CommandNotSupportedException(command.getCommandName());
    }

}
