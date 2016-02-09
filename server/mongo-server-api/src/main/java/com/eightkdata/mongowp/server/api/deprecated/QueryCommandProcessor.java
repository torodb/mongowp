/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */


package com.eightkdata.mongowp.server.api.deprecated;

import com.eightkdata.mongowp.server.api.deprecated.commands.AdministrationQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.ShardingQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.ReplicationQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.AuthenticationQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.AggregationQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.QueryAndWriteOperationsQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.GeospatialQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.RoleManagementQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.SystemEventsAuditingQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.UserManagementQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.TestingQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.DiagnosticQueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.commands.InternalQueryCommand;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;


/**
 *
 */
public interface QueryCommandProcessor {
    
    public enum QueryCommandGroup {
		Aggregation(AggregationQueryCommand.values()),
		Geospatial(GeospatialQueryCommand.values()),
		QueryAndWriteOperations(QueryAndWriteOperationsQueryCommand.values()),
		Authentication(AuthenticationQueryCommand.values()),
		UserManagement(UserManagementQueryCommand.values()),
		RoleManagement(RoleManagementQueryCommand.values()),
		Replication(ReplicationQueryCommand.values()),
		Sharding(ShardingQueryCommand.values()),
		Administration(AdministrationQueryCommand.values()),
		Diagnostic(DiagnosticQueryCommand.values()),
		Internal(InternalQueryCommand.values()),
		Testing(TestingQueryCommand.values()),
		SystemEventsAuditing(SystemEventsAuditingQueryCommand.values());
		
		private final QueryCommand[] queryCommands;
		
		private QueryCommandGroup(QueryCommand[] queryCommands) {
			this.queryCommands = queryCommands;
		}

	    private static final Map<String,QueryCommand> COMMANDS_MAP = new HashMap<>();
	    static {
	        for(QueryCommandGroup commandGroup : values()) {
		        for(QueryCommand command : commandGroup.queryCommands) {
		        	// 	Some driver use lower case version of the command so we must take it into account
		        	String key = command.getKey().toLowerCase(Locale.ROOT);
		        	if (COMMANDS_MAP.containsKey(key)) {
		        		throw new RuntimeException("Key " + key + " is not unique, found in enum " + 
		        				COMMANDS_MAP.get(key).getClass().getName() + " and in enum " + 
		        				command.getClass().getName() + ". Fix it!");
		        	}
		        	COMMANDS_MAP.put(key, command);
		        }
	        }
	    }

	    /**
	     *
	     * @param queryDocument
	     * @return
	     * @throws IllegalArgumentException If queryDocument is null
	     */
	    @Nullable
	    public static QueryCommand byQueryDocument(@Nonnull BsonDocument queryDocument) {
	        Preconditions.checkNotNull(queryDocument);

            UnmodifiableIterator<Entry<?>> it = queryDocument.iterator();
            if (it.hasNext()) {
                String possibleCommand = it.next().getKey();
                // Some driver use lower case version of the command so we must take it into account
                possibleCommand = possibleCommand.toLowerCase(Locale.ENGLISH);

                return COMMANDS_MAP.get(possibleCommand);
            }
            return null;
	    }
	}
	
    public interface QueryCommand {
        /**
         *
         * @param requestBaseMessage
         * @param query
         * @param caller
         * @throws java.lang.Exception
         * @throws java.lang.IllegalArgumentException If either query or caller are null
         */
        public void call(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull ProcessorCaller caller) throws Exception;
        
        public String getKey();
        
        public boolean isAdminOnly();
    }

    public class ProcessorCaller extends QueryCommandProcessorCaller {
        @Nonnull private final QueryCommandProcessor queryCommandProcessor;
        @Nonnull private final MetaCommandProcessor metaQueryProcessor;

        @Inject
        public ProcessorCaller(
                @Nonnull String database,
                @Nonnull QueryCommandProcessor queryCommandProcessor, 
                @Nonnull MetaCommandProcessor metaQueryProcessor,
                @Nonnull MessageReplier messageReplier) {
            super(database, messageReplier);
            this.queryCommandProcessor = queryCommandProcessor;
            this.metaQueryProcessor = metaQueryProcessor;
        }
        
        public void createIndexes(@Nonnull BsonDocument document) throws Exception {
        	queryCommandProcessor.createIndexes(document, messageReplier);
        }
        
        public void create(@Nonnull BsonDocument document) throws Exception {
        	queryCommandProcessor.create(document, messageReplier);
        }
        
        public void deleteIndexes(BsonDocument query) throws Exception {
            queryCommandProcessor.deleteIndexes(query, messageReplier);
        }

        public void getLastError(
        		@Nullable BsonValue w, boolean j, boolean fsync,
                @Nonnegative @Nullable int wtimeout
        ) throws Exception {
            queryCommandProcessor.getLastError(w, j, fsync, wtimeout, messageReplier);
        }
        
        public void validate(@Nonnull String database, @Nonnull BsonDocument document) throws Exception {
        	queryCommandProcessor.validate(database, document, messageReplier);
        }
        
        public void whatsmyuri(@Nonnull String host, @Nonnull int port) {
        	queryCommandProcessor.whatsmyuri(host, port, messageReplier);
        }
        
        public void isMaster() {
        	queryCommandProcessor.isMaster(messageReplier);
        }
        
        public void replSetGetStatus() {
        	queryCommandProcessor.replSetGetStatus(messageReplier);
        }
        
        public void buildInfo() {
        	queryCommandProcessor.buildInfo(messageReplier);
        }
        
        public void ping() {
            queryCommandProcessor.ping(messageReplier);
        }
        
        public void getLog(@Nonnull GetLogType log) {
        	queryCommandProcessor.getLog(log, messageReplier);
        }

        public void unimplemented(@Nonnull QueryCommand userCommand) throws Exception {
        	queryCommandProcessor.unimplemented(userCommand, messageReplier);
        }

        public void listDatabases() throws Exception {
            queryCommandProcessor.listDatabases(messageReplier);
        }
        
        public void getnonce() {
            queryCommandProcessor.getnonce(messageReplier);
        }

        public void listCollections(BsonDocument query) throws Exception {
            queryCommandProcessor.listCollections(messageReplier, query);
        }

        public void listIndexes(String collection) throws Exception {
            queryCommandProcessor.listIndexes(messageReplier, collection);
        }
    }

    public void deleteIndexes(BsonDocument query, MessageReplier messageReplier) throws Exception;

    public void createIndexes(@Nonnull BsonDocument document, @Nonnull MessageReplier messageReplier) throws Exception;
    
    public void create(@Nonnull BsonDocument document, @Nonnull MessageReplier messageReplier) throws Exception;
    
    /**
     * Either w or majority will be set, but not both at the same time.
     * @param j
     * @param w
     * @param fsync
     * @param wtimeout
     * @param messageReplier
     */
    public void getLastError(
    		@Nullable Object w, boolean j, boolean fsync,
            @Nonnegative @Nullable int wtimeout, @Nonnull MessageReplier messageReplier
    ) throws Exception;

	public void validate(@Nonnull String database, @Nonnull BsonDocument document, @Nonnull MessageReplier messageReplier) throws Exception;

    public void ping(MessageReplier messageReplier);

    public void listDatabases(MessageReplier messageReplier) throws Exception;
	
    public void whatsmyuri(@Nonnull String host, @Nonnull int port, @Nonnull MessageReplier messageReplier);

    public void replSetGetStatus(@Nonnull MessageReplier messageReplier);
    
    public void listCollections(@Nonnull MessageReplier messageReplier, BsonDocument query) throws Exception;
    
    public void listIndexes(MessageReplier messageReplier, String collection) throws Exception;

    public enum GetLogType {
        global("global"),
        rs("rs"),
        startupWarnings("startupWarnings"),
        all("*");

        private final String logFilter;

        GetLogType(String logFilter) {
            this.logFilter = logFilter;
        }

        public String getLogFilter() {
            return logFilter;
        }

        private static final Map<String,GetLogType> TYPES_MAP = new HashMap<>(values().length);
        static {
            for(GetLogType getLogType : values()) {
                TYPES_MAP.put(getLogType.logFilter, getLogType);
            }
        }

        @Nullable public static GetLogType getByLog(String log) {
            return TYPES_MAP.get(log);
        }
    }
    public void getLog(@Nonnull GetLogType log, @Nonnull MessageReplier messageReplier);
    
    public void isMaster(@Nonnull MessageReplier messageReplier);
    
    public void buildInfo(@Nonnull MessageReplier messageReplier);
    
    public void unimplemented(@Nonnull QueryCommand userCommand, @Nonnull MessageReplier messageReplier) throws Exception;
    
    public void getnonce(MessageReplier messageReplier);
    
}
