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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class GroupedCommandsLibrary implements CommandsLibrary {

    private final String supportedVersion;
    private final ImmutableList<CommandsLibrary> subLibraries;

    public GroupedCommandsLibrary(String supportedVersion, ImmutableList<CommandsLibrary> subLibraries) {
        this.supportedVersion = supportedVersion;
        this.subLibraries = subLibraries;
    }

    @Override
    public String getSupportedVersion() {
        return supportedVersion;
    }

    @Override
    public Set<Command> getSupportedCommands() {
        HashSet<Command> supportedCommands = Sets.newHashSet();

        for (CommandsLibrary subLibrary : subLibraries) {
            Set<Command> subSupportedCommands = subLibrary.getSupportedCommands();
            if (subSupportedCommands == null) {
                return null;
            }
            supportedCommands.addAll(subSupportedCommands);
        }

        return supportedCommands;
    }

    @Override
    public LibraryEntry find(BsonDocument requestDocument) {
        for (CommandsLibrary subLibrary : subLibraries) {
            LibraryEntry found = subLibrary.find(requestDocument);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

}
