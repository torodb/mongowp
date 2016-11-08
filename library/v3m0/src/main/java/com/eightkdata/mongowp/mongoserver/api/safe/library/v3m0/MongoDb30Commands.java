/*
 * MongoWP - MongoWP: v3.0 Library
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
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.AdminCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.AdminCommands.AdminCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.aggregation.AggregationCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.aggregation.AggregationCommands.AggregationCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.authentication.AuthenticationCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.authentication.AuthenticationCommands.AuthenticationCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.DiagnosticCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.DiagnosticCommands.DiagnosticCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GeneralCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GeneralCommands.GeneralCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.InternalCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.InternalCommands.InternalCommandsImplementationsBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplCommands;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplCommands.ReplCommandsImplementationsBuilder;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
                        new AuthenticationCommands(),
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

    public static class MongoDb30CommandsImplementationBuilder<Context> implements Iterable<Map.Entry<Command<?,?>, CommandImplementation<?, ?, ? super Context>>> {

        private final AdminCommandsImplementationsBuilder<Context> adminCommandsImplementationsBuilder;
        private final AggregationCommandsImplementationsBuilder<Context> aggregationImplementationsBuilder;
        private final AuthenticationCommandsImplementationsBuilder<Context> authenticationCommandsImplementationsBuilder;
        private final DiagnosticCommandsImplementationsBuilder<Context> diagnosticImplementationsBuilder;
        private final GeneralCommandsImplementationsBuilder<Context> generalImplementationsBuilder;
        private final InternalCommandsImplementationsBuilder<Context> internalCommandsImplementationsBuilder;
        private final ReplCommandsImplementationsBuilder<Context> replCommandsImplementationsBuilder;

        public MongoDb30CommandsImplementationBuilder(
                AdminCommandsImplementationsBuilder<Context> adminCommandsImplementationsBuilder,
                AggregationCommandsImplementationsBuilder<Context> aggregationImplementationsBuilder,
                AuthenticationCommandsImplementationsBuilder<Context> authenticationCommandsImplementationsBuilder,
                DiagnosticCommandsImplementationsBuilder<Context> diagnosticImplementationsBuilder,
                GeneralCommandsImplementationsBuilder<Context> generalImplementationsBuilder,
                InternalCommandsImplementationsBuilder<Context> internalCommandsImplementationsBuilder,
                ReplCommandsImplementationsBuilder<Context> replCommandsImplementationsBuilder) {
            this.adminCommandsImplementationsBuilder
                    = adminCommandsImplementationsBuilder;
            this.aggregationImplementationsBuilder
                    = aggregationImplementationsBuilder;
            this.authenticationCommandsImplementationsBuilder
                    = authenticationCommandsImplementationsBuilder;
            this.diagnosticImplementationsBuilder
                    = diagnosticImplementationsBuilder;
            this.generalImplementationsBuilder = generalImplementationsBuilder;
            this.internalCommandsImplementationsBuilder
                    = internalCommandsImplementationsBuilder;
            this.replCommandsImplementationsBuilder
                    = replCommandsImplementationsBuilder;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Iterator<Entry<Command<?, ?>, CommandImplementation<?, ?, ? super Context>>> iterator() {
            return Iterators.concat(
                    adminCommandsImplementationsBuilder.iterator(),
                    aggregationImplementationsBuilder.iterator(),
                    authenticationCommandsImplementationsBuilder.iterator(),
                    diagnosticImplementationsBuilder.iterator(),
                    generalImplementationsBuilder.iterator(),
                    internalCommandsImplementationsBuilder.iterator(),
                    replCommandsImplementationsBuilder.iterator()
            );
        }

    }
}
