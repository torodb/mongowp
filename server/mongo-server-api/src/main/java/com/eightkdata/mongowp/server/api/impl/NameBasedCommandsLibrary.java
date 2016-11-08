/*
 * MongoWP - Mongo Server: API
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandsLibrary;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 *
 */
public class NameBasedCommandsLibrary implements CommandsLibrary {

    private final String version;
    private final ImmutableMap<String, Command<?, ?>> commandsMap;

    public NameBasedCommandsLibrary(String version,
            ImmutableMap<String, Command<?,?>> commandsMap) {
        this.version = version;
        this.commandsMap = commandsMap;
    }

    @Override
    public String getSupportedVersion() {
        return version;
    }

    @Override
    public Set<Command> getSupportedCommands() {
        HashSet<Command> supportedCommands = Sets.newHashSet(commandsMap.values());
        
        return supportedCommands;
    }

    @Override
    public LibraryEntry find(BsonDocument requestDocument) {
        if (requestDocument.isEmpty()) {
            return null;
        }
        String commandAlias = requestDocument.getFirstEntry().getKey();
        String key = commandAlias.toLowerCase(Locale.ENGLISH);
        Command<?, ?> command = commandsMap.get(key);
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

        public NameBasedCommandsLibrary build() {
            return new NameBasedCommandsLibrary(version, commandsMapBuilder.build());
        }
    }
    
}
