/*
 * MongoWP - Mongo Server: API
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
package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.Status;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.*;
import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.server.api.pojos.QueryRequest;

/**
 *
 */
public interface SafeRequestProcessor<C extends Connection> extends CommandsExecutor<C> {

    public C openConnection();

    public CommandsLibrary getCommandsLibrary();

    @Override
    public <Arg, Result> Status<Result> execute(Request request, Command<? super Arg, ? super Result> command, Arg arg, C context);
    
    public ReplyMessage query(C connection, Request req, int requestId, QueryRequest queryRequest) throws MongoException;

    public ReplyMessage getMore(C connection, Request req, int requestId, GetMoreMessage moreMessage) throws MongoException;

    public void killCursors(C connection, Request req, KillCursorsMessage killCursorsMessage) throws MongoException;

    public void insert(C connection, Request req, InsertMessage insertMessage) throws MongoException;

    public void update(C connection, Request req, UpdateMessage updateMessage) throws MongoException;

    public void delete(C connection, Request req, DeleteMessage deleteMessage) throws MongoException;
}
