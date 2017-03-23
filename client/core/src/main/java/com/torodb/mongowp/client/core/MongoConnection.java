/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.client.core;

import com.torodb.mongowp.ErrorCode;
import com.torodb.mongowp.Status;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.utils.DefaultBsonValues;
import com.torodb.mongowp.exceptions.MongoException;
import com.torodb.mongowp.messages.request.QueryMessage.QueryOptions;
import com.torodb.mongowp.server.api.Command;
import com.torodb.mongowp.server.api.MarshalException;
import com.torodb.mongowp.server.api.pojos.MongoCursor;
import com.torodb.mongowp.utils.BsonDocumentBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

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
      @Nullable BsonDocument sortBy,
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
   * Executes the given command.
   *
   * @param isSlaveOk if the execution on slave nodes has to be enforced
   * @return the reply to the given command. The reply is always {@linkplain CommandReply#isOk() ok}
   *         because all errors are reported as exceptions
   */
  @Nonnull
  public <A, R> RemoteCommandResponse<R> execute(
      @Nonnull Command<? super A, R> command,
      String database,
      boolean isSlaveOk,
      @Nonnull A arg);

  @Nonnull
  public <A, R> RemoteCommandResponse<R> execute(
      @Nonnull Command<? super A, R> command,
      String database,
      boolean isSlaveOk,
      @Nonnull A arg,
      Duration timeout);

  /**
   * Returns true iff this object represents a remote server.
   *
   * This method will be return true in most cases, but when used in a mongod node, an instance of
   * this object can represent the same mongod
   *
   * @return
   */
  public boolean isRemote();

  @Override
  public void close();

  public boolean isClosed();

  public static interface RemoteCommandResponse<R> {

    /**
     * The command reply.
     *
     * If there were no errors, it is always present. It is posible to be present even if
         * {@link #isOk() } returns true.
     *
     * @return
     */
    Optional<R> getCommandReply();

    @Nonnull
    Duration getNetworkTime();

    /**
     * Returns the reply marshalled as bson for report purposes.
     *
     * The returned bson can be null if {@link #getCommandReply() } is absent or if its unknown how
     * to marshal the response. As the purpose of this method is to be used on human reports, the
     * given BSON can even not be unmarshalled by the related command.
     *
     * @return the reply marshalled as BSON or null if {@link #getCommandReply() } is absent.
     */
    @Nullable
    BsonDocument getBson();

    public ErrorCode getErrorCode();

    /**
     * Returns true iff {@link #getErrorCode() } returns {@link ErrorCode#OK}.
     *
     * @return
     */
    public boolean isOk();

    @Nonnull
    public String getErrorDesc();

    /**
     *
     * @return @throws IllegalStateException if {@link #getErrorCode()}.isOk()
     */
    @Nonnull
    public MongoException asMongoException() throws IllegalStateException;

    public Status<R> asStatus();
  }

  public static class CorrectRemoteCommandResponse<R> implements RemoteCommandResponse<R> {

    private static final Logger LOGGER =
        LogManager.getLogger(CorrectRemoteCommandResponse.class);

    private final Command<?, R> command;
    @Nonnull
    private final Duration networkTime;
    private final Optional<R> commandReply;
    private BsonDocument bson;

    public CorrectRemoteCommandResponse(
        @Nonnull Command<?, R> command,
        @Nonnull Duration networkTime,
        @Nonnull R commandReply) {
      this.command = command;
      this.networkTime = networkTime;
      this.commandReply = Optional.of(commandReply);
    }

    @Override
    public Optional<R> getCommandReply() {
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
    public boolean isOk() {
      return true;
    }

    @Override
    public String getErrorDesc() {
      return "";
    }

    @Override
    public MongoException asMongoException() {
      throw new IllegalStateException("This is a correct Remote Command Response");
    }

    @Override
    public Status<R> asStatus() {
      return Status.ok(getCommandReply().get());
    }
  }

  public static class ErroneousRemoteCommandResponse<R> implements
      RemoteCommandResponse<R> {

    @Nonnull
    private final ErrorCode errorCode;
    @Nonnull
    private final String errorDesc;
    @Nonnull
    private final Duration networkTime;
    @Nonnull
    private final Optional<R> result;
    private final BsonDocument bson;

    public ErroneousRemoteCommandResponse(ErrorCode errorCode, String errorDesc,
        Duration networkTime) {
      this(errorCode, errorDesc, networkTime, null, null);
    }

    public ErroneousRemoteCommandResponse(
        ErrorCode errorCode,
        String errorDesc,
        Duration networkTime,
        @Nullable R result,
        @Nullable BsonDocument bson) {
      this.errorDesc = errorDesc;
      this.networkTime = networkTime;
      this.errorCode = errorCode;
      this.result = Optional.ofNullable(result);
      this.bson = bson;
    }

    @Override
    public Optional<R> getCommandReply() {
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
    public boolean isOk() {
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
    public Status<R> asStatus() {
      return Status.from(errorCode, errorDesc);
    }

  }

  public static class FromExceptionRemoteCommandRequest<R> 
      implements RemoteCommandResponse<R> {

    @Nonnull
    private final MongoException exception;
    @Nonnull
    private final Duration networkTime;
    @Nonnull
    private final Optional<R> result;
    private final BsonDocument bson;

    public FromExceptionRemoteCommandRequest(MongoException exception, Duration networkTime) {
      this(exception, networkTime, null, null);
    }

    public FromExceptionRemoteCommandRequest(
        @Nonnull MongoException exception,
        @Nonnull Duration networkTime,
        @Nullable R result,
        @Nullable BsonDocument bson) {
      this.exception = exception;
      this.networkTime = networkTime;
      this.result = Optional.ofNullable(result);
      this.bson = bson;
    }

    @Override
    public Optional<R> getCommandReply() {
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
    public boolean isOk() {
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
    public Status<R> asStatus() {
      return Status.from(exception);
    }
  }
}
