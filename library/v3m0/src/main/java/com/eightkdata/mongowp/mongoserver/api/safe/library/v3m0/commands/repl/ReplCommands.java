
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ApplyOpsCommand.ApplyOpsArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ApplyOpsCommand.ApplyOpsReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.IsMasterCommand.IsMasterReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetFreezeCommand.ReplSetFreezeArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetFreezeCommand.ReplSetFreezeReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetGetStatusCommand.ReplSetGetStatusReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetReconfigCommand.ReplSetReconfigArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetStepDownCommand.ReplSetStepDownArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetSyncFromCommand.ReplSetSyncFromReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 */
public class ReplCommands implements Iterable<Command> {

    private final ImmutableList<Command> commands = ImmutableList.<Command>of(
            ApplyOpsCommand.INSTANCE,
            IsMasterCommand.INSTANCE,
            ReplSetFreezeCommand.INSTANCE,
            ReplSetGetConfigCommand.INSTANCE,
            ReplSetGetStatusCommand.INSTANCE,
            ReplSetInitiateCommand.INSTANCE,
            ReplSetMaintenanceCommand.INSTANCE,
            ReplSetReconfigCommand.INSTANCE,
            ReplSetStepDownCommand.INSTANCE,
            ReplSetSyncFromCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class ReplCommandsImplementationsBuilder<Context> implements Iterable<Entry<Command<?,?>, CommandImplementation<?, ?, ? super Context>>> {

        public abstract CommandImplementation<ApplyOpsArgument, ApplyOpsReply, ? super Context> getApplyOpsImplementation();

        public abstract CommandImplementation<Empty, IsMasterReply, ? super Context> getIsMasterImplementation();

        public abstract CommandImplementation<ReplSetFreezeArgument, ReplSetFreezeReply, ? super Context> getReplSetFreezeImplementation();

        public abstract CommandImplementation<Empty, ReplicaSetConfig, ? super Context> getReplSetGetConfigImplementation();

        public abstract CommandImplementation<Empty, ReplSetGetStatusReply, ? super Context> getReplSetGetStatusImplementation();

        public abstract CommandImplementation<ReplicaSetConfig, Empty, ? super Context> getReplSetInitiateImplementation();

        public abstract CommandImplementation<Boolean, Empty, ? super Context> getReplSetMaintenanceImplementation();

        public abstract CommandImplementation<ReplSetReconfigArgument, Empty, ? super Context> getReplSetReconfigImplementation();

        public abstract CommandImplementation<ReplSetStepDownArgument, Empty, ? super Context> getReplSetStepDownImplementation();

        public abstract CommandImplementation<HostAndPort, ReplSetSyncFromReply, ? super Context> getReplSetSyncFromImplementation();

        private Map<Command<?,?>, CommandImplementation<?, ?, ? super Context>> createMap() {
            return ImmutableMap.<Command<?,?>, CommandImplementation<?, ?, ? super Context>>builder()
                    .put(ApplyOpsCommand.INSTANCE, getApplyOpsImplementation())
                    .put(IsMasterCommand.INSTANCE, getIsMasterImplementation())
                    .put(ReplSetFreezeCommand.INSTANCE, getReplSetFreezeImplementation())
                    .put(ReplSetGetConfigCommand.INSTANCE, getReplSetGetConfigImplementation())
                    .put(ReplSetGetStatusCommand.INSTANCE, getReplSetGetStatusImplementation())
                    .put(ReplSetInitiateCommand.INSTANCE, getReplSetInitiateImplementation())
                    .put(ReplSetMaintenanceCommand.INSTANCE, getReplSetMaintenanceImplementation())
                    .put(ReplSetReconfigCommand.INSTANCE, getReplSetReconfigImplementation())
                    .put(ReplSetStepDownCommand.INSTANCE, getReplSetStepDownImplementation())
                    .put(ReplSetSyncFromCommand.INSTANCE, getReplSetSyncFromImplementation())

                    .build();
        }

        @Override
        public Iterator<Entry<Command<?,?>, CommandImplementation<?, ?, ? super Context>>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
    
}
