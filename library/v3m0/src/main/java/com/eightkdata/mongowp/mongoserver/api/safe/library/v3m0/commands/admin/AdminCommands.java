
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateCollectionCommand.CreateCollectionArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateIndexesCommand.CreateIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateIndexesCommand.CreateIndexesResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListCollectionsCommand.ListCollectionsArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListCollectionsCommand.ListCollectionsResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListIndexesCommand.ListIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListIndexesCommand.ListIndexesResult;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.server.api.impl.CollectionCommandArgument;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class AdminCommands implements Iterable<Command> {

    private final ImmutableList<Command> commands = ImmutableList.<Command>of(
            ListCollectionsCommand.INSTANCE,
            DropDatabaseCommand.INSTANCE,
            DropCollectionCommand.INSTANCE,
            CreateCollectionCommand.INSTANCE,
            ListIndexesCommand.INSTANCE,
            CreateIndexesCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class AdminCommandsImplementationsBuilder<Context> implements Iterable<Map.Entry<Command<?,?>, CommandImplementation>> {

        public abstract CommandImplementation<ListCollectionsArgument, ListCollectionsResult, Context> getListCollectionsImplementation();

        public abstract CommandImplementation<Empty, Empty, Context> getDropDatabaseImplementation();

        public abstract CommandImplementation<CollectionCommandArgument, Empty, Context> getDropCollectionImplementation();

        public abstract CommandImplementation<CreateCollectionArgument, Empty, Context> getCreateCollectionImplementation();

        public abstract CommandImplementation<ListIndexesArgument, ListIndexesResult, Context> getListIndexesImplementation();

        public abstract CommandImplementation<CreateIndexesArgument, CreateIndexesResult, Context> getCreateIndexesImplementation();

        private Map<Command<?,?>, CommandImplementation> createMap() {
            return ImmutableMap.<Command<?,?>, CommandImplementation>builder()
                    .put(ListCollectionsCommand.INSTANCE, getListCollectionsImplementation())
                    .put(DropDatabaseCommand.INSTANCE, getDropDatabaseImplementation())
                    .put(DropCollectionCommand.INSTANCE, getDropCollectionImplementation())
                    .put(CreateCollectionCommand.INSTANCE, getCreateCollectionImplementation())
                    .put(ListIndexesCommand.INSTANCE, getListIndexesImplementation())
                    .put(CreateIndexesCommand.INSTANCE, getCreateIndexesImplementation())
                    .build();
        }

        @Override
        public Iterator<Map.Entry<Command<?,?>, CommandImplementation>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
}
