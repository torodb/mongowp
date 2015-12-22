package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoException;
import javax.annotation.Nonnull;

/**
 *
 */
public interface CommandImplementation<Arg, Result> {

    @Nonnull
    public CommandResult<Result> apply(Command<? super Arg, ? super Result> command, CommandRequest<Arg> req) throws MongoException;

}
