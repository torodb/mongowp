package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.exceptions.MongoException;
import javax.annotation.Nonnull;

/**
 *
 */
public interface CommandImplementation<Arg, Result> {

    @Nonnull
    public CommandResult<Result> apply(Command<? super Arg, ? super Result> command, CommandRequest<Arg> req) throws MongoException;

}
