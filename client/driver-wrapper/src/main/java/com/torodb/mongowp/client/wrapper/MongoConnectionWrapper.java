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
package com.torodb.mongowp.client.wrapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import com.mongodb.CursorType;
import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.MongoServerException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import com.torodb.mongowp.ErrorCode;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.org.bson.utils.MongoBsonTranslator;
import com.torodb.mongowp.bson.utils.DefaultBsonValues;
import com.torodb.mongowp.client.core.MongoClient;
import com.torodb.mongowp.client.core.MongoConnection;
import com.torodb.mongowp.exceptions.BadValueException;
import com.torodb.mongowp.exceptions.MongoException;
import com.torodb.mongowp.messages.request.QueryMessage.QueryOptions;
import com.torodb.mongowp.server.api.Command;
import com.torodb.mongowp.server.api.MarshalException;
import com.torodb.mongowp.server.api.MongoRuntimeException;
import com.torodb.mongowp.server.api.pojos.CollectionBatch;
import com.torodb.mongowp.server.api.pojos.MongoCursor;
import com.torodb.mongowp.server.api.pojos.MongoCursor.DeadCursorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 *
 */
public class MongoConnectionWrapper implements MongoConnection {

  private static final Logger LOGGER = LogManager.getLogger(MongoConnectionWrapper.class);
  private static final int DEFAULT_MAX_BATCH_SIZE = 100;
  private static final BsonDocument EMPTY_DOC = DefaultBsonValues.EMPTY_DOC;

  private final CodecRegistry codecRegistry;
  private final MongoClientWrapper owner;
  private boolean close = false;

  public MongoConnectionWrapper(
      CodecRegistry codecRegistry,
      MongoClientWrapper owner) {
    this.codecRegistry = codecRegistry;
    this.owner = owner;
  }

  @Override
  public MongoClient getClientOwner() {
    return owner;
  }

  @Override
  public MongoCursor<BsonDocument> query(
      String database,
      String collection,
      BsonDocument query,
      int numberToSkip,
      int numberToReturn,
      QueryOptions queryOptions,
      BsonDocument sortBy,
      BsonDocument projection) throws MongoException {

    try {
      if (query == null) {
        query = EMPTY_DOC;
      }
      if (projection == null) {
        projection = EMPTY_DOC;
      }
      if (sortBy == null) {
        sortBy = EMPTY_DOC;
      }
      FindIterable<org.bson.BsonDocument> findIterable = owner.getDriverClient()
          .getDatabase(database)
          .getCollection(collection)
          .find(MongoBsonTranslator.translate(query), org.bson.BsonDocument.class)
          .skip(numberToSkip)
          .limit(numberToReturn)
          .sort(MongoBsonTranslator.translate(sortBy))
          .projection(MongoBsonTranslator.translate(projection))
          .cursorType(toCursorType(queryOptions))
          .noCursorTimeout(queryOptions.isNoCursorTimeout())
          .oplogReplay(queryOptions.isOplogReplay());
      return new WrappedMongoCursor(
          database,
          collection,
          DEFAULT_MAX_BATCH_SIZE,
          queryOptions.isTailable(),
          findIterable.iterator()
      );
    } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
      if (ErrorCode.isErrorCode(ex.getCode())) {
        throw toMongoException(ex);
      } else {
        throw toRuntimeMongoException(ex);
      }
    } catch (IOException ex) {
      throw new BadValueException("Unexpected IO exception", ex);
    }
  }

  private CursorType toCursorType(QueryOptions queryOptions) {
    if (!queryOptions.isTailable()) {
      return CursorType.NonTailable;
    }
    if (queryOptions.isAwaitData()) {
      return CursorType.TailableAwait;
    }
    return CursorType.Tailable;
  }

  @Override
  public void asyncKillCursors(Iterable<Long> cursors) throws
      MongoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void asyncKillCursors(long[] cursors) throws
      MongoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void asyncInsert(
      String database,
      String collection,
      boolean continueOnError,
      List<? extends BsonDocument> docsToInsert) throws MongoException {
    try {
      owner.getDriverClient()
          .getDatabase(database)
          .getCollection(collection, BsonDocument.class)
          .insertMany(docsToInsert, new InsertManyOptions()
              .ordered(continueOnError));
    } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
      if (ErrorCode.isErrorCode(ex.getCode())) {
        throw toMongoException(ex);
      } else {
        throw toRuntimeMongoException(ex);
      }
    }
  }

  @Override
  public void asyncUpdate(
      String database,
      String collection,
      BsonDocument selector,
      BsonDocument update,
      boolean upsert,
      boolean multiUpdate) throws MongoException {

    try {
      UpdateOptions updateOptions = new UpdateOptions().upsert(
          upsert
      );

      MongoCollection<org.bson.BsonDocument> mongoCollection =
          owner.getDriverClient()
              .getDatabase(database)
              .getCollection(collection, org.bson.BsonDocument.class);
      org.bson.BsonDocument translatedUpdate =
          MongoBsonTranslator.translate(update);
      if (multiUpdate) {
        mongoCollection.updateMany(translatedUpdate, translatedUpdate, updateOptions);
      } else {
        mongoCollection.updateOne(translatedUpdate, translatedUpdate, updateOptions);
      }
    } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
      if (ErrorCode.isErrorCode(ex.getCode())) {
        throw toMongoException(ex);
      } else {
        throw toRuntimeMongoException(ex);
      }
    } catch (IOException ex) {
      throw new BadValueException("Unexpected IO exception", ex);
    }
  }

  @Override
  public void asyncDelete(
      String database,
      String collection,
      boolean singleRemove,
      BsonDocument selector) throws MongoException {
    try {
      MongoCollection<BsonDocument> collectionObject =
          owner.getDriverClient()
              .getDatabase(database)
              .getCollection(collection, BsonDocument.class);
      org.bson.BsonDocument mongoSelector =
          MongoBsonTranslator.translate(selector);
      if (singleRemove) {
        collectionObject.deleteOne(mongoSelector);
      } else {
        collectionObject.deleteMany(mongoSelector);
      }
    } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
      if (ErrorCode.isErrorCode(ex.getCode())) {
        throw toMongoException(ex);
      } else {
        throw toRuntimeMongoException(ex);
      }
    } catch (IOException ex) {
      throw new BadValueException("Unexpected IO exception", ex);
    }
  }

  @Override
  public <A, R> RemoteCommandResponse<R> execute(
      Command<? super A, R> command,
      String database,
      boolean isSlaveOk,
      A arg) {
    long startMillis = System.currentTimeMillis();
    try {
      ReadPreference readPreference;
      if (isSlaveOk) {
        readPreference = ReadPreference.nearest();
      } else {
        readPreference = ReadPreference.primary();
      }
      Document document = owner.getDriverClient()
          .getDatabase(database)
          .runCommand(
              MongoBsonTranslator.translate(command.marshallArg(arg, command.getCommandName())),
              readPreference
          );
      org.bson.BsonDocument bsonDoc =
          document.toBsonDocument(Document.class, codecRegistry);
      R commandResult = command.unmarshallResult(
          MongoBsonTranslator.translate(bsonDoc));
      Duration d = Duration.ofMillis(System.currentTimeMillis() - startMillis);
      return new CorrectRemoteCommandResponse<>(command, d, commandResult);
    } catch (MarshalException ex) {
      Duration d = Duration.ofMillis(System.currentTimeMillis() - startMillis);
      return new ErroneousRemoteCommandResponse<>(
          ErrorCode.BAD_VALUE,
          "It was impossible to marshall the given argument to " + command,
          d, null, null);
    } catch (IOException ex) {
      Duration d = Duration.ofMillis(System.currentTimeMillis() - startMillis);
      return new ErroneousRemoteCommandResponse<>(
          ErrorCode.BAD_VALUE,
          "Unexpected IO exception",
          d, null, null);
    } catch (MongoException ex) { //our MongoWP exception
      Duration d = Duration.ofMillis(System.currentTimeMillis() - startMillis);
      return new FromExceptionRemoteCommandRequest<>(ex, d);
    } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
      if (ErrorCode.isErrorCode(ex.getCode())) {
        Duration d = Duration.ofMillis(System.currentTimeMillis() - startMillis);
        return new FromExceptionRemoteCommandRequest<>(toMongoException(ex), d);
      } else {
        throw toRuntimeMongoException(ex);
      }
    }
  }

  @Override
  public <A, R> RemoteCommandResponse<R> execute(
      Command<? super A, R> command,
      String database,
      boolean isSlaveOk,
      A arg,
      Duration timeout) {
    //TODO: manage duration!
    throw new UnsupportedOperationException("Timeout command execution is not supported yet");
  }

  @Override
  public boolean isClosed() {
    return close;
  }

  @Override
  public boolean isRemote() {
    return true;
  }

  /**
   *
   * @param ex an exception whose {@link com.mongodb.MongoException#getCode()} is valid (as
   *           specified by {@link ErrorCode#isErrorCode(int)}
   * @return
   */
  static final MongoException toMongoException(com.mongodb.MongoException ex) {
    try {
      ErrorCode errorCode = ErrorCode.fromErrorCode(ex.getCode());
      return new MongoException(ex.getMessage(), errorCode);
    } catch (IllegalArgumentException ex2) {
      throw new RuntimeException("Unrecognized error code "
          + ex.getCode() + " from a mongo client exception", ex);
    }
  }

  private static MongoRuntimeException toRuntimeMongoException(com.mongodb.MongoException ex) {
    if (ex instanceof com.mongodb.MongoSocketException) {
      return new com.torodb.mongowp.server.api.MongoSocketException(ex);
    }
    throw new MongoRuntimeException(ex);
  }

  @Override
  public void close() {
    //Nothing to do
    close = true;
  }

  private static class WrappedMongoCursor implements MongoCursor<BsonDocument> {

    private static final long MAX_WAIT_TIME = 10;

    private final String database;
    private final String collection;
    private int maxBatchSize;
    private final boolean tailable;
    private boolean close = false;
    private HostAndPort serverAddress;
    private final com.mongodb.client.MongoCursor<org.bson.BsonDocument> cursor;

    public WrappedMongoCursor(
        String database,
        String collection,
        int maxBatchSize,
        boolean tailable,
        com.mongodb.client.MongoCursor<org.bson.BsonDocument> cursor) {
      this.database = database;
      this.collection = collection;
      Preconditions.checkArgument(maxBatchSize > 0);
      this.maxBatchSize = maxBatchSize;
      this.tailable = tailable;
      this.cursor = cursor;
    }

    @Override
    public String getDatabase() {
      return database;
    }

    @Override
    public String getCollection() {
      return collection;
    }

    @Override
    public long getId() {
      return cursor.getServerCursor().getId();
    }

    @Override
    public void setMaxBatchSize(int newBatchSize) {
      Preconditions.checkState(!close, "This cursor is closed");
      this.maxBatchSize = newBatchSize;
    }

    @Override
    public int getMaxBatchSize() {
      return maxBatchSize;
    }

    @Override
    public boolean isTailable() {
      return tailable;
    }

    @Override
    public Batch<BsonDocument> tryFetchBatch() throws MongoException, DeadCursorException {
      Preconditions.checkState(!close, "This cursor is closed");
      long start = System.currentTimeMillis();

      List<BsonDocument> docs = Lists.newArrayList();

      try {
        boolean noMoreDocs = false;
        while (docs.size() < maxBatchSize && !noMoreDocs) {
          org.bson.BsonDocument tryNext = cursor.tryNext();
          if (tryNext != null) {
            docs.add(MongoBsonTranslator.translate(tryNext));
          } else {
            noMoreDocs = true;
          }
        }
        if (docs.isEmpty()) {
          return null;
        }
      } catch (MongoCursorNotFoundException ex) {
        this.close();
        throw new DeadCursorException();
      } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
        if (ErrorCode.isErrorCode(ex.getCode())) {
          throw toMongoException(ex);
        } else {
          throw toRuntimeMongoException(ex);
        }
      }
      return new CollectionBatch<>(docs, start);
    }

    @Override
    public Batch<BsonDocument> fetchBatch() throws MongoException,
        DeadCursorException {
      Preconditions.checkState(!close, "This cursor is closed");
      long start = System.currentTimeMillis();

      List<BsonDocument> docs = Lists.newArrayList();

      try {
        if (!isTailable() && !cursor.hasNext()) {
          return new CollectionBatch<>(docs, start);
        }
        docs.add(MongoBsonTranslator.translate(cursor.next()));

        while (docs.size() < maxBatchSize && System.currentTimeMillis()
            - start < MAX_WAIT_TIME) {
          BsonDocument next =
              MongoBsonTranslator.translate(cursor.tryNext());
          if (next == null) {
            break;
          }
          docs.add(next);
        }
      } catch (MongoCursorNotFoundException ex) {
        this.close();
        throw new DeadCursorException();
      } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
        if (ErrorCode.isErrorCode(ex.getCode())) {
          throw toMongoException(ex);
        } else {
          throw toRuntimeMongoException(ex);
        }
      }

      return new CollectionBatch<>(docs, start);
    }

    @Override
    public HostAndPort getServerAddress() {
      if (serverAddress == null) {
        ServerAddress mongoServerAddress = cursor.getServerAddress();
        serverAddress = HostAndPort.fromParts(mongoServerAddress.getHost(), mongoServerAddress
            .getPort());
      }
      return serverAddress;
    }

    /**
     * {@inheritDoc }
     * This method can throw a {@link MongoServerException} if the underlaying cursor throws it.
     * This breaks the abstraction and can be changed on future releases.
     *
     * @return
     * @throws MongoServerException
     */
    @Override
    public boolean hasNext() throws MongoServerException {
      //TODO(gortiz): Wrap mongo driver exceptions on our own exceptions
      if (close) {
        return false;
      }
      try {
        return cursor.hasNext();
      } catch (MongoCursorNotFoundException ex) {
        this.close();
        return false;
      } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
        throw toRuntimeMongoException(ex);
      }
    }

    /**
     * {@inheritDoc }
     * This method can throw a {@link MongoServerException} if the underlaying cursor throws it.
     * This breaks the abstraction and can be changed on future releases.
     *
     * @return
     * @throws MongoServerException
     */
    @Override
    public BsonDocument next() throws MongoServerException {
      //TODO(gortiz): Wrap mongo driver exceptions on our own exceptions
      Preconditions.checkState(!close, "This cursor is closed");
      try {
        return MongoBsonTranslator.translate(cursor.next());
      } catch (MongoCursorNotFoundException ex) {
        this.close();
        throw new DeadCursorException();
      } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
        throw toRuntimeMongoException(ex);
      }
    }

    /**
     * {@inheritDoc }
     * This method can throw a {@link MongoServerException} if the underlaying cursor throws it.
     * This breaks the abstraction and can be changed on future releases.
     *
     * @return
     * @throws MongoServerException
     */
    @Override
    public BsonDocument tryNext() throws MongoServerException {
      //TODO(gortiz): Wrap mongo driver exceptions on our own exceptions
      try {
        return MongoBsonTranslator.translate(cursor.tryNext());
      } catch (MongoCursorNotFoundException ex) {
        this.close();
        throw new DeadCursorException();
      } catch (com.mongodb.MongoException ex) { //a general Mongo driver exception
        throw toRuntimeMongoException(ex);
      }
    }

    @Override
    public boolean isClosed() {
      return close;
    }

    @Override
    public void close() {
      if (!close) {
        close = true;
        try {
          cursor.close();
        } catch (com.mongodb.MongoException ex) {
          LOGGER.debug("Ignoring an exception while closing a "
              + "remote cursor", ex);
        }
      }
    }

  }
}
