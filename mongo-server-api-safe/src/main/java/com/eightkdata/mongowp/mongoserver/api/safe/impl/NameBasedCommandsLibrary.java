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

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandsLibrary;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.bson.BsonDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class NameBasedCommandsLibrary implements CommandsLibrary {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(NameBasedCommandsLibrary.class);
    private final String version;
    private final ImmutableMap<String, Command> commandsMap;

    public NameBasedCommandsLibrary(String version, Iterable<Command> commands) {
        this.version = version;
        
        ImmutableMap.Builder<String, Command> commandsMapBuilder = ImmutableMap.builder();
        
        for (Command command : commands) {
            commandsMapBuilder.put(command.getCommandName(), command);
        }
        
        this.commandsMap = commandsMapBuilder.build();
    }

    @Override
    public String getSupportedVersion() {
        return version;
    }

    @Override
    public Set<Command> getSupportedCommands() {
        HashSet<Command> supportedCommands = Sets.newHashSet(commandsMap.values());
        assert supportedCommands.size() == commandsMap.size();
        
        return supportedCommands;
    }

    @Override
    public Command find(BsonDocument requestDocument) {
        if (requestDocument.keySet().isEmpty()) {
            return null;
        }
        String commandName = requestDocument.keySet().iterator().next();
        Command result = commandsMap.get(commandName);
        
        if (result == null) {
            LOGGER.trace("The first command do not correspond with a valid command name. Looking for other fields");
            
            for(String possibleCommand : requestDocument.keySet()) {
                // Some driver use lower case version of the command so we must take it into account
                possibleCommand = possibleCommand.toLowerCase(Locale.ROOT);
                result = commandsMap.get(possibleCommand);
                if (result != null) {
                    LOGGER.warn("Recived a request to execute " 
                            + possibleCommand + " command but the name key was "
                            + "not the first key of the BSON document");
                    return result;
                }
            }
        }
        
        return result;
    }
    
}
