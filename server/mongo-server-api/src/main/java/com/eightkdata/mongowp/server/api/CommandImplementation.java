package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.Status;
import javax.annotation.Nonnull;

/**
 *
 */
public interface CommandImplementation<Arg, Result, Context> {

    @Nonnull
    public Status<Result> apply(Request req, Command<? super Arg, ? super Result> command, Arg arg, Context context);

}
