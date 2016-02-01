
package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.exceptions.MongoException;
import javax.annotation.Nullable;

/**
 *
 */
public interface ErrorHandler {

    @Nullable
    public ReplyMessage handleUnexpectedError(
            Connection connection,
            int requestId,
            boolean canReply,
            Throwable error);

    @Nullable
    public ReplyMessage handleMongodbException(
            Connection connection,
            int requestId,
            boolean canReply,
            MongoException exception);
}
