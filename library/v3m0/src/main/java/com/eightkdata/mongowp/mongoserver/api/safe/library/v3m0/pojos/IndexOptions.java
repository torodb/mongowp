
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class IndexOptions {

    private final IndexVersion version;
    private final String name;
    private final String database;
    private final String collection;
    private final boolean background;
    private final boolean unique;
    private final boolean sparse;
    private final int expireAfterSeconds;

    private final Map<List<String>, Boolean> keys;
    private final BsonDocument storageEngine;

    public static final Function<IndexOptions, BsonValue> MARSHALLER_FUN = new MyMarshaller();
    public static final Function<BsonValue, IndexOptions> UNMARSHALLER_FUN = new MyUnMarshaller();

    public IndexOptions(
            IndexVersion version,
            String name,
            String database,
            String collection,
            boolean background,
            boolean unique,
            boolean sparse,
            int expireAfterSeconds,
            @Nonnull Map<List<String>, Boolean> keys,
            @Nullable BsonDocument storageEngine) {
        this.version = version;
        this.name = name;
        this.database = database;
        this.collection = collection;
        this.background = background;
        this.unique = unique;
        this.sparse = sparse;
        this.expireAfterSeconds = expireAfterSeconds;
        this.keys = keys;
        this.storageEngine = storageEngine;
    }

    public IndexVersion getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getDatabase() {
        return database;
    }

    public String getCollection() {
        return collection;
    }

    /**
     * Returns a map with the indexed paths and if they are ascending or descending.
     * 
     * The kyes are lists of strings that represent the path of the index and
     * values are booleans that indicates if the index is ascending or
     * descending.
     * @return
     */
    public Map<List<String>, Boolean> getKeys() {
        return Collections.unmodifiableMap(keys);
    }

    public boolean isBackground() {
        return background;
    }

    public boolean isUnique() {
        return unique;
    }

    public boolean isSparse() {
        return sparse;
    }

    public int getExpireAfterSeconds() {
        return expireAfterSeconds;
    }

    public BsonDocument getStorageEngine() {
        return storageEngine;
    }

    private static final IntField VERSION_FIELD = new IntField("v");
    private static final StringField NAME_FIELD = new StringField("name");
    private static final StringField NAMESPACE_FIELD = new StringField("ns");
    private static final BooleanField BACKGROUND_FIELD = new BooleanField("blackground");
    private static final BooleanField UNIQUE_FIELD = new BooleanField("unique");
    private static final BooleanField SPARSE_FIELD = new BooleanField("sparse");
    private static final IntField EXPIRE_AFTER_SECONDS_FIELD = new IntField("expireAfterSeconds");
    private static final DocField KEYS_FIELD = new DocField("key");
    private static final DocField STORAGE_ENGINE_FIELD = new DocField("storageEngine");
    private static final Joiner PATH_JOINER = Joiner.on('.');
    private static final Splitter PATH_SPLITER = Splitter.on('.');

    public BsonDocument marshall() {

        BsonDocumentBuilder keysDoc = new BsonDocumentBuilder();
        for (java.util.Map.Entry<List<String>, Boolean> entry : keys.entrySet()) {
            String path = PATH_JOINER.join(entry.getKey());
            BsonInt32 value = DefaultBsonValues.newInt(entry.getValue() ? 1 : -1);
            keysDoc.appendUnsafe(path, value);
        }

        BsonDocumentBuilder builder = new BsonDocumentBuilder()
                .append(VERSION_FIELD, version.toInt())
                .append(NAME_FIELD, name)
                .append(NAMESPACE_FIELD, database + '.' + collection)
                .append(KEYS_FIELD, keysDoc)
                .append(BACKGROUND_FIELD, background)
                .append(UNIQUE_FIELD, unique)
                .append(SPARSE_FIELD, sparse)
                .append(EXPIRE_AFTER_SECONDS_FIELD, expireAfterSeconds);
        if (storageEngine != null) {
            builder.append(STORAGE_ENGINE_FIELD, storageEngine);
        }
        return builder.build();
    }

    public static IndexOptions unmarshall(BsonDocument requestDoc)
            throws BadValueException, TypesMismatchException, NoSuchKeyException {
        int vInt = BsonReaderTool.getInteger(requestDoc, VERSION_FIELD);
        try {
            IndexVersion version = IndexVersion.fromInt(vInt);
            String name = BsonReaderTool.getString(requestDoc, NAMESPACE_FIELD);
            String namespace = BsonReaderTool.getString(requestDoc, NAMESPACE_FIELD);
            int dotIndex = namespace.indexOf('.');
            if (dotIndex < 1 || dotIndex > namespace.length() - 2) {
                throw new BadValueException("The not valid namespace " + namespace + " found");
            }
            String db = namespace.substring(0, dotIndex);
            String collection = namespace.substring(dotIndex + 1);
            boolean background = BsonReaderTool.getBoolean(requestDoc, BACKGROUND_FIELD, false);
            boolean unique = BsonReaderTool.getBoolean(requestDoc, UNIQUE_FIELD, false);
            boolean sparse = BsonReaderTool.getBoolean(requestDoc, SPARSE_FIELD, false);
            int expireAfterSeconds = BsonReaderTool.getInteger(requestDoc, EXPIRE_AFTER_SECONDS_FIELD, 0);

            BsonDocument keyDoc = BsonReaderTool.getDocument(requestDoc, KEYS_FIELD);
            Map<List<String>, Boolean> keys = Maps.newHashMapWithExpectedSize(keyDoc.size());
            for (Entry<?> entry : keyDoc) {
                List<String> key = PATH_SPLITER.splitToList(entry.getKey());
                BsonValue keyValue = entry.getValue();
                if (!keyValue.isInt32()) {
                    throw new BadValueException("It was expected an integer "
                            + "value on " + KEYS_FIELD.getFieldName() + '.'
                            + entry.getKey() + " but " + keyValue + " was found");
                }
                int keyInt = keyValue.asInt32().intValue();
                Boolean value;
                switch (keyInt) {
                    case 1:
                        value = true;
                        break;
                    case -1:
                        value = false;
                        break;
                    default:
                        throw new BadValueException("It was expected 1 or -1 as "
                            + "value of " + KEYS_FIELD.getFieldName() + '.'
                            + entry.getKey() + " but "  + keyInt + " was found");
                }
                keys.put(key, value);
            }

            BsonDocument storageEngine = BsonReaderTool.getDocument(keyDoc, KEYS_FIELD, null);
            return new IndexOptions(
                    version,
                    name,
                    db,
                    collection,
                    background,
                    unique,
                    sparse,
                    expireAfterSeconds,
                    keys,
                    storageEngine
            );
        } catch (IndexOutOfBoundsException ex) {
            throw new BadValueException("Value " + vInt + " is not a valid version");
        }
    }

    public static enum IndexVersion {
        V1,
        V2;

        private static IndexVersion fromInt(int i) {
            return IndexVersion.values()[i];
        }

        public int toInt() {
            return ordinal();
        }
    }

    private static class MyMarshaller implements Function<IndexOptions, BsonValue> {

        @Override
        public BsonDocument apply(@Nonnull IndexOptions input) {
            return input.marshall();
        }

    }

    private static class MyUnMarshaller implements Function<BsonValue, IndexOptions> {

        @Override
        public IndexOptions apply(@Nonnull BsonValue input) {
            try {
                if (!input.isDocument()) {
                    throw new IllegalArgumentException("Expected a document, "
                            + "but a " + input.getType() + " was found");
                }
                return IndexOptions.unmarshall(input.asDocument());
            } catch (BadValueException ex) {
                throw new IllegalArgumentException(ex);
            } catch (TypesMismatchException ex) {
                throw new IllegalArgumentException(ex);
            } catch (NoSuchKeyException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
    }

}
