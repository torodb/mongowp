
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0;

import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.AdminCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.AdminCommands.AdminCommandsImplementationsBuilder;
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

    @SuppressWarnings("unchecked")
    public MongoDb30Commands() {
        commands = ImmutableList.copyOf(
                Iterables.concat(
                        new AdminCommands(),
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

    public static class MongoDb30CommandsImplementationBuilder implements Iterable<Map.Entry<Command<?,?>, CommandImplementation>> {

        private final AdminCommandsImplementationsBuilder adminCommandsImplementationsBuilder;
        private final AggregationCommandsImplementationsBuilder aggregationImplementationsBuilder;
        private final DiagnosticCommandsImplementationsBuilder diagnosticImplementationsBuilder;
        private final GeneralCommandsImplementationsBuilder generalImplementationsBuilder;
        private final InternalCommandsImplementationsBuilder internalCommandsImplementationsBuilder;
        private final ReplCommandsImplementationsBuilder replCommandsImplementationsBuilder;

        public MongoDb30CommandsImplementationBuilder(
                AdminCommandsImplementationsBuilder adminCommandsImplementationsBuilder,
                AggregationCommandsImplementationsBuilder aggregationImplementationsBuilder,
                DiagnosticCommandsImplementationsBuilder diagnosticImplementationsBuilder,
                GeneralCommandsImplementationsBuilder generalImplementationsBuilder,
                InternalCommandsImplementationsBuilder internalCommandsImplementationsBuilder,
                ReplCommandsImplementationsBuilder replCommandsImplementationsBuilder) {
            this.adminCommandsImplementationsBuilder
                    = adminCommandsImplementationsBuilder;
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

        @SuppressWarnings("unchecked")
        @Override
        public Iterator<Map.Entry<Command<?,?>, CommandImplementation>> iterator() {
            return Iterators.concat(
                    adminCommandsImplementationsBuilder.iterator(),
                    aggregationImplementationsBuilder.iterator(),
                    diagnosticImplementationsBuilder.iterator(),
                    generalImplementationsBuilder.iterator(),
                    internalCommandsImplementationsBuilder.iterator(),
                    replCommandsImplementationsBuilder.iterator()
            );
        }

    }
}
