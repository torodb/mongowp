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
    
    public ReplyMessage query(C connection, Request req, QueryRequest build) throws MongoException;

    public ReplyMessage getMore(C connection, Request req, GetMoreMessage moreMessage) throws MongoException;

    public void killCursors(C connection, Request req, KillCursorsMessage killCursorsMessage) throws MongoException;

    public void insert(C connection, Request req, InsertMessage insertMessage) throws MongoException;

    public void update(C connection, Request req, UpdateMessage updateMessage) throws MongoException;

    public void delete(C connection, Request req, DeleteMessage deleteMessage) throws MongoException;
}
