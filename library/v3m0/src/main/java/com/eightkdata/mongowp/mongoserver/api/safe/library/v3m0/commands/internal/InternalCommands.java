
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.HandshakeCommand.HandshakeArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetElectCommand.ReplSetElectArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetElectCommand.ReplSetElectReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetFreshCommand.ReplSetFreshArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetFreshCommand.ReplSetFreshReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetGetRBIDCommand.ReplSetGetRBIDReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetUpdatePositionCommand.ReplSetUpdatePositionArgument;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 */
public class InternalCommands implements Iterable<Command> {

    private final ImmutableList<Command> commands = ImmutableList.<Command>of(
            HandshakeCommand.INSTANCE,
            ReplSetElectCommand.INSTANCE,
            ReplSetFreshCommand.INSTANCE,
            ReplSetGetRBIDCommand.INSTANCE,
            ReplSetHeartbeatCommand.INSTANCE,
            ReplSetUpdatePositionCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class InternalCommandsImplementationsBuilder implements Iterable<Entry<Command<?,?>, CommandImplementation>> {

        public abstract CommandImplementation<HandshakeArgument, Empty> getHandshakeImplementation();

        public abstract CommandImplementation<ReplSetElectArgument, ReplSetElectReply> getReplSetElectImplementation();

        public abstract CommandImplementation<ReplSetFreshArgument, ReplSetFreshReply> getReplSetFreshImplementation();

        public abstract CommandImplementation<Empty, ReplSetGetRBIDReply> getReplSetGetRBIDImplementation();

        public abstract CommandImplementation<ReplSetHeartbeatArgument, ReplSetHeartbeatReply> getReplSetHeartbeatImplementation();

        public abstract CommandImplementation<ReplSetUpdatePositionArgument, Empty> getReplSetUpdateImplementation();

        private Map<Command<?,?>, CommandImplementation> createMap() {
            return ImmutableMap.<Command<?,?>, CommandImplementation>builder()
                    .put(HandshakeCommand.INSTANCE, getHandshakeImplementation())
                    .put(ReplSetElectCommand.INSTANCE, getReplSetElectImplementation())
                    .put(ReplSetFreshCommand.INSTANCE, getReplSetFreshImplementation())
                    .put(ReplSetGetRBIDCommand.INSTANCE, getReplSetGetRBIDImplementation())
                    .put(ReplSetHeartbeatCommand.INSTANCE, getReplSetHeartbeatImplementation())
                    .put(ReplSetUpdatePositionCommand.INSTANCE, getReplSetUpdateImplementation())
                    .build();
        }

        @Override
        public Iterator<Entry<Command<?,?>, CommandImplementation>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
    
}
