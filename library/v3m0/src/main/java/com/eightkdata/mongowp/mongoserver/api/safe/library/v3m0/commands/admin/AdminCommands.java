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
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CollModCommand.CollModArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CollModCommand.CollModResult;
import java.util.Iterator;
import java.util.Map;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateCollectionCommand.CreateCollectionArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateIndexesCommand.CreateIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateIndexesCommand.CreateIndexesResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.DropIndexesCommand.DropIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.DropIndexesCommand.DropIndexesResult;
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
            RenameCollectionCommand.INSTANCE
    );

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public static abstract class AdminCommandsImplementationsBuilder<Context> implements Iterable<Map.Entry<Command<?,?>, CommandImplementation<?, ?, ? super Context>>> {

        public abstract CommandImplementation<CollModArgument, CollModResult, ? super Context> getCollModImplementation();

        public abstract CommandImplementation<ListCollectionsArgument, ListCollectionsResult, ? super Context> getListCollectionsImplementation();

        public abstract CommandImplementation<Empty, Empty, ? super Context> getDropDatabaseImplementation();

        public abstract CommandImplementation<CollectionCommandArgument, Empty, ? super Context> getDropCollectionImplementation();

        public abstract CommandImplementation<CreateCollectionArgument, Empty, ? super Context> getCreateCollectionImplementation();

        public abstract CommandImplementation<ListIndexesArgument, ListIndexesResult, ? super Context> getListIndexesImplementation();

        public abstract CommandImplementation<CreateIndexesArgument, CreateIndexesResult, ? super Context> getCreateIndexesImplementation();

        public abstract CommandImplementation<DropIndexesArgument, DropIndexesResult, ? super Context> getDropIndexesImplementation();

        public abstract CommandImplementation<RenameCollectionArgument, Empty, ? super Context> getRenameCollectionImplementation();

        private Map<Command<?,?>, CommandImplementation<?, ?, ? super Context>> createMap() {
            return ImmutableMap.<Command<?,?>, CommandImplementation<?, ?, ? super Context>>builder()
                    .put(CollModCommand.INSTANCE, getCollModImplementation())
                    .put(ListCollectionsCommand.INSTANCE, getListCollectionsImplementation())
                    .put(DropDatabaseCommand.INSTANCE, getDropDatabaseImplementation())
                    .put(DropCollectionCommand.INSTANCE, getDropCollectionImplementation())
                    .put(CreateCollectionCommand.INSTANCE, getCreateCollectionImplementation())
                    .put(ListIndexesCommand.INSTANCE, getListIndexesImplementation())
                    .put(CreateIndexesCommand.INSTANCE, getCreateIndexesImplementation())
                    .put(DropIndexesCommand.INSTANCE, getDropIndexesImplementation())
                    .put(RenameCollectionCommand.INSTANCE, getRenameCollectionImplementation())
                    .build();
        }

        @Override
        public Iterator<Map.Entry<Command<?,?>, CommandImplementation<?, ?, ? super Context>>> iterator() {
            return createMap().entrySet().iterator();
        }

    }
}
