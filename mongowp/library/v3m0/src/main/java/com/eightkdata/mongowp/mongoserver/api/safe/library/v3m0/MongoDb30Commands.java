
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandImplementation;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.aggregation.AggregationCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.aggregation.AggregationCommands.AggregationCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.DiagnosticCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.DiagnosticCommands.DiagnosticCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GeneralCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GeneralCommands.GeneralCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.InternalCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.InternalCommands.InternalCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplCommands.ReplCommandsImplementationsBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class MongoDb30Commands implements Iterable<Command> {

    private final ImmutableList<Command> commands;

    public MongoDb30Commands() {
        commands = ImmutableList.copyOf(
                Iterables.concat(
                        new AggregationCommands(),
                        new DiagnosticCommands(),
                        new GeneralCommands(),
                        new InternalCommands(),
                        new ReplCommands()
                )
        );
    }

    @Override
    public Iterator<Command> iterator() {
         return commands.iterator();
    }

    public static class MongoDb30CommandsImplementationBuilder implements Iterable<Map.Entry<Command, CommandImplementation>> {

        private final AggregationCommandsImplementationsBuilder aggregationImplementationsBuilder;
        private final DiagnosticCommandsImplementationsBuilder diagnosticImplementationsBuilder;
        private final GeneralCommandsImplementationsBuilder generalImplementationsBuilder;
        private final InternalCommandsImplementationsBuilder internalCommandsImplementationsBuilder;
        private final ReplCommandsImplementationsBuilder replCommandsImplementationsBuilder;

        public MongoDb30CommandsImplementationBuilder(
                AggregationCommandsImplementationsBuilder aggregationImplementationsBuilder,
                DiagnosticCommandsImplementationsBuilder diagnosticImplementationsBuilder,
                GeneralCommandsImplementationsBuilder generalImplementationsBuilder,
                InternalCommandsImplementationsBuilder internalCommandsImplementationsBuilder,
                ReplCommandsImplementationsBuilder replCommandsImplementationsBuilder) {
            this.aggregationImplementationsBuilder
                    = aggregationImplementationsBuilder;
            this.diagnosticImplementationsBuilder
                    = diagnosticImplementationsBuilder;
            this.generalImplementationsBuilder = generalImplementationsBuilder;
            this.internalCommandsImplementationsBuilder
                    = internalCommandsImplementationsBuilder;
            this.replCommandsImplementationsBuilder
                    = replCommandsImplementationsBuilder;
        }

        @Override
        public Iterator<Map.Entry<Command, CommandImplementation>> iterator() {
            return Iterators.concat(
                    aggregationImplementationsBuilder.iterator(),
                    diagnosticImplementationsBuilder.iterator(),
                    generalImplementationsBuilder.iterator(),
                    internalCommandsImplementationsBuilder.iterator(),
                    replCommandsImplementationsBuilder.iterator()
            );
        }

    }
}
