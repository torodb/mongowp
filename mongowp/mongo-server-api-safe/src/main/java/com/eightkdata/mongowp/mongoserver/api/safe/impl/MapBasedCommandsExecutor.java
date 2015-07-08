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
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.*;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.CommandNotSupportedException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
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
    public <Arg extends CommandArgument, Rep extends CommandReply> Rep execute(
            Command<? extends Arg, ? extends Rep> command, 
            CommandRequest<Arg> request)
            throws MongoServerException {
        CommandImplementation<Arg, Rep> implementation = implementations.get(command);
        if (implementation == null) {
            throw new CommandNotSupportedException(command.getCommandName());
        }
        return implementation.apply(command, request);
    }
    
    
    public static Builder fromLibraryBuilder(CommandsLibrary library) {
        return new FromLibraryBuilder(library);
    }
    
    public static interface Builder {
        
        public <Req extends CommandArgument, Rep extends CommandReply> Builder addImplementation(
                @Nonnull Command<Req, Rep> command,
                @Nonnull CommandImplementation<Req, Rep> implementation);

        public <Req extends CommandArgument, Rep extends CommandReply> Builder addImplementations(
                Iterable<Map.Entry<Command, CommandImplementation>> entries);
        
        public MapBasedCommandsExecutor build();
    }
    
    private static class FromLibraryBuilder implements Builder {
        private final Map<Command, CommandImplementation> implementations;
        private final @Nullable Set<Command> notImplementedCommands;

        public FromLibraryBuilder(CommandsLibrary library) {
            this.notImplementedCommands = library.getSupportedCommands();
            if (this.notImplementedCommands == null) {
                LOGGER.warn("An unsafe commands library has been used to "
                        + "create an executor. It is not possible to check at "
                        + "creation time if all commands supported by the "
                        + "library have an associated implementation");
                this.implementations = Maps.newHashMap();
            }
            else {
                this.implementations = Maps.newHashMapWithExpectedSize(notImplementedCommands.size());
            }
        }

        @Override
        public <Req extends CommandArgument, Rep extends CommandReply> Builder addImplementations(
                Iterable<Map.Entry<Command, CommandImplementation>> entries) {
            for (Entry<Command, CommandImplementation> entry : entries) {
                addImplementation(entry.getKey(), entry.getValue());
            }
            return this;
        }
        
        @Override
        public <Req extends CommandArgument, Rep extends CommandReply> Builder addImplementation(
                Command<Req, Rep> command,
                CommandImplementation<Req, Rep> implementation) {
            if (implementations.containsKey(command)) {
                throw new IllegalArgumentException(
                        "There is another implementation ("
                        + implementations.get(command) + " associated to "
                        + command);
            }

            if (notImplementedCommands != null && !notImplementedCommands.remove(command)) {
                throw new IllegalArgumentException("Command " + command + " is "
                        + "not supported by the given collection");
            }
            
            implementations.put(command, implementation);
            
            return this;
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
            return new MapBasedCommandsExecutor(
                    ImmutableMap.copyOf(implementations)
            );
        }
    }
    
}
