/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.server.api.CommandResult;
import com.eightkdata.mongowp.server.api.CommandReply;
import com.eightkdata.mongowp.server.api.CommandsExecutor;
import com.eightkdata.mongowp.server.api.CommandsLibrary;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandRequest;
import com.eightkdata.mongowp.exceptions.CommandNotSupportedException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@SuppressWarnings("unchecked")
public class MapBasedCommandsExecutor implements CommandsExecutor {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(MapBasedCommandsExecutor.class);

    /**
     * A map that associates each command with its implementation.
     * 
     * It is guaranteed by construction that the implementation uses the same
     * request and reply than command it is associated with.
     */
    private final ImmutableMap<Command, CommandImplementation> implementations;

    private MapBasedCommandsExecutor(ImmutableMap<Command, CommandImplementation> implementations) {
        this.implementations = implementations;
    }
    
    @Override
    public <Arg, Result> CommandReply<Result> execute(
            @Nonnull Command<? super Arg, ? super Result> command,
            @Nonnull CommandRequest<Arg> request)
            throws MongoException, CommandNotSupportedException {
        CommandImplementation<Arg, Result> implementation = implementations.get(command);
        if (implementation == null) {
            throw new CommandNotSupportedException(command.getCommandName());
        }
        try {
            CommandResult<Result> result = implementation.apply(command, request);
            if (command.isReadyToReplyResult(result.getResult())) {
                return new DelegateCommandReply<>(command, result);
            }
            else {
                return new CorrectCommandReply<>(command, result);
            }
        } catch (MongoException ex) {
            return new FailedCommandReply<>(ex);
        }
    }
    
    
    public static Builder fromLibraryBuilder(CommandsLibrary library) {
        return new FromLibraryBuilder(library);
    }

    public static Builder builder() {
        return new UnsafeBuilder();
    }
    
    public static interface Builder {
        
        public <Req, Result> Builder addImplementation(
                @Nonnull Command<Req, Result> command,
                @Nonnull CommandImplementation<Req, Result> implementation);

        public <Req, Result> Builder addImplementations(
                Iterable<Map.Entry<Command<?,?>, CommandImplementation>> entries);
        
        public MapBasedCommandsExecutor build();
    }

    private static class UnsafeBuilder implements Builder {

        private final Map<Command, CommandImplementation> implementations = Maps.newHashMap();

        @Override
        public <Req, Result> Builder addImplementations(
                Iterable<Map.Entry<Command<?,?>, CommandImplementation>> entries) {
            for (Entry<Command<?,?>, CommandImplementation> entry : entries) {
                addImplementation(entry.getKey(), entry.getValue());
            }
            return this;
        }

        @Override
        public <Req, Result> Builder addImplementation(
                Command<Req, Result> command,
                CommandImplementation<Req, Result> implementation) {
            if (implementations.containsKey(command)) {
                throw new IllegalArgumentException(
                        "There is another implementation ("
                        + implementations.get(command) + " associated to "
                        + command);
            }

            implementations.put(command, implementation);

            return this;
        }

        @Override
        public MapBasedCommandsExecutor build() {
            return new MapBasedCommandsExecutor(
                    ImmutableMap.copyOf(implementations)
            );
        }
    }
    
    private static class FromLibraryBuilder extends UnsafeBuilder {
        private final @Nullable Set<Command> notImplementedCommands;

        public FromLibraryBuilder(CommandsLibrary library) {
            this.notImplementedCommands = library.getSupportedCommands();
            if (this.notImplementedCommands == null) {
                LOGGER.warn("An unsafe commands library has been used to "
                        + "create an executor. It is impossible to check at "
                        + "creation time if all commands supported by the "
                        + "library have an associated implementation");
            }
        }

        @Override
        public <Req, Result> Builder addImplementations(
                Iterable<Map.Entry<Command<?,?>, CommandImplementation>> entries) {
            for (Entry<Command<?,?>, CommandImplementation> entry : entries) {
                addImplementation(entry.getKey(), entry.getValue());
            }
            return this;
        }
        
        @Override
        public <Req, Result> Builder addImplementation(
                Command<Req, Result> command,
                CommandImplementation<Req, Result> implementation) {
            if (notImplementedCommands != null && !notImplementedCommands.remove(command)) {
                throw new IllegalArgumentException("Command " + command + " is "
                        + "not supported by the given library");
            }
            return super.addImplementation(command, implementation);
        }
        
        @Override
        public MapBasedCommandsExecutor build() {
            if (notImplementedCommands != null) {
                Preconditions.checkState(
                        notImplementedCommands.isEmpty(),
                        "The following commands have no implementation: %s",
                        notImplementedCommands
                );
            }
            return super.build();
        }
    }
    
}
