
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandImplementation;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GetLastErrorCommand.GetLastErrorArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GetLastErrorCommand.GetLastErrorReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertReply;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 */
public class GeneralCommands implements Iterable<Command> {

    private final ImmutableList<Command> commands = ImmutableList.<Command>of(
            InsertCommand.INSTANCE,
            GetLastErrorCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class GeneralCommandsImplementationsBuilder implements Iterable<Map.Entry<Command, CommandImplementation>> {

        public abstract CommandImplementation<GetLastErrorArgument, GetLastErrorReply> getGetLastErrrorImplementation();

        public abstract CommandImplementation<InsertArgument, InsertReply> getInsertImplementation();

        private Map<Command, CommandImplementation> createMap() {
            return ImmutableMap.<Command, CommandImplementation>builder()
                    .put(InsertCommand.INSTANCE, getInsertImplementation())
                    .put(GetLastErrorCommand.INSTANCE, getGetLastErrrorImplementation())
                    .build();
        }

        @Override
        public Iterator<Entry<Command, CommandImplementation>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
    
}
