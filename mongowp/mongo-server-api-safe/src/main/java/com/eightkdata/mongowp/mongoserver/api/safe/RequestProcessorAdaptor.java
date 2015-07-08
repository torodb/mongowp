
package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.messages.request.*;
import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.mongoserver.api.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.api.safe.SafeRequestProcessor.SubRequestProcessor;
import com.eightkdata.mongowp.mongoserver.api.safe.pojos.QueryRequest;
import com.eightkdata.mongowp.mongoserver.callback.MessageReplier;
import com.eightkdata.mongowp.mongoserver.callback.RequestProcessor;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.CommandNotFoundException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import com.google.common.util.concurrent.Futures;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import io.netty.util.DefaultAttributeMap;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
public class RequestProcessorAdaptor implements RequestProcessor {
    public final static AttributeKey<Connection> CONNECTION =
			AttributeKey.valueOf(RequestProcessorAdaptor.class.getCanonicalName() + ".connection");

    private static final String NAMESPACES_COLLECTION = "system.namespaces";
    private static final String INDEXES_COLLECTION = "system.indexes";
    private static final String PROFILE_COLLECTION = "system.profile";
    private static final String JS_COLLECTION = "system.js";

    public static final String QUERY_MESSAGE_COMMAND_COLLECTION = "$cmd";
    public static final String QUERY_MESSAGE_ADMIN_DATABASE = "admin";

    private final ConnectionIdFactory connectionIdFactory;
    private final SafeRequestProcessor safeRequestProcessor;
    private final ErrorHandler errorHandler;

    @Inject
    public RequestProcessorAdaptor(
            ConnectionIdFactory connectionIdFactory,
            SafeRequestProcessor safeRequestProcessor,
            ErrorHandler errorHandler) {
        this.safeRequestProcessor = safeRequestProcessor;
        this.connectionIdFactory = connectionIdFactory;
        this.errorHandler = errorHandler;
    }

    @Nonnull
    protected Connection getConnection(AttributeMap attMap) {
        return attMap.attr(CONNECTION).get();
    }

    @Nonnull
    protected Connection getConnection(MessageReplier messageReplier) {
        return messageReplier.getAttributeMap().attr(CONNECTION).get();
    }

    @Override
    public void onChannelActive(AttributeMap attMap) {
        Connection newConnection = new Connection(
                connectionIdFactory.newConnectionId(),
                new DefaultAttributeMap()
        );
        Connection oldConnection = attMap.attr(CONNECTION).setIfAbsent(
                newConnection
        );
        if (oldConnection != null) {
            throw new IllegalArgumentException("A connection with id "
                    + oldConnection.getConnectionId() + " was stored before "
                    + "channel became active!");
        }
        safeRequestProcessor.onConnectionActive(newConnection);
    }

    @Override
    public void onChannelInactive(AttributeMap attMap) {
        Connection connection = attMap.attr(CONNECTION).getAndRemove();
        if (connection != null) {
            safeRequestProcessor.onConnectionInactive(connection);
        }
    }

    private SafeRequestProcessor.SubRequestProcessor getSubRequestProcessor(String collection) {
        if (NAMESPACES_COLLECTION.equals(collection)) {
            return safeRequestProcessor.getNamespacesRequestProcessor();
        }
        if(INDEXES_COLLECTION.equals(collection)) {
            return safeRequestProcessor.getIndexRequestProcessor();
        }
        if (PROFILE_COLLECTION.equals(collection)) {
            return safeRequestProcessor.getProfileRequestProcessor();
        }
        if (JS_COLLECTION.equals(collection)) {
            return safeRequestProcessor.getJSProcessor();
        }
        return safeRequestProcessor.getStantardRequestProcessor();
    }

    @Override
    public void queryMessage(QueryMessage queryMessage, MessageReplier messageReplier) {
        Connection connection = getConnection(messageReplier.getAttributeMap());
        try {
            if (QUERY_MESSAGE_COMMAND_COLLECTION.equals(queryMessage.getCollection())) {
                executeCommand(connection, queryMessage, messageReplier);
            }
            else {
                QueryRequest.Builder requestBuilder = new QueryRequest.Builder(
                    queryMessage.getDatabase(),
                    queryMessage.getCollection()
                );
                requestBuilder.setCollection(queryMessage.getCollection())
                        .setQuery(extractQuery(queryMessage.getDocument()))
                        .setProjection(null)
                        .setNumberToSkip(queryMessage.getNumberToSkip())
                        .setLimit(queryMessage.getNumberToReturn())
                        .setAwaitData(queryMessage.isFlagSet(QueryMessage.Flag.AWAIT_DATA))
                        .setExhaust(queryMessage.isFlagSet(QueryMessage.Flag.EXHAUST))
                        .setNoCursorTimeout(queryMessage.isFlagSet(QueryMessage.Flag.NO_CURSOR_TIMEOUT))
                        .setOplogReplay(queryMessage.isFlagSet(QueryMessage.Flag.OPLOG_REPLAY))
                        .setPartial(queryMessage.isFlagSet(QueryMessage.Flag.PARTIAL))
                        .setSlaveOk(queryMessage.isFlagSet(QueryMessage.Flag.SLAVE_OK))
                        .setTailable(queryMessage.isFlagSet(QueryMessage.Flag.TAILABLE_CURSOR));

                if (requestBuilder.getLimit() < 0) {
                    requestBuilder.setAutoclose(true);
                    requestBuilder.setLimit(-requestBuilder.getLimit());
                }
                else if (requestBuilder.getLimit() == 1) {
                    requestBuilder.setAutoclose(true);
                }
                SubRequestProcessor subRP = getSubRequestProcessor(queryMessage.getCollection());

                ReplyMessage reply = subRP.query(
                        new Request(
                                connection,
                                messageReplier.getRequestId(),
                                queryMessage.getDatabase(),
                                queryMessage.getClientAddress(),
                                queryMessage.getClientPort()
                        ),
                        requestBuilder.build()
                );
                messageReplier.replyMessage(reply);
            }
        } catch (MongoServerException ex) {
            errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
        }

    }

    private void executeCommand(
            Connection connection,
            QueryMessage queryMessage,
            MessageReplier messageReplier) throws MongoServerException {
        try {
            SubRequestProcessor subRP = getSubRequestProcessor(queryMessage.getCollection());

            BsonDocument document = queryMessage.getDocument();
            Command command = subRP.getCommandsLibrary().find(document);
            if (command == null) {
                if (document.isEmpty()) {
                    throw new CommandNotFoundException("Empty document query");
                }
                String firstKey = document.keySet().iterator().next();
                throw new CommandNotFoundException(firstKey);
            }

            if (command.isAdminOnly()) {
                if (!QUERY_MESSAGE_ADMIN_DATABASE.equals(queryMessage.getDatabase())) {
                    messageReplier.replyQueryCommandFailure(
                            "{0} may only be run against the admin database.",
                            MongoWP.ErrorCode.UNAUTHORIZED.getErrorCode(),
                            command.getCommandName()
                    );
                    return ;
                }
            }

            CommandArgument arg = command.unmarshallArg(document);
            CommandRequest request = new CommandRequest(
                    connection,
                    queryMessage.getRequestId(),
                    queryMessage.getDatabase(),
                    queryMessage.getClientAddress(),
                    queryMessage.getClientPort(),
                    arg
            );
            CommandReply reply = subRP.execute(command, request);

            if (reply.getWriteOpResult() != null) {
                connection.setLastWriteOp(Futures.immediateFuture(reply.getWriteOpResult()));
            }
            BsonDocument bsonReply = command.marshallReply(reply);

            messageReplier.replyMessageNoCursor(bsonReply);
        }
        catch (CommandNotFoundException ex) {
            messageReplier.replyQueryCommandFailure(
                    MongoWP.ErrorCode.COMMAND_NOT_FOUND,
                    ex.getCommandName()
            );
        }
    }

    @Override
    public void getMore(GetMoreMessage getMoreMessage, MessageReplier messageReplier) {
        Connection connection = getConnection(messageReplier);
        try {
            Request req = new Request(
                    connection,
                    getMoreMessage.getRequestId(),
                    getMoreMessage.getDatabase(),
                    getMoreMessage.getClientAddress(),
                    getMoreMessage.getClientPort()
            );

            ReplyMessage reply = safeRequestProcessor.getMore(req, getMoreMessage);
            messageReplier.replyMessage(reply);
        } catch (MongoServerException ex) {
            errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
        }
    }

    @Override
    public void killCursors(KillCursorsMessage killCursorsMessage, MessageReplier messageReplier) {
        Connection connection = getConnection(messageReplier);
        try {
            Request req = new Request(
                    connection,
                    killCursorsMessage.getRequestId(),
                    null,
                    killCursorsMessage.getClientAddress(),
                    killCursorsMessage.getClientPort()
            );
            safeRequestProcessor.killCursors(req, killCursorsMessage);
        } catch (MongoServerException ex) {
            errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
        }
    }

    @Override
    public void insert(InsertMessage insertMessage, MessageReplier messageReplier) {
        Connection connection = getConnection(messageReplier);
        try {
            Request req = new Request(
                    connection,
                    insertMessage.getRequestId(),
                    insertMessage.getDatabase(),
                    insertMessage.getClientAddress(),
                    insertMessage.getClientPort()
            );
            Future<? extends WriteOpResult> futureWriteOp = getSubRequestProcessor(insertMessage.getCollection())
                    .insert(
                            req,
                            insertMessage
                    );

            connection.setLastWriteOp(futureWriteOp);
        } catch (MongoServerException ex) {
            errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
        }
    }

    @Override
    public void update(UpdateMessage updateMessage, MessageReplier messageReplier) {
        Connection connection = getConnection(messageReplier);
        try {
            Request req = new Request(
                    connection,
                    updateMessage.getRequestId(),
                    updateMessage.getDatabase(),
                    updateMessage.getClientAddress(),
                    updateMessage.getClientPort());
            Future<? extends WriteOpResult> futureWriteOp = getSubRequestProcessor(updateMessage.getCollection())
                    .update(
                            req,
                            updateMessage
                    );

            connection.setLastWriteOp(futureWriteOp);
        } catch (MongoServerException ex) {
            errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
        }
    }

    @Override
    public void delete(DeleteMessage deleteMessage, MessageReplier messageReplier) {
        Connection connection = getConnection(messageReplier);
        try {
            Request req = new Request(
                    connection,
                    deleteMessage.getRequestId(),
                    deleteMessage.getDatabase(),
                    deleteMessage.getClientAddress(),
                    deleteMessage.getClientPort());
            Future<? extends WriteOpResult> futureWriteOp = getSubRequestProcessor(deleteMessage.getCollection())
                    .delete(
                            req,
                            deleteMessage
                    );

            connection.setLastWriteOp(futureWriteOp);
        }
        catch (MongoServerException ex) {
            errorHandler.handleMongodbException(connection, messageReplier.getRequestId(), false, ex);
        }
    }

    @Override
    public boolean handleError(RequestOpCode requestOpCode, MessageReplier messageReplier, Throwable throwable) {
        Connection connection = getConnection(messageReplier);

        ReplyMessage handleMongodbException;
        if (throwable instanceof MongoServerException) {
            handleMongodbException = errorHandler.handleMongodbException(
                    connection,
                    messageReplier.getRequestId(),
                    requestOpCode.canReply(),
                    (MongoServerException) throwable
            );
        }
        else {
            handleMongodbException = errorHandler.handleUnexpectedError(
                    connection,
                    messageReplier.getRequestId(),
                    requestOpCode.canReply(),
                    throwable
            );
        }
        if (requestOpCode.canReply() && handleMongodbException != null) {
            messageReplier.replyMessage(handleMongodbException);
        }
        return true;
    }

    private BsonDocument extractQuery(BsonDocument query) {
        for (Entry<String, BsonValue> entrySet : query.entrySet()) {
            String key = entrySet.getKey();
            if ("query".equals(key) || "$query".equals(key)) {
    			BsonValue queryObject = entrySet.getValue();
    			if (queryObject != null && queryObject.isDocument()) {
    				return query.asDocument();
    			}
    		}
        }
        return query;
    }
}
