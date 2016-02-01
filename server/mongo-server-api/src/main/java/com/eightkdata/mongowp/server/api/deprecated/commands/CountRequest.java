
package com.eightkdata.mongowp.server.api.deprecated.commands;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.google.common.base.Preconditions;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class CountRequest extends CollectionCommandRequest {

    private final BsonDocument query;
    private final int limit;
    private final int skip;
    private final String hint;

    private CountRequest(
            String database,
            AttributeMap attributes,
            String collection, 
            BsonDocument query,
            int limit, 
            int skip, 
            String hint) {
        super(database, attributes, collection);
        this.query = query;
        this.limit = limit;
        this.skip = skip;
        this.hint = hint;
    }

    /**
     * A query that selects which documents to count in a collection.
     * 
     * @return the filter or null if all documents shall be count
     */
    @Nullable
    public BsonDocument getQuery() {
        return query;
    }

    @Nonnegative
    public int getLimit() {
        return limit;
    }

    @Nonnegative
    public int getSkip() {
        return skip;
    }

    /**
     * The index to use. 
     * Specify either the index name as a string.
     * @return 
     */
    @Nullable
    public String getHint() {
        return hint;
    }
    
    public static class Builder {
        private final String database;
        private final AttributeMap attributes;
        private String collection;
        private BsonDocument query;
        private int limit;
        private int skip;
        private String hint;

        public Builder(@Nonnull String database, @Nonnull AttributeMap attributes) {
            this.database = database;
            this.attributes = attributes;
        }

        public Builder(
                @Nonnull String database, 
                @Nonnull AttributeMap attributes, 
                @Nonnull String collection) {
            this.database = database;
            this.attributes = attributes;
            this.collection = collection;
        }

        public Builder(
                @Nonnull String database, 
                @Nonnull AttributeMap attributes, 
                @Nonnull String collection, 
                @Nonnull BsonDocument query) {
            this.database = database;
            this.attributes = attributes;
            this.collection = collection;
            this.query = query;
        }

        public String getCollection() {
            return collection;
        }

        public Builder setCollection(@Nonnull String collection) {
            this.collection = collection;
            return this;
        }

        public BsonDocument getQuery() {
            return query;
        }

        public Builder setQuery(BsonDocument query) {
            this.query = query;
            return this;
        }

        public int getLimit() {
            return limit;
        }

        public Builder setLimit(@Nonnegative int limit) {
            Preconditions.checkArgument(limit >= 0);
            this.limit = limit;
            return this;
        }

        public int getSkip() {
            return skip;
        }

        public Builder setSkip(@Nonnegative int skip) {
            Preconditions.checkArgument(skip >= 0);
            this.skip = skip;
            return this;
        }

        public String getHint() {
            return hint;
        }

        public Builder setHint(String index) {
            this.hint = index;
            return this;
        }
        
        public CountRequest build() {
            Preconditions.checkState(collection != null);
            return new CountRequest(database, attributes, collection, query, limit, skip, hint);
        }
    }
    
}
