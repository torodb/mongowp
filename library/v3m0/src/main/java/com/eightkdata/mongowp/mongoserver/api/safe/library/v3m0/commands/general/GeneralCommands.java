
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.DeleteCommand.DeleteArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.FindCommand.FindArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.FindCommand.FindResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GetLastErrorCommand.GetLastErrorArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GetLastErrorCommand.GetLastErrorReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.UpdateCommand.UpdateArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.UpdateCommand.UpdateResult;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
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
            DeleteCommand.INSTANCE,
            InsertCommand.INSTANCE,
            GetLastErrorCommand.INSTANCE,
            UpdateCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class GeneralCommandsImplementationsBuilder<Context> implements Iterable<Map.Entry<Command<?,?>, CommandImplementation>> {

        public abstract CommandImplementation<GetLastErrorArgument, GetLastErrorReply, ? super Context> getGetLastErrrorImplementation();

        public abstract CommandImplementation<InsertArgument, InsertResult, ? super Context> getInsertImplementation();

        public abstract CommandImplementation<DeleteArgument, Long, ? super Context> getDeleteImplementation();

        public abstract CommandImplementation<FindArgument, FindResult, ? super Context> getFindImplementation();

        public abstract CommandImplementation<UpdateArgument, UpdateResult, ? super Context> getUpdateImplementation();

        private Map<Command<?,?>, CommandImplementation> createMap() {
            return ImmutableMap.<Command<?,?>, CommandImplementation>builder()
                    .put(DeleteCommand.INSTANCE, getDeleteImplementation())
                    .put(DeleteCommand.INSTANCE, getFindImplementation())
                    .put(InsertCommand.INSTANCE, getInsertImplementation())
                    .put(GetLastErrorCommand.INSTANCE, getGetLastErrrorImplementation())
                    .put(UpdateCommand.INSTANCE, getUpdateImplementation())
                    .build();
        }

        @Override
        public Iterator<Entry<Command<?,?>, CommandImplementation>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
    
}
