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
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonInt32;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.server.api.deprecated.QueryCommandProcessor;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

/**
 * 
 * matteom
 */
public enum QueryAndWriteOperationsQueryCommand implements QueryCommandProcessor.QueryCommand {
//    delete {
//        @Override
//        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
//        	caller.delete(query);
//        }
//    },
    eval,
    findAndModify,
    getLastError {
        @Override
        public void doCall(@Nonnull RequestBaseMessage queryMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
            // TODO: no data type checking is being performed. ClassCastExceptions may be thrown
            BsonValue w = query.containsKey("w") ? query.get("w") : PrimitiveBsonInt32.newInstance(1);
            boolean j = query.containsKey("j") ? query.get("j").asBoolean().getValue() : false;
            boolean fsync = query.containsKey("fsync") ? query.get("fsync").asBoolean().getValue() : false;
            int wtimeout = query.containsKey("wtimeout") ? query.get("wtimeout").asNumber().intValue() : 0;
            caller.getLastError(w, j, fsync, wtimeout);
        }
    },
    getPrevError,
//    insert {
//        @Override
//        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
//        	caller.insert(query);
//        }
//    },
    parallelCollectionScan,
    resetError,
    text,
//    update {
//        @Override
//        public void doCall(@Nonnull RequestBaseMessage requestBaseMessage, @Nonnull BsonDocument query, @Nonnull QueryCommandProcessor.ProcessorCaller caller) throws Exception {
//        	caller.update(query);
//        }
//    }
    ;
    
    private final String key;
    
    private QueryAndWriteOperationsQueryCommand() {
    	this.key = name();
    }
    
    private QueryAndWriteOperationsQueryCommand(String key) {
    	this.key = key;
    }
    
    @Override
    public String getKey() {
    	return key;
    }
    
    @Override
    public boolean isAdminOnly() {
    	return false;
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
