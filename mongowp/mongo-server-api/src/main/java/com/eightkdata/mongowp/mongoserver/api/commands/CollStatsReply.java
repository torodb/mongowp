package com.eightkdata.mongowp.mongoserver.api.commands;

import com.eightkdata.mongowp.mongoserver.api.callback.MessageReplier;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.nettybson.mongodriver.MongoBSONDocument;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class CollStatsReply implements Reply {

    private final int scale;
    private final String database;
    private final String collection;
    private final Number count;
    private final Number size;
    private final Number storageSize;
    private final Number numExtents;
    private final Number lastExtentSize;
    private final Number paddingFactor;
    private final boolean _idIndexExists;
    private final boolean usePowerOf2Sizes;
    private final boolean capped;
    private final Number maxIfCapped;
    private final Map<String, ? extends Number> sizeByIndex;

    private CollStatsReply(
            int scale,
            String database,
            String collection,
            Number count,
            Number size,
            Number storageSize,
            Number numExtents,
            Number lastExtentSize,
            Number paddingFactor,
            boolean _idIndexExists,
            boolean usePowerOf2Sizes,
            boolean capped,
            Number maxIfCapped,
            Map<String, ? extends Number> sizeByIndex) {
        this.scale = scale;
        this.database = database;
        this.collection = collection;
        this.count = count;
        this.size = size;
        this.storageSize = storageSize;
        this.numExtents = numExtents;
        this.lastExtentSize = lastExtentSize;
        this.paddingFactor = paddingFactor;
        this._idIndexExists = _idIndexExists;
        this.usePowerOf2Sizes = usePowerOf2Sizes;
        this.capped = capped;
        this.maxIfCapped = maxIfCapped;
        this.sizeByIndex = sizeByIndex;
    }

    @Override
    public void reply(MessageReplier replier) {
        Map<String, Object> keyValue = Maps.newHashMapWithExpectedSize(15);
        keyValue.put("ns", database + '.' + collection);
        keyValue.put("count", toIntIfPossible(count));
        keyValue.put("size", toIntIfPossible(size));
        if (count.longValue() != 0) {
            Number avgObjSize = scale * size.longValue() / count.longValue();
            keyValue.put("avgObjSize", toIntIfPossible(avgObjSize));
        }
        keyValue.put("storageSize", toIntIfPossible(storageSize));
        keyValue.put("numExtents", numExtents);
        keyValue.put("nindexes", sizeByIndex.size());
        keyValue.put("lastExtentSize", lastExtentSize);
        keyValue.put("paddingFactor", paddingFactor);
        keyValue.put("systemFlags", _idIndexExists ? 1 : 0);
        keyValue.put("userFlags", usePowerOf2Sizes ? 1 : 0);
        keyValue.put("totalIndexSize", getTotalIndexSize());
        keyValue.put("indexSizes", toIntIfPossible(sizeByIndex));
        keyValue.put("capped", capped);
        if (maxIfCapped != null) {
            keyValue.put("max", toIntIfPossible(maxIfCapped));
        }
        keyValue.put("ok", MongoWP.OK);
        
        replier.replyMessageNoCursor(new MongoBSONDocument(keyValue));
    }
    
    private Number toIntIfPossible(Number number) {
        if (number instanceof Double && (((Double) number).isInfinite() || ((Double) number).isNaN())) {
            return number;
        }
        if (number.longValue() < Integer.MAX_VALUE) {
            return number.intValue();
        }
        return number;
    }
    
    private Map<String, Number> toIntIfPossible(Map<String, ? extends Number> old) {
        Map<String, Number> result = Maps.newHashMapWithExpectedSize(old.size());
        for (Map.Entry<String, ? extends Number> entry : old.entrySet()) {
            if (entry.getValue().longValue() < Integer.MAX_VALUE) {
                result.put(entry.getKey(), entry.getValue().intValue());
            }
            else {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
    
    private Number getTotalIndexSize() {
        long totalSize = 0;
        for (Number indexSize : sizeByIndex.values()) {
            totalSize += indexSize.longValue();
        }
        return toIntIfPossible(totalSize);
    }

    public static class Builder {

        private int scale;
        private String database;
        private String collection;
        private Number count;
        private Number size;
        private Number storageSize;
        private Number numExtents;
        private Number lastExtentSize;
        private Number paddingFactor;
        private boolean _idIndexExists;
        private boolean usePowerOf2Sizes;
        private boolean capped;
        private Number maxIfCapped;
        private Map<String, ? extends Number> sizeByIndex;

        public Builder(@Nonnull String database, @Nonnull String collection) {
            this.database = database;
            this.collection = collection;
        }

        public int getScale() {
            return scale;
        }

        public Builder setScale(int scale) {
            Preconditions.checkArgument(scale > 0, "Scale must be a positive number");
            this.scale = scale;
            return this;
        }
        
        public Number getCount() {
            return count;
        }

        /**
         *
         * @param count The number of objects or documents in this collection.
         * @return 
         */
        public Builder setCount(@Nonnull @Nonnegative Number count) {
            this.count = count;
            return this;
        }

        public Number getSize() {
            return size;
        }

        /**
         * The total size of all records in a collection. This value does not
         * include the record header, which is 16 bytes per record, but does
         * include the record’s padding. Additionally size does not include the
         * size of any indexes associated with the collection.
         * <p>
         * The scale argument affects this value.
         * <p>
         * @param size
         * @return 
         */
        public Builder setSize(@Nonnull @Nonnegative Number size) {
            this.size = size;
            return this;
        }

        public Number getStorageSize() {
            return storageSize;
        }

        /**
         * The total amount of storage allocated to this collection for document
         * storage. The scale argument affects this value. The storageSize does
         * not decrease as you remove or shrink documents.
         * <p>
         * @param storageSize
         * @return 
         */
        public Builder setStorageSize(@Nonnull @Nonnegative Number storageSize) {
            this.storageSize = storageSize;
            return this;
        }

        public Number getNumExtents() {
            return numExtents;
        }

        /**
         * The total number of contiguously allocated data file regions.
         * <p>
         * @param numExtents
         * @return 
         */
        public Builder setNumExtents(@Nonnull @Nonnegative Number numExtents) {
            this.numExtents = numExtents;
            return this;
        }

        public Number getLastExtentSize() {
            return lastExtentSize;
        }

        /**
         * The size of the last extent allocated. The scale argument affects
         * this value.
         * <p>
         * @param lastExtentSize
         * @return 
         */
        public Builder setLastExtentSize(@Nonnull @Nonnegative Number lastExtentSize) {
            this.lastExtentSize = lastExtentSize;
            return this;
        }

        public Number getPaddingFactor() {
            return paddingFactor;
        }

        /**
         * The amount of space added to the end of each document at insert time.
         * The document padding provides a small amount of extra space on disk
         * to allow a document to grow slightly without needing to move the
         * document. mongod automatically calculates this padding factor
         * <p>
         * @param paddingFactor
         * @return 
         */
        public Builder setPaddingFactor(@Nonnull @Nonnegative Number paddingFactor) {
            this.paddingFactor = paddingFactor;
            return this;
        }

        public boolean isIdIndexExists() {
            return _idIndexExists;
        }

        /**
         *
         * @param _idIndexExists true iff index on attribute _id exists.
         * @return 
         */
        public Builder setIdIndexExists(boolean _idIndexExists) {
            this._idIndexExists = _idIndexExists;
            return this;
        }

        public boolean isUsePowerOf2Sizes() {
            return usePowerOf2Sizes;
        }

        /**
         *
         * @param usePowerOf2Sizes true iff usePowerOf2Sizes is enabled
         * @return 
         */
        public Builder setUsePowerOf2Sizes(boolean usePowerOf2Sizes) {
            this.usePowerOf2Sizes = usePowerOf2Sizes;
            return this;
        }

        public boolean isCapped() {
            return capped;
        }

        /**
         * This field will be “true” if the collection is capped.
         * <p>
         * @param capped
         * @return 
         */
        public Builder setCapped(boolean capped) {
            this.capped = capped;
            return this;
        }

        public Number getMaxIfCapped() {
            return maxIfCapped;
        }

        /**
         * Shows the maximum number of documents that may be present in a capped
         * collection.
         * <p>
         * @param maxIfCapped
         * @return 
         */
        public Builder setMaxIfCapped(@Nullable @Nonnegative Number maxIfCapped) {
            this.maxIfCapped = maxIfCapped;
            return this;
        }

        public Map<String, ? extends Number> getSizeByIndex() {
            return sizeByIndex;
        }

        /**
         * This field specifies the key and size of every existing index on the
         * collection. The scale argument affects this value.
         * <p>
         * @param sizeByIndex
         * @return 
         */
        public Builder setSizeByIndex(@Nonnull Map<String, ? extends Number> sizeByIndex) {
            this.sizeByIndex = sizeByIndex;
            return this;
        }

        public CollStatsReply build() {
            return new CollStatsReply(
                    scale,
                    database,
                    collection,
                    count,
                    size,
                    storageSize,
                    numExtents,
                    lastExtentSize,
                    paddingFactor,
                    _idIndexExists,
                    usePowerOf2Sizes,
                    capped,
                    maxIfCapped,
                    sizeByIndex
            );
        }
    }

}
