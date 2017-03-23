/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.Status;
import com.eightkdata.mongowp.exceptions.CommandNotSupportedException;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandExecutor;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.server.api.CommandLibrary;
import com.eightkdata.mongowp.server.api.Request;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unchecked")
public class MapBasedCommandExecutor<ContextT> implements CommandExecutor<ContextT> {

  private static final Logger LOGGER = LogManager.getLogger(MapBasedCommandExecutor.class);

  /**
   * A map that associates each command with its implementation.
   *
   * It is guaranteed by construction that the implementation uses the same request and reply than
   * command it is associated with.
   */
  private final Map<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>> implementations;

  private MapBasedCommandExecutor(
      ImmutableMap<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>> implementations) {
    this.implementations = implementations;
  }

  public static <ContextT> CommandExecutor<ContextT> fromMap(
      Map<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>> map) {
    ImmutableMap<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>> copyMap =
        ImmutableMap.<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>>copyOf(map);
    return new MapBasedCommandExecutor<>(copyMap);
  }

  @Override
  public <A, R> Status<R> execute(Request request,
      Command<? super A, ? super R> command, A arg, ContextT context) {
    CommandImplementation<A, R, ContextT> implementation =
        (CommandImplementation<A, R, ContextT>) implementations.get(command);
    if (implementation == null) {
      return Status.from(new CommandNotSupportedException(command.getCommandName()));
    }
    return implementation.apply(request, command, arg, context);
  }

  public static <ContextT> Builder<ContextT> fromLibraryBuilder(CommandLibrary library) {
    return new FromLibraryBuilder<>(library);
  }

  public static <ContextT> Builder<ContextT> builder() {
    return new UnsafeBuilder<>();
  }

  public static interface Builder<ContextT> {

    public <RequestT, ResultT> Builder<ContextT> addImplementation(
        @Nonnull Command<RequestT, ResultT> command,
        @Nonnull CommandImplementation<RequestT, ResultT, ? super ContextT> implementation);

    public <RequestT, ResultT> Builder<ContextT> addImplementations(
        Iterable<Map.Entry<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>>> entries);

    public MapBasedCommandExecutor<ContextT> build();
  }

  private static class UnsafeBuilder<ContextT> implements Builder<ContextT> {

    private final Map<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>> implementations
        = Maps.newHashMap();

    @Override
    public <RequestT, ResultT> Builder<ContextT> addImplementations(
        Iterable<Map.Entry<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>>> entries) {
      for (Entry<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>> entry : entries) {
        addImplementationPrivate(entry.getKey(), entry.getValue());
      }
      return this;
    }

    @Override
    public <RequestT, ResultT> Builder<ContextT> addImplementation(
        Command<RequestT, ResultT> command,
        CommandImplementation<RequestT, ResultT, ? super ContextT> implementation) {
      return addImplementationPrivate(command, implementation);
    }

    private Builder<ContextT> addImplementationPrivate(
        Command<?, ?> command,
        CommandImplementation<?, ?, ? super ContextT> implementation) {
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
    public MapBasedCommandExecutor<ContextT> build() {
      return new MapBasedCommandExecutor(
          ImmutableMap.copyOf(implementations)
      );
    }
  }

  private static class FromLibraryBuilder<ContextT> extends UnsafeBuilder<ContextT> {

    @Nullable
    private final Set<Command> notImplementedCommands;

    public FromLibraryBuilder(CommandLibrary library) {
      this.notImplementedCommands = library.getSupportedCommands().orElse(null);
      if (this.notImplementedCommands == null) {
        LOGGER.debug("An unsafe commands library has been used to "
            + "create an executor. It is impossible to check at "
            + "creation time if all commands supported by the "
            + "library have an associated implementation");
      }
    }

    @Override
    public <RequestT, ResultT> Builder<ContextT> addImplementations(
        Iterable<Map.Entry<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>>> entries) {
      for (Entry<Command<?, ?>, CommandImplementation<?, ?, ? super ContextT>> entry : entries) {
        addImplementationPrivate(entry.getKey(), entry.getValue());
      }
      return this;
    }

    @Override
    public <RequestT, ResultT> Builder<ContextT> addImplementation(
        Command<RequestT, ResultT> command,
        CommandImplementation<RequestT, ResultT, ? super ContextT> implementation) {
      return addImplementationPrivate(command, implementation);
    }

    private Builder<ContextT> addImplementationPrivate(
        Command<?, ?> command,
        CommandImplementation<?, ?, ? super ContextT> implementation) {
      if (notImplementedCommands != null && !notImplementedCommands.remove(command)) {
        throw new IllegalArgumentException("Command " + command + " is "
            + "not supported by the given library");
      }
      return super.addImplementationPrivate(command, implementation);
    }

    @Override
    public MapBasedCommandExecutor<ContextT> build() {
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
