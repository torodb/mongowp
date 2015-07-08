package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.messages.request.*;
import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.mongoserver.api.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.api.safe.pojos.QueryRequest;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;

/**
 *
 */
public interface SafeRequestProcessor {

    public void onConnectionActive(Connection connection);

    public void onConnectionInactive(Connection connection);

    @Nonnull
    public ReplyMessage getMore(Request request, GetMoreMessage getMoreMessage)
            throws MongoServerException;

    public Future<?> killCursors(Request request, KillCursorsMessage killCursorsMessage)
            throws MongoServerException;

    public SubRequestProcessor getStantardRequestProcessor();

    public SubRequestProcessor getNamespacesRequestProcessor();

    public SubRequestProcessor getIndexRequestProcessor();

    public SubRequestProcessor getJSProcessor();

    public SubRequestProcessor getProfileRequestProcessor();

    public static interface SubRequestProcessor extends CommandsExecutor {

        @Nonnull
        public ReplyMessage query(Request request, QueryRequest queryMessage)
                throws MongoServerException;

        public CommandsLibrary getCommandsLibrary();

        public Future<? extends WriteOpResult> insert(Request request, InsertMessage insertMessage)
                throws MongoServerException;

        public Future<? extends WriteOpResult> update(Request request, UpdateMessage deleteMessage)
                throws MongoServerException;

        public Future<? extends WriteOpResult> delete(Request request, DeleteMessage deleteMessage)
                throws MongoServerException;

    }
}
