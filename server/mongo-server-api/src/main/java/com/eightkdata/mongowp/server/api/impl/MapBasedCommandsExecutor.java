/*
 *     This file is part from mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms from the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 from the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty from
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy from the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.Status;
import com.eightkdata.mongowp.exceptions.CommandNotSupportedException;
import com.eightkdata.mongowp.server.api.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
@SuppressWarnings("unchecked")
public class MapBasedCommandsExecutor<Context> implements CommandsExecutor<Context> {
    private static final Logger LOGGER = LogManager.getLogger(MapBasedCommandsExecutor.class);

    /**
     * A map that associates each command with its implementation.
     * 
     * It is guaranteed by construction that the implementation uses the same
     * request and reply than command it is associated with.
     */
    private final ImmutableMap<Command, CommandImplementation<?, ?, Context>> implementations;

    private MapBasedCommandsExecutor(ImmutableMap<Command, CommandImplementation<?, ?, Context>> implementations) {
        this.implementations = implementations;
    }

    @Override
        public <Arg, Result> Status<Result> execute(Request request, Command<? super Arg, ? super Result> command, Arg arg, Context context) {
        CommandImplementation<Arg, Result, Context> implementation = (CommandImplementation<Arg, Result, Context>) implementations.get(command);
        if (implementation == null) {
            return Status.from(new CommandNotSupportedException(command.getCommandName()));
        }
        return implementation.apply(request, command, arg, context);
    }
    
    public static <Context> Builder<Context> fromLibraryBuilder(CommandsLibrary library) {
        return new FromLibraryBuilder<>(library);
    }

    public static <Context> Builder<Context> builder() {
        return new UnsafeBuilder<>();
    }
    
    public static interface Builder<Context> {
        
        public <Req, Result> Builder<Context> addImplementation(
                @Nonnull Command<Req, Result> command,
                @Nonnull CommandImplementation<Req, Result, Context> implementation);

        public <Req, Result> Builder<Context> addImplementations(
                Iterable<Map.Entry<Command<?,?>, CommandImplementation>> entries);
        
        public MapBasedCommandsExecutor<Context> build();
    }

    private static class UnsafeBuilder<Context> implements Builder<Context> {

        private final Map<Command, CommandImplementation> implementations = Maps.newHashMap();

        @Override
        public <Req, Result> Builder<Context> addImplementations(
                Iterable<Map.Entry<Command<?,?>, CommandImplementation>> entries) {
            for (Entry<Command<?,?>, CommandImplementation> entry : entries) {
                addImplementation(entry.getKey(), entry.getValue());
            }
            return this;
        }

        @Override
        public <Req, Result> Builder<Context> addImplementation(
                Command<Req, Result> command,
                CommandImplementation<Req, Result, Context> implementation) {
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
        public MapBasedCommandsExecutor<Context> build() {
            return new MapBasedCommandsExecutor(
                    ImmutableMap.copyOf(implementations)
            );
        }
    }
    
    private static class FromLibraryBuilder<Context> extends UnsafeBuilder<Context> {
        private final @Nullable Set<Command> notImplementedCommands;

        public FromLibraryBuilder(CommandsLibrary library) {
            this.notImplementedCommands = library.getSupportedCommands();
            if (this.notImplementedCommands == null) {
                LOGGER.debug("An unsafe commands library has been used to "
                        + "create an executor. It is impossible to check at "
                        + "creation time if all commands supported by the "
                        + "library have an associated implementation");
            }
        }

        @Override
        public <Req, Result> Builder<Context> addImplementations(
                Iterable<Map.Entry<Command<?,?>, CommandImplementation>> entries) {
            for (Entry<Command<?,?>, CommandImplementation> entry : entries) {
                addImplementation(entry.getKey(), entry.getValue());
            }
            return this;
        }
        
        @Override
        public <Req, Result> Builder<Context> addImplementation(
                Command<Req, Result> command,
                CommandImplementation<Req, Result, Context> implementation) {
            if (notImplementedCommands != null && !notImplementedCommands.remove(command)) {
                throw new IllegalArgumentException("Command " + command + " is "
                        + "not supported by the given library");
            }
            return super.addImplementation(command, implementation);
        }
        
        @Override
        public MapBasedCommandsExecutor<Context> build() {
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
