package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;

/**
 *
 */
public interface CommandImplementation<Arg extends CommandArgument, Rep extends CommandReply> {

    public Rep apply(Command<? extends Arg, ? extends Rep> command, CommandRequest<Arg> req) throws MongoServerException;

}
