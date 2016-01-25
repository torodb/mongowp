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


package com.eightkdata.mongowp.mongoserver.api.deprecated.commands;

import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.mongoserver.api.deprecated.QueryCommandProcessor;
import com.eightkdata.mongowp.mongoserver.api.deprecated.QueryCommandProcessor.ProcessorCaller;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 * 
 * matteom
 */
public enum AdministrationQueryCommand implements QueryCommandProcessor.QueryCommand {
    //clean,
//clean,
    cloneCollectionAsCapped,
    cloneCollection,
    clone,
    closeAllDatabases(true),
    collMod,
    compact,
    connPoolSync,
    convertToCapped,
    copydb(true),
    createIndexes {
		@Override
		public void doCall(RequestBaseMessage queryMessage, BsonDocument query, ProcessorCaller caller) throws Exception {
			caller.createIndexes(query);
		}
    },
    create {
    	@Override
    	public void doCall(RequestBaseMessage queryMessage, BsonDocument query, ProcessorCaller caller) throws Exception {
			caller.create(query);
    	}
    },
    dropDatabase,
    deleteIndexes {
        @Override
		public void doCall(RequestBaseMessage queryMessage, BsonDocument query, ProcessorCaller caller) throws Exception {
			caller.deleteIndexes(query);
		}
    },
    drop {
		@Override
		public void doCall(RequestBaseMessage queryMessage, BsonDocument query, ProcessorCaller caller) throws Exception {
			caller.drop(query);
		}
    },
    filemd5,
    fsync(true),
    getParameter(true),
    listCollections {
		@Override
		public void doCall(RequestBaseMessage queryMessage, BsonDocument query, ProcessorCaller caller) throws Exception {
			caller.listCollections(query);
		}
    },
    listIndexes {

        @Override
        public void doCall(RequestBaseMessage queryMessage, BsonDocument query, ProcessorCaller caller) throws Exception {
            BsonValue value = query.get("listIndexes");
            if (!value.isString()) {
                throw new RuntimeException("Argument to listIndexes must be of type String");
            }
            caller.listIndexes(value.asString().getValue());
        }
    },
    logRotate(true),
    reIndex,
    renameCollection(true),
    repairDatabase,
    setParameter(true),
    shutdown(true),
    touch
    ;
    
    private final String key;
    private final boolean adminOnly;
    
    private AdministrationQueryCommand() {
    	this.key = name();
    	this.adminOnly = false;
    }
    
    private AdministrationQueryCommand(boolean adminOnly) {
    	this.key = name();
    	this.adminOnly = adminOnly;
    }
    
    private AdministrationQueryCommand(String key) {
    	this.key = key;
    	this.adminOnly = false;
    }
    
    @Override
    public String getKey() {
    	return key;
    }
    
    @Override
    public boolean isAdminOnly() {
    	return adminOnly;
    }

    public void doCall(@Nonnull RequestBaseMessage queryMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
    	caller.unimplemented(this);
    }

    @Override
    public void call(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
        Preconditions.checkNotNull(query);
        Preconditions.checkNotNull(caller);

        doCall(requestBaseMessage, query, caller);
    }

    private static final Map<String,AdministrationQueryCommand> COMMANDS_MAP = new HashMap<String, AdministrationQueryCommand>(values().length);
    static {
        for(AdministrationQueryCommand command : values()) {
            // Some driver use lower case version of the command so we must take it into account
            COMMANDS_MAP.put(command.key.toLowerCase(Locale.ROOT), command);
        }
    }

    /**
     *
     * @param queryDocument
     * @return
     * @throws IllegalArgumentException If queryDocument is null
     */
    @Nullable
    public static AdministrationQueryCommand byQueryDocument(@Nonnull BsonDocument queryDocument) {
        Preconditions.checkNotNull(queryDocument);

        // TODO: if might be worth to improve searching taking into account the # of keys of the document,
        // matching it with the # of args of the commands, which could be registered as enum fields
        for(String possibleCommand : queryDocument.keySet()) {
            // Some driver use lower case version of the command so we must take it into account
        	possibleCommand = possibleCommand.toLowerCase(Locale.ROOT);
            if(COMMANDS_MAP.containsKey(possibleCommand)) {
                return COMMANDS_MAP.get(possibleCommand);
            }
        }

        return null;
    }
}
