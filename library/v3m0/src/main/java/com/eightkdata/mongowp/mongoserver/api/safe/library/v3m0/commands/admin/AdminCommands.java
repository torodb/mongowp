
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import java.util.Iterator;
import java.util.Map;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateCollectionCommand.CreateCollectionArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateIndexesCommand.CreateIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateIndexesCommand.CreateIndexesResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.DropIndexesCommand.DropIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListCollectionsCommand.ListCollectionsArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListCollectionsCommand.ListCollectionsResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListIndexesCommand.ListIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.ListIndexesCommand.ListIndexesResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.RenameCollectionCommand.RenameCollectionArgument;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.server.api.impl.CollectionCommandArgument;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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
            CreateIndexesCommand.INSTANCE,
            DropIndexesCommand.INSTANCE,
            DeleteIndexesCommand.INSTANCE,
            RenameCollectionCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class AdminCommandsImplementationsBuilder<Context> implements Iterable<Map.Entry<Command<?,?>, CommandImplementation<?, ?, ? super Context>>> {

        public abstract CommandImplementation<ListCollectionsArgument, ListCollectionsResult, ? super Context> getListCollectionsImplementation();

        public abstract CommandImplementation<Empty, Empty, ? super Context> getDropDatabaseImplementation();

        public abstract CommandImplementation<CollectionCommandArgument, Empty, ? super Context> getDropCollectionImplementation();

        public abstract CommandImplementation<CreateCollectionArgument, Empty, ? super Context> getCreateCollectionImplementation();

        public abstract CommandImplementation<ListIndexesArgument, ListIndexesResult, ? super Context> getListIndexesImplementation();

        public abstract CommandImplementation<CreateIndexesArgument, CreateIndexesResult, ? super Context> getCreateIndexesImplementation();

        public abstract CommandImplementation<DropIndexesArgument, Empty, ? super Context> getDropIndexesImplementation();

        public abstract CommandImplementation<RenameCollectionArgument, Empty, ? super Context> getRenameCollectionImplementation();

        private Map<Command<?,?>, CommandImplementation<?, ?, ? super Context>> createMap() {
            return ImmutableMap.<Command<?,?>, CommandImplementation<?, ?, ? super Context>>builder()
                    .put(ListCollectionsCommand.INSTANCE, getListCollectionsImplementation())
                    .put(DropDatabaseCommand.INSTANCE, getDropDatabaseImplementation())
                    .put(DropCollectionCommand.INSTANCE, getDropCollectionImplementation())
                    .put(CreateCollectionCommand.INSTANCE, getCreateCollectionImplementation())
                    .put(ListIndexesCommand.INSTANCE, getListIndexesImplementation())
                    .put(CreateIndexesCommand.INSTANCE, getCreateIndexesImplementation())
                    .put(DropIndexesCommand.INSTANCE, getDropIndexesImplementation())
                    .put(DeleteIndexesCommand.INSTANCE, getDropIndexesImplementation())
                    .put(RenameCollectionCommand.INSTANCE, getRenameCollectionImplementation())
                    .build();
        }

        @Override
        public Iterator<Map.Entry<Command<?,?>, CommandImplementation<?, ?, ? super Context>>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
}
