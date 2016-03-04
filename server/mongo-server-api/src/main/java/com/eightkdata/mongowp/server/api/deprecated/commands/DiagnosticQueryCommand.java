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


package com.eightkdata.mongowp.server.api.deprecated.commands;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.server.api.deprecated.QueryCommandProcessor;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

/**
 * 
 * matteom
 */
public enum DiagnosticQueryCommand implements QueryCommandProcessor.QueryCommand {
    availableQueryOptions,
    buildInfo {
        @Override
        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) {
        	caller.buildInfo();
        }
    },
    //collStats, //removed: use the new API
    connPoolStats,
    cursorInfo,
    dataSize,
    dbHash,
    dbStats,
    diagLogging(true),
    driverOIDTest,
    features,
    getCmdLineOpts(true),
    getLog(true) {
        @Override
        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws BadValueException {
            String log = query.get("getLog").asString().getValue();
            QueryCommandProcessor.GetLogType getLogType = QueryCommandProcessor.GetLogType.getByLog(log);
            if(null == getLogType) {
                throw new BadValueException("The value " + log + " is not a spected log value");
            } else {
                caller.getLog(getLogType);
            }
        }
    },
    hostInfo,
    //indexStats,
    //isSelf,
    listCommands,
    listDatabases(true) {
        
        @Override
        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
            caller.listDatabases();
        }
    },
    //netstat,
    ping {

        @Override
        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) {
            caller.ping();
        }
        
    },
    profile,
    serverStatus,
    shardConnPoolStats,
    top(true),
    validate {
        @Override
        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
            caller.validate(caller.getDatabase(), query);
        }
    },
    whatsmyuri {
        @Override
        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) {
            caller.whatsmyuri(requestBaseMessage.getClientAddressString(), requestBaseMessage.getClientPort());
        }
    }
    ;
    
    private final String key;
    private final boolean adminOnly;
    
    private DiagnosticQueryCommand() {
    	this.key = name();
    	this.adminOnly = false;
    }
    
    private DiagnosticQueryCommand(boolean adminOnly) {
    	this.key = name();
    	this.adminOnly = adminOnly;
    }
    
    private DiagnosticQueryCommand(String key) {
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
}
