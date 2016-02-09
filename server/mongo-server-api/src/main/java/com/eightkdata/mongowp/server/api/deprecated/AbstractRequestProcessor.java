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

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.exceptions.CommandNotFoundException;
import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.messages.request.RequestOpCode;
import com.eightkdata.mongowp.server.api.deprecated.QueryCommandProcessor.QueryCommand;
import com.eightkdata.mongowp.server.api.deprecated.QueryCommandProcessor.QueryCommandGroup;
import com.eightkdata.mongowp.server.api.deprecated.commands.QueryReply;
import com.eightkdata.mongowp.server.api.deprecated.commands.QueryRequest;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import com.eightkdata.mongowp.server.callback.RequestProcessor;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 *
 */
public abstract class AbstractRequestProcessor implements RequestProcessor {
    public static final String QUERY_MESSAGE_COMMAND_COLLECTION = "$cmd";
    public static final String QUERY_MESSAGE_ADMIN_DATABASE = "admin";
    
    public static final AttributeKey<QueryCommand> QUERY_COMMAND = AttributeKey.valueOf("queryCommand");

    private final QueryCommandProcessor queryCommandProcessor;
    private final MetaCommandProcessor metaCommandProcessor;

    @Inject
    public AbstractRequestProcessor(
            @Nonnull QueryCommandProcessor queryCommandProcessor,
            @Nonnull MetaCommandProcessor metaCommandProcessor
    ) {
        this.queryCommandProcessor = queryCommandProcessor;
        this.metaCommandProcessor = metaCommandProcessor;
    }

    @Override
    public void queryMessage(@Nonnull QueryMessage queryMessage, @Nonnull MessageReplier messageReplier)
            throws Exception {
        AttributeMap attributeMap = messageReplier.getAttributeMap();
        RequestBaseMessage requestBaseMessage = queryMessage.getBaseMessage();
        BsonDocument query = queryMessage.getQuery();

        attributeMap.attr(QUERY_COMMAND).set(null);
        if (QUERY_MESSAGE_COMMAND_COLLECTION.equals(queryMessage.getCollection())) {
            QueryCommand queryCommand = QueryCommandGroup.byQueryDocument(query);
            if (null == queryCommand) {
                noSuchCommand(query, messageReplier);
                return;
            }

            if (queryCommand.isAdminOnly()
                    && !QUERY_MESSAGE_ADMIN_DATABASE.equals(queryMessage.getDatabase())) {
                adminOnlyCommand(queryCommand, messageReplier);
                return;
            }

            attributeMap.attr(QUERY_COMMAND).set(queryCommand);
            queryCommand.call(
                    requestBaseMessage,
                    query,
                    new QueryCommandProcessor.ProcessorCaller(
                            queryMessage.getDatabase(),
                            queryCommandProcessor,
                            metaCommandProcessor,
                            messageReplier
                    )
            );
        }
        else {
            query(queryMessage, messageReplier);
        }
    }

    @Override
	public boolean handleError(@Nonnull RequestOpCode requestOpCode, @Nonnull MessageReplier messageReplier, @Nonnull Throwable throwable) throws Exception {
        throw new UnsupportedOperationException();
//    	AttributeMap attributeMap = messageReplier.getAttributeMap();
//		if (requestOpCode == RequestOpCode.OP_QUERY) {
//			if (attributeMap.attr(QUERY_COMMAND).get() != null) {
//				queryCommandProcessor.handleError(attributeMap.attr(QUERY_COMMAND).get(), messageReplier, throwable);
//				return true;
//			}
//		}
//
//		return false;
	}

	public void query(@Nonnull QueryMessage queryMessage, @Nonnull MessageReplier messageReplier) throws Exception {
        QueryRequest.Builder requestBuilder = new QueryRequest.Builder(
                queryMessage.getDatabase(),
                messageReplier.getAttributeMap()
        );
        QueryOptions queryOptions = queryMessage.getQueryOptions();
        requestBuilder.setCollection(queryMessage.getCollection())
                .setQuery(extractQuery(queryMessage.getQuery()))
                .setProjection(null)
                .setNumberToSkip(queryMessage.getNumberToSkip())
                .setLimit(queryMessage.getNumberToReturn())
                .setAwaitData(queryOptions.isAwaitData())
                .setExhaust(queryOptions.isExhaust())
                .setNoCursorTimeout(queryOptions.isNoCursorTimeout())
                .setOplogReplay(queryOptions.isOplogReplay())
                .setPartial(queryOptions.isPartial())
                .setSlaveOk(queryOptions.isSlaveOk())
                .setTailable(queryOptions.isTailable());
        
        if (requestBuilder.getLimit() < 0) {
            requestBuilder.setAutoclose(true);
            requestBuilder.setLimit(-requestBuilder.getLimit());
        }
        else if (requestBuilder.getLimit() == 1) {
            requestBuilder.setAutoclose(true);
        }
        
        QueryReply reply;
        if (metaCommandProcessor.isMetaQuery(queryMessage)) {
            reply = metaCommandProcessor.query(requestBuilder.build());
        }
        else {
            reply = query(requestBuilder.build());
        }
        reply.reply(messageReplier);
    }
	
	public abstract void noSuchCommand(@Nonnull BsonDocument query, @Nonnull MessageReplier messageReplier) throws CommandNotFoundException;
	
	public abstract void adminOnlyCommand(@Nonnull QueryCommand queryCommand, @Nonnull MessageReplier messageReplier) throws Exception;

    public abstract QueryReply query(QueryRequest build) throws Exception;
    
    private BsonDocument extractQuery(BsonDocument query) {
        for (Entry<?> entrySet : query) {
            String key = entrySet.getKey();
            if ("query".equals(key) || "$query".equals(key)) {
    			BsonValue queryObject = entrySet.getValue();
    			if (queryObject != null && queryObject.isDocument()) {
    				return query.asDocument();
    			}
    		}
        }
        return query;
    }
}
