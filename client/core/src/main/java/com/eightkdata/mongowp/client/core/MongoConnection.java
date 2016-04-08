
package com.eightkdata.mongowp.client.core;

import java.io.Closeable;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.threeten.bp.Duration;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandReply;
import com.eightkdata.mongowp.server.api.pojos.MongoCursor;
import com.google.common.base.Optional;

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
    public <Arg, Result> RemoteCommandResponse<Result> execute(
            @Nonnull Command<? super Arg, Result> command,
            String database,
            boolean isSlaveOk,
            @Nonnull Arg arg)
            throws MongoException;

    @Nonnull
    public <Arg, Result> RemoteCommandResponse<Result> execute(
            @Nonnull Command<? super Arg, Result> command,
            String database,
            boolean isSlaveOk,
            @Nonnull Arg arg,
            Duration timeout)
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

    public boolean isClosed();

    public static interface RemoteCommandResponse<Result> {

        /**
         * The command reply.
         *
         * If there were no errors, it is always present. It is posible to be present even if
         * {@link #isOK() } returns true.
         * @return
         */
        Optional<Result> getCommandReply();

        @Nonnull
        Duration getNetworkTime();

        @Nullable
        BsonDocument getBson();

        public ErrorCode getErrorCode();

        /**
         * Returns true iff {@link #getErrorCode() } returns {@link ErrorCode#OK}.
         * @return
         */
        public boolean isOK();

        @Nonnull
        public String getErrorDesc();
    }
    
    public static class CorrectRemoteCommandResponse<Result> implements RemoteCommandResponse<Result> {

        @Nonnull
    	private final Duration networkTime;
    	private final Optional<Result> commandReply;
        private final BsonDocument bson;
    	
		public CorrectRemoteCommandResponse(@Nonnull Duration networkTime, @Nullable Result commandReply, @Nullable BsonDocument bson) {
			super();
			this.networkTime = networkTime;
			this.commandReply = Optional.fromNullable(commandReply);
			this.bson = bson;
		}

		@Override
		public Optional<Result> getCommandReply() {
			return commandReply;
		}

		@Override
		public Duration getNetworkTime() {
			return networkTime;
		}

		@Override
		public BsonDocument getBson() {
			return bson;
		}

		@Override
		public ErrorCode getErrorCode() {
			return ErrorCode.OK;
		}

		@Override
		public boolean isOK() {
			return true;
		}

		@Override
		public String getErrorDesc() {
			return "";
		}
    }

    public static class ErroneousRemoteCommandResponse<Result> implements RemoteCommandResponse<Result> {

        @Nonnull
        private final ErrorCode errorCode;
        @Nonnull
        private final String errorDesc;
        @Nonnull
        private final Duration networkTime;
        @Nonnull
        private final Optional<Result> result;
        private final BsonDocument bson;

        public ErroneousRemoteCommandResponse(
                ErrorCode errorCode,
                String errorDesc,
                Duration networkTime,
                @Nullable Result result,
                @Nullable BsonDocument bson) {
            this.errorDesc = errorDesc;
            this.networkTime = networkTime;
            this.errorCode = errorCode;
            this.result = Optional.fromNullable(result);
            this.bson = bson;
        }

        @Override
        public Optional<Result> getCommandReply() {
            return result;
        }

        @Override
        public Duration getNetworkTime() {
            return networkTime;
        }

        @Override
        public BsonDocument getBson() {
            return bson;
        }

        @Override
        public ErrorCode getErrorCode() {
            return errorCode;
        }

        @Override
        public boolean isOK() {
            return false;
        }

        @Override
        public String getErrorDesc() {
            return errorDesc;
        }

    }
}
