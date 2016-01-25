
package com.eightkdata.mongowp.client.wrapper;

import com.eightkdata.mongowp.client.core.MongoClient;
import com.eightkdata.mongowp.client.core.MongoConnection;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.mongoserver.api.Command;
import com.eightkdata.mongowp.mongoserver.api.MarshalException;
import com.eightkdata.mongowp.mongoserver.api.pojos.MongoCursor;
import com.eightkdata.mongowp.mongoserver.api.pojos.SimpleBatch;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mongodb.CursorType;
import com.mongodb.ReadPreference;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import java.util.Iterator;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class MongoConnectionWrapper implements MongoConnection {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(MongoConnectionWrapper.class);
    private static final int DEFAULT_MAX_BATCH_SIZE = 100;
    private static final BsonDocument EMPTY_DOC = new BsonDocument();

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
            BsonDocument projection) throws MongoException {

        if (query == null) {
            query = EMPTY_DOC;
        }
        if (projection == null) {
            projection = EMPTY_DOC;
        }
        FindIterable<BsonDocument> findIterable = owner.getDriverClient()
                .getDatabase(database)
                .getCollection(collection)
                .find(query, BsonDocument.class)
                .skip(numberToSkip)
                .limit(numberToReturn)
                .projection(projection)
                .cursorType(toCursorType(queryOptions))
                .noCursorTimeout(queryOptions.isNoCursorTimeout())
                .oplogReplay(queryOptions.isOplogReplay());
        return new MyMongoCursor(
                database,
                collection,
                DEFAULT_MAX_BATCH_SIZE,
                queryOptions.isTailable(),
                findIterable.iterator()
        );
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

        UpdateOptions updateOptions = new UpdateOptions().upsert(
                upsert
        );

        MongoCollection<BsonDocument> mongoCollection = owner.getDriverClient()
                .getDatabase(database)
                .getCollection(collection, BsonDocument.class);
        if (multiUpdate) {
            mongoCollection.updateMany(update, update, updateOptions);
        }
        else {
            mongoCollection.updateOne(update, update, updateOptions);
        }
    }

    @Override
    public void asyncDelete(
            String database,
            String collection,
            boolean singleRemove,
            BsonDocument selector) throws MongoException {
        MongoCollection<BsonDocument> collectionObject = owner.getDriverClient()
                .getDatabase(database)
                .getCollection(collection, BsonDocument.class);
        if (singleRemove) {
            collectionObject.deleteOne(selector);
        }
        else {
            collectionObject.deleteMany(selector);
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
            }
            else {
                readPreference = ReadPreference.primary();
            }
            Document document = owner.getDriverClient()
                    .getDatabase(database)
                    .runCommand(command.marshallArg(arg), readPreference);
            BsonDocument bsonDoc = document.toBsonDocument(Document.class, codecRegistry);
            return command.unmarshallResult(bsonDoc);
        } catch (MarshalException ex) {
            throw new BadValueException(
                    "It was impossible to marshall the given argument to "
                            + command.getCommandName(),
                    ex
            );
        }
    }

    @Override
    public boolean isRemote() {
        return true;
    }

    @Override
    public void close() {
    }

    private static class MyMongoCursor implements MongoCursor<BsonDocument> {

        private static final long MAX_WAIT_TIME = 10;

        private final String database;
        private final String collection;
        private int maxBatchSize;
        private final boolean tailable;
        private boolean close = false;
        private final com.mongodb.client.MongoCursor<BsonDocument> cursor;

        public MyMongoCursor(
                String database,
                String collection,
                int maxBatchSize,
                boolean tailable,
                com.mongodb.client.MongoCursor<BsonDocument> cursor) {
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
            throw new UnsupportedOperationException("This kind of cursors does not have id");
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
        public boolean isDead() {
            return close || !cursor.hasNext();
        }

        @Override
        public Batch<BsonDocument> fetchBatch() throws MongoException,
                DeadCursorException {
            Preconditions.checkState(!close, "This cursor is closed");
            long start = System.currentTimeMillis();

            List<BsonDocument> docs = Lists.newArrayList();

            if (!cursor.hasNext()) {
                return new SimpleBatch<BsonDocument>(docs, start);
            }
            docs.add(cursor.next());
            
            while (docs.size() < maxBatchSize && System.currentTimeMillis() - start < MAX_WAIT_TIME) {
                BsonDocument next = cursor.tryNext();
                if (next == null) {
                    break;
                }
                docs.add(next);
            }

            return new SimpleBatch<BsonDocument>(docs, start);
        }

        @Override
        public BsonDocument getOne() throws MongoException, DeadCursorException {
            Preconditions.checkState(!close, "This cursor is closed");
            BsonDocument next = cursor.next();
            close();
            return next;
        }

        @Override
        public void close() {
            if (!close) {
                close = true;
                cursor.close();
            }
        }

        @Override
        public Iterator<BsonDocument> iterator() {
            Preconditions.checkState(!close, "This cursor is closed");
            return cursor;
        }

    }
}
