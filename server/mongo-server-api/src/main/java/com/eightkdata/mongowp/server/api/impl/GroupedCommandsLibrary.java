
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
    public Command find(BsonDocument requestDocument) {
        for (CommandsLibrary subLibrary : subLibraries) {
            Command found = subLibrary.find(requestDocument);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

}
