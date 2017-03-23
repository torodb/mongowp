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
package com.torodb.mongowp.commands.impl;

import com.google.common.collect.ImmutableMap;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.commands.Command;
import com.torodb.mongowp.commands.CommandLibrary;
import com.torodb.mongowp.commands.CommandLibrary.LibraryEntry;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class NameBasedCommandLibrary implements CommandLibrary {

  private final String version;
  private final ImmutableMap<String, Command<?, ?>> commandMap;

  public NameBasedCommandLibrary(String version,
      ImmutableMap<String, Command<?, ?>> commandsMap) {
    this.version = version;
    this.commandMap = commandsMap;
  }

  @Override
  public String getSupportedVersion() {
    return version;
  }

  @Override
  public Optional<Map<String, Command>> asMap() {
    return Optional.of(Collections.unmodifiableMap(commandMap));
  }

  @Override
  public LibraryEntry find(BsonDocument requestDocument) {
    if (requestDocument.isEmpty()) {
      return null;
    }
    String commandAlias = requestDocument.getFirstEntry().getKey();
    String key = commandAlias.toLowerCase(Locale.ENGLISH);
    Command<?, ?> command = commandMap.get(key);
    if (command == null) {
      return null;
    }

    return new PojoLibraryEntry(commandAlias, command);
  }

  public static class Builder {

    private String version;
    private final ImmutableMap.Builder<String, Command<?, ?>> commandsMapBuilder;

    public Builder(String version) {
      commandsMapBuilder = new ImmutableMap.Builder<>();
      this.version = version;
    }

    public Builder addCommand(Command<?, ?> command) {
      return addAsAlias(command, command.getCommandName());
    }

    public Builder addAsAlias(Command<?, ?> command, String alias) {
      commandsMapBuilder.put(alias.toLowerCase(Locale.ENGLISH), command);
      return this;
    }

    public Builder addCommands(Iterable<Command<?, ?>> commands) {
      for (Command<?, ?> command : commands) {
        addCommand(command);
      }
      return this;
    }

    public NameBasedCommandLibrary build() {
      return new NameBasedCommandLibrary(version, commandsMapBuilder.build());
    }
  }

}
