package com.eightkdata.mongowp.client.wrapper;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.org.bson.utils.MongoBsonTranslator;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.client.core.MongoClient;
import com.eightkdata.mongowp.client.core.MongoConnection;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.eightkdata.mongowp.server.api.pojos.MongoCursor;
import com.eightkdata.mongowp.server.api.pojos.CollectionBatch;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.net.HostAndPort;
import com.mongodb.CursorType;
import com.mongodb.MongoCursorNotFoundException;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;

/**
 *
 */
public class MongoConnectionWrapper implements MongoConnection {

    private static final Logger LOGGER = LogManager.getLogger(MongoConnectionWrapper.class);
    private static final int DEFAULT_MAX_BATCH_SIZE = 100;
    private static final BsonDocument EMPTY_DOC = DefaultBsonValues.EMPTY_DOC;

    private final CodecRegistry codecRegistry;
    private final MongoClientWrapper owner;

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
        owner.getDriverClient()
                .getDatabase(database)
                .getCollection(collection, BsonDocument.class)
                .insertMany(docsToInsert, new InsertManyOptions().ordered(continueOnError));
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

            MongoCollection<org.bson.BsonDocument> mongoCollection
                    = owner.getDriverClient()
                    .getDatabase(database)
                    .getCollection(collection, org.bson.BsonDocument.class);
            org.bson.BsonDocument translatedUpdate
                    = MongoBsonTranslator.translate(update);
            if (multiUpdate) {
                mongoCollection.updateMany(translatedUpdate, translatedUpdate, updateOptions);
            } else {
                mongoCollection.updateOne(translatedUpdate, translatedUpdate, updateOptions);
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
            MongoCollection<BsonDocument> collectionObject
                    = owner.getDriverClient()
                    .getDatabase(database)
                    .getCollection(collection, BsonDocument.class);
            org.bson.BsonDocument mongoSelector
                    = MongoBsonTranslator.translate(selector);
            if (singleRemove) {
                collectionObject.deleteOne(mongoSelector);
            } else {
                collectionObject.deleteMany(mongoSelector);
            }
        } catch (IOException ex) {
            throw new BadValueException("Unexpected IO exception", ex);
        }
    }

    @Override
    public <Arg, Result> Result execute(
            Command<? super Arg, Result> command,
            String database,
            boolean isSlaveOk,
            Arg arg) throws MongoException {
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
                            MongoBsonTranslator.translate(command.marshallArg(arg)),
                            readPreference
                    );
            org.bson.BsonDocument bsonDoc
                    = document.toBsonDocument(Document.class, codecRegistry);
            return command.unmarshallResult(MongoBsonTranslator.translate(bsonDoc));
        } catch (MarshalException ex) {
            throw new BadValueException(
                    "It was impossible to marshall the given argument to "
                    + command.getCommandName(),
                    ex
            );
        } catch (IOException ex) {
            throw new BadValueException("Unexpected IO exception", ex);
        }
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Override
    public void close() {
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
                if (!cursor.hasNext()) {
                    return new CollectionBatch<>(docs, start);
                }
                docs.add(MongoBsonTranslator.translate(cursor.next()));

                while (docs.size() < maxBatchSize && System.currentTimeMillis()
                        - start < MAX_WAIT_TIME) {
                    BsonDocument next
                            = MongoBsonTranslator.translate(cursor.tryNext());
                    if (next == null) {
                        break;
                    }
                    docs.add(next);
                }
            } catch (MongoCursorNotFoundException ex) {
                this.close();
                throw new DeadCursorException();
            }

            return new CollectionBatch<>(docs, start);
        }

        @Override
        public HostAndPort getServerAddress() {
            if (serverAddress == null) {
                ServerAddress mongoServerAddress = cursor.getServerAddress();
                serverAddress = HostAndPort.fromParts(mongoServerAddress.getHost(), mongoServerAddress.getPort());
            }
            return serverAddress;
        }

        @Override
        public boolean hasNext() {
            if (close) {
                return false;
            }
            try {
                return cursor.hasNext();
            } catch (MongoCursorNotFoundException ex) {
                this.close();
                return false;
            }
        }

        @Override
        public BsonDocument next() {
            Preconditions.checkState(!close, "This cursor is closed");
            try {
                return MongoBsonTranslator.translate(cursor.next());
            } catch (MongoCursorNotFoundException ex) {
                this.close();
                throw new DeadCursorException();
            }
        }

        @Override
        public BsonDocument tryNext() {
            try {
                return MongoBsonTranslator.translate(cursor.tryNext());
            } catch (MongoCursorNotFoundException ex) {
                this.close();
                throw new DeadCursorException();
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
                cursor.close();
            }
        }

    }
}
