
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.bson.*;

/**
 *
 */
public abstract class CollectionOptions {

    private static final BsonField<Boolean> CAPPED_FIELD = BsonField.create("capped");
    private static final BsonField<Long> CAPPED_SIZE_FIELD = BsonField.create("size");
    private static final BsonField<Long> CAPPED_MAX_DOCS_FIELD = BsonField.create("max");
    private static final BsonField<BsonArray> INITIAL_EXTENT_SIZES_FIELD = BsonField.create("$nExtents");
    private static final BsonField<Long> INITIAL_NUM_EXTENDS_FIELD = BsonField.create("$nExtents");
    private static final BsonField<Boolean> AUTO_INDEX_MODE_FIELD = BsonField.create("autoIndexId");
    private static final BsonField<Integer> FLAGS_FIELD = BsonField.create("flags");
    private static final BsonField<BsonDocument> STORAGE_ENGINE_FIELD = BsonField.create("storageEngine");
    private static final BsonField<Boolean> TEMP_FIELD = BsonField.create("temp");

    public abstract boolean isCapped();

    public abstract long getCappedSize();

    public abstract long getCappedMaxDocs();

    public abstract Long getInitialNumExtents();

    public abstract List<Long> getInitialExtentSizes();

    public abstract AutoIndexMode getAutoIndexMode();

    public abstract EnumSet<Flag> getFlags();

    public abstract BsonDocument getStorageEngine();

    public abstract boolean isTemp();

    public BsonDocument marshall() {
        BsonDocumentBuilder builder = new BsonDocumentBuilder();
        marshall(builder);
        return builder.build();
    }

    public void marshall(BsonDocumentBuilder builder) {
        if (isCapped()) {
            builder.append(CAPPED_FIELD, true);
            long cappedSize = getCappedSize();
            if (cappedSize != 0) {
                builder.append(CAPPED_SIZE_FIELD, cappedSize);
            }
            long cappedMaxDocs = getCappedMaxDocs();
            if (cappedMaxDocs != 0) {
                builder.append(CAPPED_MAX_DOCS_FIELD, cappedMaxDocs);
            }
        }

        Long initialNumExtents = getInitialNumExtents();
        if (initialNumExtents != null && initialNumExtents != 0) {
            builder.append(INITIAL_NUM_EXTENDS_FIELD, initialNumExtents);
        }
        List<Long> initialExtentSizes = getInitialExtentSizes();
        if (initialExtentSizes != null && !initialExtentSizes.isEmpty()) {
            BsonArray array = new BsonArray();
            for (Long initialExtentSize : initialExtentSizes) {
                array.add(new BsonInt64(initialExtentSize));
            }
            builder.append(INITIAL_EXTENT_SIZES_FIELD, array);
        }

        AutoIndexMode autoIndexMode = getAutoIndexMode();
        if (!autoIndexMode.equals(AutoIndexMode.DEFAULT)) {
            boolean value = autoIndexMode.equals(AutoIndexMode.YES);
            builder.append(AUTO_INDEX_MODE_FIELD, value);
        }

        EnumSet<Flag> flags = getFlags();
        if (!flags.isEmpty()) {
            int value = 0;
            for (Flag flag : flags) {
                value |= flag.getBit();
            }
            builder.append(FLAGS_FIELD, value);
        }

        BsonDocument storageEngine = getStorageEngine();
        if (storageEngine != null && storageEngine.isEmpty()) {
            builder.append(STORAGE_ENGINE_FIELD, storageEngine);
        }

        boolean temp = isTemp();
        if (temp) {
            builder.append(TEMP_FIELD, temp);
        }
    }

    public static CollectionOptions unmarshal(BsonDocument doc) throws BadValueException {
        boolean capped;
        try {
            capped = BsonReaderTool.getBooleanOrNumeric(doc, CAPPED_FIELD, false);
        } catch (TypesMismatchException ex) {
            capped = true;
        }

        long cappedSize;
        try {
            cappedSize = BsonReaderTool.getLong(doc, CAPPED_SIZE_FIELD, 0);
            if (cappedSize < 0) {
                throw new BadValueException("size has to be >= 0");
            }
        } catch (TypesMismatchException ex) {
            cappedSize = 0; //backward compatibility
        }

        long cappedMaxDocs;
        try {
            cappedMaxDocs = BsonReaderTool.getLong(doc, CAPPED_MAX_DOCS_FIELD, 0);
            if (cappedMaxDocs < 0) {
                throw new BadValueException("max has to be >= 0");
            }
        } catch (TypesMismatchException ex) {
            cappedMaxDocs = 0; //backward compatibility
        }

        final ImmutableList.Builder<Long> initialExtentSizes = ImmutableList.builder();
        Long initialNumExtends;
        BsonArray array;
        try {
            array = BsonReaderTool.getArray(doc, INITIAL_EXTENT_SIZES_FIELD, null);

            if (array == null) {
                initialNumExtends = null;
            }
            else {
                initialNumExtends = null;
                for (int i = 0; i < array.size(); i++) {
                    BsonValue element = array.get(i);
                    if (!element.isNumber()) {
                        throw new BadValueException("'$nExtents'.'"+i+"' has "
                                + "the value " + element.toString()+" which is "
                                + "not a number");
                    }
                    initialExtentSizes.add(element.asNumber().longValue());
                }
            }
        } catch (TypesMismatchException ex) {
            try {
                initialNumExtends = BsonReaderTool.getLong(doc, INITIAL_NUM_EXTENDS_FIELD);
            } catch (NoSuchKeyException ex1) {
                initialNumExtends = null;
            } catch (TypesMismatchException ex1) {
                initialNumExtends = null;
            }
        }

        AutoIndexMode autoIndexMode;
        try {
            if (BsonReaderTool.getBoolean(doc, AUTO_INDEX_MODE_FIELD)) {
                autoIndexMode = AutoIndexMode.YES;
            }
            else {
                autoIndexMode = AutoIndexMode.NO;
            }
        } catch (NoSuchKeyException ex) {
            autoIndexMode = AutoIndexMode.DEFAULT;
        } catch (TypesMismatchException ex) {
            autoIndexMode = AutoIndexMode.DEFAULT;
        }

        EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
        try {
            int flagInt = BsonReaderTool.getInteger(doc, FLAGS_FIELD, 0);
            for (Flag value : Flag.values()) {
                if ((flagInt & value.bit) != 0) {
                    flags.add(value);
                }
            }
        } catch (TypesMismatchException ex) {
        }

        BsonDocument storageEngine;
        try {
            storageEngine = BsonReaderTool.getDocument(doc, STORAGE_ENGINE_FIELD, null);
        } catch (TypesMismatchException ex) {
            throw new BadValueException("'storageEngine' has to be a document.");
        }

        if (storageEngine != null) {
            if (storageEngine.isEmpty()) {
                throw new BadValueException(
                        "Empty 'storageEngine' options are invalid. "
                        + "Please remove, or include valid options");
            }
            for (Entry<String, BsonValue> entry : storageEngine.entrySet()) {
                if (!entry.getValue().isDocument()) {
                    throw new BadValueException("'storageEngie'.'"
                            + entry.getKey() + "' has to be an embedded document");
                }
            }
        }

        boolean temp;
        try {
            temp = BsonReaderTool.getBoolean(doc, TEMP_FIELD, false);
        } catch (TypesMismatchException ex) {
            throw new BadValueException("Temp field must be a boolean");
        }

        return new DefaultCollectionOptions(
                capped,
                cappedSize,
                cappedMaxDocs,
                initialNumExtends,
                initialExtentSizes.build(),
                autoIndexMode,
                flags,
                storageEngine,
                temp
        );

    }

    public static enum AutoIndexMode {
        /**
         * {@linkplain #YES} for most collections, {@linkplain #NO} for some system ones
         */
        DEFAULT,
        YES,
        NO
    }

    public static enum Flag {
        USE_POWER_OF_2(0),
        NO_PADDING(1);

        private final int bit;

        private Flag(int bit) {
            this.bit = bit;
        }

        public int getBit() {
            return bit;
        }

        public static Flag fromBit(int bit) {
            for (Flag value : Flag.values()) {
                if (value.bit == bit) {
                    return value;
                }
            }
            throw new IllegalArgumentException("There is no collection flag "
                    + "whose bit is equal to '"+ bit + "'");
        }
    }

    public static class Builder {
        private boolean capped;
        private long cappedSize;
        private long cappedMaxDocs;
        private Long initialNumExtents;
        private List<Long> initialExtentSizes;
        private AutoIndexMode autoIndexMode;
        private EnumSet<Flag> flags;
        private BsonDocument storageEngine;
        private boolean temp;

        public Builder setCapped(boolean capped) {
            this.capped = capped;
            return this;
        }

        public Builder setCappedSize(long cappedSize) {
            Preconditions.checkState(capped || cappedSize == 0);
            this.cappedSize = cappedSize;
            return this;
        }

        public Builder setCappedMaxDocs(long cappedMaxDocs) {
            Preconditions.checkState(capped || cappedMaxDocs == 0);
            this.cappedMaxDocs = cappedMaxDocs;
            return this;
        }

        public Builder setInitialNumExtents(Long initialNumExtents) {
            Preconditions.checkState(initialExtentSizes == null);
            this.initialNumExtents = initialNumExtents;
            return this;
        }

        public Builder setInitialExtentSizes(List<Long> initialExtentSizes) {
            Preconditions.checkState(initialNumExtents == null);
            this.initialExtentSizes = initialExtentSizes;
            return this;
        }

        public Builder setAutoIndexMode(@Nonnull AutoIndexMode autoIndexMode) {
            this.autoIndexMode = autoIndexMode;
            return this;
        }

        public Builder setFlags(EnumSet<Flag> flags) {
            this.flags = flags;
            return this;
        }

        public Builder setStorageEngine(BsonDocument storageEngine) {
            this.storageEngine = storageEngine;
            return this;
        }

        public Builder setTemp(boolean temp) {
            this.temp = temp;
            return this;
        }

        public CollectionOptions build() {
            return new DefaultCollectionOptions(
                    capped,
                    cappedSize,
                    cappedMaxDocs,
                    initialNumExtents,
                    ImmutableList.copyOf(initialExtentSizes),
                    autoIndexMode,
                    flags,
                    storageEngine,
                    temp
            );
        }

    }

    private static class DefaultCollectionOptions extends CollectionOptions {

        private final boolean capped;
        private final long cappedSize;
        private final long cappedMaxDocs;
        private final Long initialNumExtents;
        private final ImmutableList<Long> initialExtentSizes;
        private final AutoIndexMode autoIndexMode;
        private final EnumSet<Flag> flags;
        private final BsonDocument storageEngine;
        private final boolean temp;

        private DefaultCollectionOptions(
                boolean capped,
                long cappedSize,
                long cappedMaxDocs,
                Long initialNumExtents,
                ImmutableList<Long> initialExtentSizes,
                AutoIndexMode autoIndexMode,
                EnumSet<Flag> flags,
                BsonDocument storageEngine,
                boolean temp) {
            assert capped || cappedSize == 0 && cappedMaxDocs == 0;
            assert initialNumExtents == null || initialExtentSizes == null;

            this.capped = capped;
            this.cappedSize = cappedSize;
            this.cappedMaxDocs = cappedMaxDocs;
            this.initialNumExtents = initialNumExtents;
            this.initialExtentSizes = initialExtentSizes;
            this.autoIndexMode = autoIndexMode;
            this.flags = flags;
            this.storageEngine = storageEngine;
            this.temp = temp;
        }

        @Override
        public boolean isCapped() {
            return capped;
        }

        @Override
        public long getCappedSize() {
            return cappedSize;
        }

        @Override
        public long getCappedMaxDocs() {
            return cappedMaxDocs;
        }

        @Override
        public Long getInitialNumExtents() {
            return initialNumExtents;
        }

        @Override
        public ImmutableList<Long> getInitialExtentSizes() {
            return initialExtentSizes;
        }

        @Override
        public AutoIndexMode getAutoIndexMode() {
            return autoIndexMode;
        }

        @Override
        public EnumSet<Flag> getFlags() {
            return flags;
        }

        @Override
        public BsonDocument getStorageEngine() {
            return storageEngine;
        }

        @Override
        public boolean isTemp() {
            return temp;
        }
    }

}
