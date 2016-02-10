
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

    private static final String VERSION_FIELD_NAME = "v";
    private static final String NAME_FIELD_NAME = "name";
    private static final String NAMESPACE_FIELD_NAME = "ns";
    private static final String BACKGROUND_FIELD_NAME = "blackground";
    private static final String UNIQUE_FIELD_NAME = "unique";
    private static final String SPARSE_FIELD_NAME = "sparse";
    private static final String EXPIRE_AFTER_SECONDS_FIELD_NAME = "expireAfterSeconds";
    private static final String KEYS_FIELD_NAME = "key";
    private static final String STORAGE_ENGINE_FIELD_NAME = "storageEngine";
    private static final NumberField<?> VERSION_FIELD = new NumberField<>(VERSION_FIELD_NAME);
    private static final StringField NAME_FIELD = new StringField(NAME_FIELD_NAME);
    private static final StringField NAMESPACE_FIELD = new StringField(NAMESPACE_FIELD_NAME);
    private static final BooleanField BACKGROUND_FIELD = new BooleanField(BACKGROUND_FIELD_NAME);
    private static final BooleanField UNIQUE_FIELD = new BooleanField(UNIQUE_FIELD_NAME);
    private static final BooleanField SPARSE_FIELD = new BooleanField(SPARSE_FIELD_NAME);
    private static final IntField EXPIRE_AFTER_SECONDS_FIELD = new IntField(EXPIRE_AFTER_SECONDS_FIELD_NAME);
    private static final DocField KEYS_FIELD = new DocField(KEYS_FIELD_NAME);
    private static final DocField STORAGE_ENGINE_FIELD = new DocField(STORAGE_ENGINE_FIELD_NAME);
    private static final Joiner PATH_JOINER = Joiner.on('.');
    private static final Splitter PATH_SPLITER = Splitter.on('.');

    private final IndexVersion version;
    private final String name;
    @Nullable
    private final String database;
    @Nullable
    private final String collection;
    private final boolean background;
    private final boolean unique;
    private final boolean sparse;
    private final int expireAfterSeconds;
    @Nonnull
    private final BsonDocument otherProps;

    private final Map<List<String>, Boolean> keys;
    @Nonnull
    private final BsonDocument storageEngine;

    public static final Function<IndexOptions, BsonValue> MARSHALLER_FUN = new MyMarshaller();
    public static final Function<BsonValue, IndexOptions> UNMARSHALLER_FUN = new MyUnMarshaller();

    public IndexOptions(
            IndexVersion version,
            String name,
            @Nullable String database,
            @Nullable String collection,
            boolean background,
            boolean unique,
            boolean sparse,
            int expireAfterSeconds,
            @Nonnull Map<List<String>, Boolean> keys,
            @Nullable BsonDocument storageEngine,
            @Nullable BsonDocument otherProps) {
        this.version = version;
        this.name = name;
        this.database = database;
        this.collection = collection;
        this.background = background;
        this.unique = unique;
        this.sparse = sparse;
        this.expireAfterSeconds = expireAfterSeconds;
        this.keys = keys;
        this.storageEngine = storageEngine != null ? storageEngine : DefaultBsonValues.EMPTY_DOC;
        this.otherProps = otherProps != null ? otherProps : DefaultBsonValues.EMPTY_DOC;
    }

    public IndexVersion getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getDatabase() {
        return database;
    }

    @Nullable 
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

    @Nonnull
    public BsonDocument getStorageEngine() {
        return storageEngine;
    }

    @Nonnull
    public BsonDocument getOtherProps() {
        return otherProps;
    }

    public BsonDocument marshall() {

        BsonDocumentBuilder keysDoc = new BsonDocumentBuilder();
        for (java.util.Map.Entry<List<String>, Boolean> entry : keys.entrySet()) {
            String path = PATH_JOINER.join(entry.getKey());
            BsonInt32 value = DefaultBsonValues.newInt(entry.getValue() ? 1 : -1);
            keysDoc.appendUnsafe(path, value);
        }

        BsonDocumentBuilder builder = new BsonDocumentBuilder()
                .appendNumber(VERSION_FIELD, version.toInt())
                .append(NAME_FIELD, name)
                .append(KEYS_FIELD, keysDoc)
                .append(BACKGROUND_FIELD, background)
                .append(UNIQUE_FIELD, unique)
                .append(SPARSE_FIELD, sparse)
                .append(EXPIRE_AFTER_SECONDS_FIELD, expireAfterSeconds);
        if (!storageEngine.isEmpty()) {
            builder.append(STORAGE_ENGINE_FIELD, storageEngine);
        }
        if (database != null && collection != null) {
            builder.append(NAMESPACE_FIELD, database + '.' + collection);
        }
        for (Entry<?> otherProp : otherProps) {
            builder.appendUnsafe(otherProp.getKey(), otherProp.getValue());
        }
        return builder.build();
    }

    public static IndexOptions unmarshall(BsonDocument requestDoc)
            throws BadValueException, TypesMismatchException, NoSuchKeyException {
        IndexVersion version = IndexVersion.V1;
        String name = null;
        String namespace = null;
        boolean background = false;
        boolean unique = false;
        boolean sparse = false;
        int expireAfterSeconds = 0;
        BsonDocument keyDoc = null;
        BsonDocument storageEngine = null;
        BsonDocumentBuilder otherBuilder = new BsonDocumentBuilder();
        for (Entry<?> entry : requestDoc) {
            String key = entry.getKey();
            switch (key) {
                case VERSION_FIELD_NAME: {
                    int vInt = BsonReaderTool.getNumeric(requestDoc, VERSION_FIELD).intValue();
                    try {
                        version = IndexVersion.fromInt(vInt);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new BadValueException("Value " + vInt + " is not a valid version");
                    }
                    break;
                }
                case NAME_FIELD_NAME: {
                    name = BsonReaderTool.getString(entry, NAME_FIELD);
                    break;
                }
                case NAMESPACE_FIELD_NAME: {
                    namespace = BsonReaderTool.getString(entry, NAMESPACE_FIELD);
                    break;
                }
                case BACKGROUND_FIELD_NAME: {
                    background = BsonReaderTool.getBoolean(entry, BACKGROUND_FIELD);
                    break;
                }
                case UNIQUE_FIELD_NAME: {
                    unique = BsonReaderTool.getBoolean(entry, UNIQUE_FIELD);
                    break;
                }
                case SPARSE_FIELD_NAME: {
                    sparse = BsonReaderTool.getBoolean(entry, SPARSE_FIELD);
                    break;
                }
                case EXPIRE_AFTER_SECONDS_FIELD_NAME: {
                    expireAfterSeconds = BsonReaderTool.getInteger(entry, EXPIRE_AFTER_SECONDS_FIELD);
                    break;
                }
                case KEYS_FIELD_NAME: {
                    keyDoc = BsonReaderTool.getDocument(entry, KEYS_FIELD);
                    break;
                }
                case STORAGE_ENGINE_FIELD_NAME: {
                    storageEngine = BsonReaderTool.getDocument(entry, STORAGE_ENGINE_FIELD);
                    break;
                }
                default: {
                    otherBuilder.appendUnsafe(key, entry.getValue());
                    break;
                }
            }
        }
        String db = null;
        String collection = null;

        if (namespace != null) {
            int dotIndex = namespace.indexOf('.');
            if (dotIndex < 1 || dotIndex > namespace.length() - 2) {
                throw new BadValueException("The not valid namespace " + namespace + " found");
            }
            db = namespace.substring(0, dotIndex);
            collection = namespace.substring(dotIndex + 1);
        }

        if (name == null) {
            throw new NoSuchKeyException(NAME_FIELD_NAME, "Indexes need names");
        }
        if (keyDoc == null) {
            throw new NoSuchKeyException(KEYS_FIELD_NAME, "Indexes need at least one key to index");
        }

        Map<List<String>, Boolean> keys = Maps.newHashMapWithExpectedSize(keyDoc.size());
        for (Entry<?> entry : keyDoc) {
            List<String> key = PATH_SPLITER.splitToList(entry.getKey());
            int keyInt = BsonReaderTool.getNumeric(entry, KEYS_FIELD.getFieldName() + '.' + entry.getKey())
                    .asInt32().intValue();

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
                            + entry.getKey() + " but " + keyInt + " was found");
            }
            keys.put(key, value);
        }

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
                storageEngine,
                otherBuilder.build()
        );
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
