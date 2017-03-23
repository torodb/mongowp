/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.Status;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.DeleteMessage;
import com.eightkdata.mongowp.messages.request.GetMoreMessage;
import com.eightkdata.mongowp.messages.request.InsertMessage;
import com.eightkdata.mongowp.messages.request.KillCursorsMessage;
import com.eightkdata.mongowp.messages.request.UpdateMessage;
import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.server.api.pojos.QueryRequest;

public interface SafeRequestProcessor<C extends Connection> extends CommandExecutor<C> {

  public C openConnection();

  public CommandLibrary getCommandsLibrary();

  @Override
  public <A, R> Status<R> execute(Request request,
      Command<? super A, ? super R> command, A arg, C context);

  public ReplyMessage query(C connection, Request req, int requestId, QueryRequest queryRequest)
      throws MongoException;

  public ReplyMessage getMore(C connection, Request req, int requestId, GetMoreMessage moreMessage)
      throws MongoException;

  public void killCursors(C connection, Request req, KillCursorsMessage killCursorsMessage) throws
      MongoException;

  public void insert(C connection, Request req, InsertMessage insertMessage) throws MongoException;

  public void update(C connection, Request req, UpdateMessage updateMessage) throws MongoException;

  public void delete(C connection, Request req, DeleteMessage deleteMessage) throws MongoException;
}
