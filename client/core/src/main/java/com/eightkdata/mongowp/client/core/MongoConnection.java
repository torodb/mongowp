
package com.eightkdata.mongowp.client.core;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandReply;
import com.eightkdata.mongowp.server.api.pojos.MongoCursor;
import java.io.Closeable;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 */
@NotThreadSafe
public interface MongoConnection extends Closeable {

    @Nonnull
    public MongoClient getClientOwner();

    @Nonnull
    public MongoCursor<BsonDocument> query(
            String database,
            String collection,
            @Nullable BsonDocument query,
            int numberToSkip,
            int numberToReturn,
            @Nonnull QueryOptions queryOptions,
            @Nullable BsonDocument projection) throws MongoException;

    public void asyncKillCursors(@Nonnull Iterable<Long> cursors) throws
            MongoException;

    public void asyncKillCursors(long[] cursors) throws
            MongoException;

    public void asyncInsert(
            @Nonnull String database,
            @Nonnull String collection,
            boolean continueOnError,
            List<? extends BsonDocument> docsToInsert
    ) throws MongoException;

    public void asyncUpdate(
            @Nonnull String database,
            @Nonnull String collection,
            @Nonnull BsonDocument selector,
            @Nonnull BsonDocument update,
            boolean upsert,
            boolean multiUpdate) throws MongoException;

    public void asyncDelete(
            @Nonnull String database,
            @Nonnull String collection,
            boolean singleRemove,
            @Nonnull BsonDocument selector) throws MongoException;

    /**
     *
     * @param <Arg>
     * @param <Result>
     * @param command
     * @param database
     * @param isSlaveOk if the execution on slave nodes has to be enforced
     * @param arg
     * @return the reply to the given command. The reply is always
     *         {@linkplain CommandReply#isOk() ok} because all errors are
     *         reported as exceptions
     * @throws MongoException
     */
    @Nonnull
    public <Arg, Result> Result execute(
            @Nonnull Command<? super Arg, Result> command,
            String database,
            boolean isSlaveOk,
            @Nonnull Arg arg)
            throws MongoException;

    /**
     * Returns true iff this object represents a remote server.
     *
     * This method will be return true in most cases, but when used in a mongod
     * node, an instance of this object can represent the same mongod
     * @return
     */
    public boolean isRemote();

    @Override
    public void close();
}
