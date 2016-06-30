
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.BuildInfoCommand.BuildInfoResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.CollStatsCommand.CollStatsArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.CollStatsCommand.CollStatsReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.GetLogCommand.GetLogArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.GetLogCommand.GetLogReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.ListDatabasesCommand.ListDatabasesReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.ServerStatusCommand.ServerStatusArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.ServerStatusCommand.ServerStatusReply;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.server.api.tools.Empty;
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
            CollStatsCommand.INSTANCE,
            ListDatabasesCommand.INSTANCE,
            BuildInfoCommand.INSTANCE,
            PingCommand.INSTANCE,
            ServerStatusCommand.INSTANCE,
            GetLogCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class DiagnosticCommandsImplementationsBuilder<Context> implements Iterable<Map.Entry<Command<?,?>, CommandImplementation>> {

        public abstract CommandImplementation<CollStatsArgument, CollStatsReply, ? super Context> getCollStatsImplementation();

        public abstract CommandImplementation<GetLogArgument, GetLogReply, ? super Context> getGetLogImplementation();

        public abstract CommandImplementation<Empty, ListDatabasesReply, ? super Context> getListDatabasesImplementation();

        public abstract CommandImplementation<Empty, Empty, ? super Context> getPingCommandImplementation();

        public abstract CommandImplementation<Empty, BuildInfoResult, ? super Context> getBuildInfoImplementation();

        public abstract CommandImplementation<ServerStatusArgument, ServerStatusReply, ? super Context> getServerStatusImplementation();

        private Map<Command<?,?>, CommandImplementation> createMap() {
            return ImmutableMap.<Command<?,?>, CommandImplementation>builder()
                    .put(CollStatsCommand.INSTANCE, getCollStatsImplementation())
                    .put(GetLogCommand.INSTANCE, getGetLogImplementation())
                    .put(ListDatabasesCommand.INSTANCE, getListDatabasesImplementation())
                    .put(BuildInfoCommand.INSTANCE, getBuildInfoImplementation())
                    .put(PingCommand.INSTANCE, getPingCommandImplementation())
                    .put(ServerStatusCommand.INSTANCE, getServerStatusImplementation())
                    .build();
        }

        @Override
        public Iterator<Entry<Command<?,?>, CommandImplementation>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
    
}
