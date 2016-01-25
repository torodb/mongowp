
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.mongoserver.api.pojos.MongoCursor;
import com.eightkdata.mongowp.mongoserver.api.pojos.MongoCursor.Batch;
import com.eightkdata.mongowp.mongoserver.api.pojos.MongoCursor.DeadCursorException;
import com.eightkdata.mongowp.mongoserver.api.pojos.SimpleBatch;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonReaderTool;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
public class CursorMarshaller {

    private static final BsonField<Long> ID_FIELD = BsonField.create("id");
    private static final BsonField<String> NAMESPACE_FIELD = BsonField.create("ns");
    private static final BsonField<BsonArray> FIRST_BATCH_FIELD = BsonField.create("firstBatch");

    public static <T> BsonDocument marshall(
            MongoCursor<T> cursor,
            Function<T, ? extends BsonValue> conversor) throws MongoException {

        BsonArray array = new BsonArray();
        Iterator<T> firstBatch = cursor.fetchBatch();
        while (firstBatch.hasNext()) {
            array.add(conversor.apply(firstBatch.next()));
        }

        return new BsonDocumentBuilder()
                .append(ID_FIELD, cursor.getId())
                .append(NAMESPACE_FIELD, cursor.getDatabase() + '.' + cursor.getCollection())
                .append(FIRST_BATCH_FIELD, array)
                .build();
    }

    public static <T> FirstBatchOnlyCursor<T> unmarshall(
            BsonDocument doc,
            Function<BsonValue, T> conversor) throws BadValueException, TypesMismatchException, NoSuchKeyException {

        String ns = BsonReaderTool.getString(doc, NAMESPACE_FIELD);
        int dotIndex = ns.indexOf('.');

        String database;
        String collection;

        if (dotIndex == -1) {
            throw new BadValueException("The given namespace (" + ns + ") does "
                    + "not contain a collection ");
        }
        else {
            database = ns.substring(0, dotIndex);
            collection = ns.substring(dotIndex + 1);
        }

        ImmutableList.Builder<T> builder = ImmutableList.builder();
        for (BsonValue element : BsonReaderTool.getArray(doc, FIRST_BATCH_FIELD)) {
            builder.add(conversor.apply(element));
        }

        return new FirstBatchOnlyCursor<T>(
                BsonReaderTool.getLong(doc, ID_FIELD),
                database,
                collection,
                builder.build(),
                System.currentTimeMillis()
        );
    }

    public static class FirstBatchOnlyCursor<T> implements MongoCursor<T> {

        private final long id;
        private final String database;
        private final String collection;
        private final ImmutableList<T> firstBatch;
        private final long firstBatchTime;
        private boolean consumed;

        public FirstBatchOnlyCursor(
                long id,
                String database,
                String collection,
                ImmutableList<T> firstBatch,
                long firstBatchTime) {
            this.id = id;
            this.database = database;
            this.collection = collection;
            this.firstBatch = firstBatch;
            this.consumed = false;
            this.firstBatchTime = firstBatchTime;
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
            return id;
        }

        @Override
        public void setMaxBatchSize(int newBatchSize) {
        }

        @Override
        public int getMaxBatchSize() {
            return firstBatch.size();
        }

        /**
         * This cursor is always not tailable.
         * @return
         */
        @Override
        public boolean isTailable() {
            return false;
        }

        /**
         * {@inheritDoc }
         * <p/>
         * This cursor is always dead.
         * @return
         */
        @Override
        public boolean isDead() {
            return true;
        }

        @Override
        public T getOne() throws MongoException, DeadCursorException {
            if (consumed) {
                throw new DeadCursorException();
            }
            if (firstBatch.isEmpty()) {
                return null;
            }
            T result = firstBatch.get(0);

            close();

            return result;
        }
        
        @Override
        public Batch<T> fetchBatch() {
            if (consumed) {
                throw new DeadCursorException();
            }
            consumed = true;
            return new SimpleBatch<T>(firstBatch, firstBatchTime);
        }

        @Override
        public Iterator<T> iterator() {
            return new SimpleBatch<T>(firstBatch, firstBatchTime);
        }

        @Override
        public void close() {
            consumed = true;
        }
    }


}
