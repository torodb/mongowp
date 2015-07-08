
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandImplementation;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.CollStatsCommand.CollStatsArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.CollStatsCommand.CollStatsReply;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 */
public class DiagnosticCommands implements Iterable<Command> {

    private final ImmutableList<Command> commands = ImmutableList.<Command>of(
            CollStatsCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class DiagnosticCommandsImplementationsBuilder implements Iterable<Map.Entry<Command, CommandImplementation>> {

        public abstract CommandImplementation<CollStatsArgument, CollStatsReply> getCollStatsImplementation();

        private Map<Command, CommandImplementation> createMap() {
            return ImmutableMap.<Command, CommandImplementation>builder()
                    .put(CollStatsCommand.INSTANCE, getCollStatsImplementation())
                    .build();
        }

        @Override
        public Iterator<Entry<Command, CommandImplementation>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
    
}
