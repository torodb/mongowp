
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.CommandReply;
import com.eightkdata.mongowp.server.api.CommandsExecutor;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandRequest;
import com.eightkdata.mongowp.exceptions.CommandNotSupportedException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;

/**
 *
 */
public class GroupedCommandsExecutor implements CommandsExecutor {

    private final ImmutableList<CommandsExecutor> subExecutors;

    public GroupedCommandsExecutor(ImmutableList<CommandsExecutor> subExecutors) {
        this.subExecutors = subExecutors;
    }

    @Override
    public <Arg, Result> CommandReply<Result> execute(
            @Nonnull Command<? super Arg, ? super Result> command,
            @Nonnull CommandRequest<Arg> request)
            throws MongoException, CommandNotSupportedException {
        for (CommandsExecutor subExecutor : subExecutors) {
            try {
                return subExecutor.execute(command, request);
            } catch (CommandNotSupportedException ex) {
            }
        }
        throw new CommandNotSupportedException(command.getCommandName());
    }

}
