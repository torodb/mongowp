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
import com.eightkdata.mongowp.server.api.deprecated.QueryCommandProcessor;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

/**
 * 
 * matteom
 */
public enum InternalQueryCommand implements QueryCommandProcessor.QueryCommand {
    _migrateClone,
    _recvChunkAbort,
    _recvChunkCommit,
    _recvChunkStart,
    _recvChunkStatus,
    //_replSetFresh,
    _transferMods,
    handshake(false),
    mapreduce_shardedfinish("mapreduce.shardedfinish"),
    replSetElect,
    replSetGetRBID,
    replSetHeartbeat,
    writeBacksQueued,
    writebacklisten
    ;
    
    private final String key;
    private final boolean adminOnly;
    
    private InternalQueryCommand() {
    	this.key = name();
    	this.adminOnly = true;
    }
    
    private InternalQueryCommand(boolean adminOnly) {
    	this.key = name();
    	this.adminOnly = adminOnly;
    }
    
    private InternalQueryCommand(String key) {
    	this.key = key;
    	this.adminOnly = true;
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
