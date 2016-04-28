
package com.eightkdata.mongowp.client.core;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.Status;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandReply;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.eightkdata.mongowp.server.api.pojos.MongoCursor;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.google.common.base.Optional;
import java.io.Closeable;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import org.slf4j.LoggerFactory;
import org.threeten.bp.Duration;

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
            @Nonnull Arg arg);

    @Nonnull
    public <Arg, Result> RemoteCommandResponse<Result> execute(
            @Nonnull Command<? super Arg, Result> command,
            String database,
            boolean isSlaveOk,
            @Nonnull Arg arg,
            Duration timeout);

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

        /**
         * Returns the reply marshalled as bson for report purposes.
         *
         * The returned bson can be null if {@link #getCommandReply() } is absent or if its unknown
         * how to marshal the response. As the purpose of this method is to be used on human reports,
         * the given BSON can even not be unmarshalled by the related command.
         *
         * @return the reply marshalled as BSON or null if {@link #getCommandReply() } is absent.
         */
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

        /**
         *
         * @return
         * @throws IllegalStateException if {@link #getErrorCode()}.isOk()
         */
        @Nonnull
        public MongoException asMongoException() throws IllegalStateException;

        public Status<Result> asStatus();
    }
    
    public static class CorrectRemoteCommandResponse<Result> implements RemoteCommandResponse<Result> {
        private static final org.slf4j.Logger LOGGER
                = LoggerFactory.getLogger(CorrectRemoteCommandResponse.class);

        private final Command<?, Result> command;
        @Nonnull
    	private final Duration networkTime;
    	private final Optional<Result> commandReply;
        private BsonDocument bson;

        public CorrectRemoteCommandResponse(
                @Nonnull Command<?, Result> command,
                @Nonnull Duration networkTime,
                @Nonnull Result commandReply) {
            this.command = command;
            this.networkTime = networkTime;
            this.commandReply = Optional.of(commandReply);
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
            if (bson == null) {
                try {
                    bson = command.marshallResult(commandReply.get());
                } catch (MarshalException ex) {
                    LOGGER.debug("Impossible to marshall a reply for " + command.getCommandName()
                            + " command", ex);
                    bson = new BsonDocumentBuilder(2)
                            .appendUnsafe("errorOn", DefaultBsonValues.newString("marshallable respose"))
                            .appendUnsafe("command", DefaultBsonValues.newString(command.getCommandName()))
                            .build();
                }
            }
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

        @Nullable
        public MongoException asMongoException() {
            throw new IllegalStateException("This is a correct Remote Command Response");
        }

        @Override
        public Status<Result> asStatus() {
            return Status.of(getCommandReply().get());
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

        public ErroneousRemoteCommandResponse(ErrorCode errorCode, String errorDesc, Duration networkTime) {
            this(errorCode, errorDesc, networkTime, null, null);
        }

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

        @Override
        public MongoException asMongoException() throws IllegalStateException {
            return new MongoException(errorDesc, errorCode);
        }

        @Override
        public Status<Result> asStatus() {
            return Status.error(errorCode, errorDesc);
        }

    }

    public static class FromExceptionRemoteCommandRequest<Result> implements RemoteCommandResponse<Result> {
        @Nonnull
        private final MongoException exception;
        @Nonnull
        private final Duration networkTime;
        @Nonnull
        private final Optional<Result> result;
        private final BsonDocument bson;

        public FromExceptionRemoteCommandRequest(MongoException exception, Duration networkTime) {
            this(exception, networkTime, null, null);
        }

        public FromExceptionRemoteCommandRequest(
                @Nonnull MongoException exception,
                @Nonnull Duration networkTime,
                @Nullable Result result,
                @Nullable BsonDocument bson) {
            this.exception = exception;
            this.networkTime = networkTime;
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
            return exception.getErrorCode();
        }

        @Override
        public boolean isOK() {
            return false;
        }

        @Override
        public String getErrorDesc() {
            return exception.getLocalizedMessage();
        }

        @Override
        public MongoException asMongoException() throws IllegalStateException {
            return exception;
        }

        @Override
        public Status<Result> asStatus() {
            return Status.error(exception);
        }
    }
}
