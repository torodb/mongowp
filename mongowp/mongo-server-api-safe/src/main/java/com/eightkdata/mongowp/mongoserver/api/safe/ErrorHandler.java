
package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
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
            MongoServerException exception);
}
