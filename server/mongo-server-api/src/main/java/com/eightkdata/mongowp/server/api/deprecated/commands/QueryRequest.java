package com.eightkdata.mongowp.server.api.deprecated.commands;

import com.eightkdata.mongowp.bson.BsonDocument;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 *
 */
public class QueryRequest extends CollectionCommandRequest {

    private final BsonDocument query;
    private final BsonDocument projection;
    private final int numberToSkip;
    private final int limit;
    private final boolean autoclose;
    private final boolean tailable;
    private final boolean slaveOk;
    private final boolean oplogReplay;
    private final boolean noCursorTimeout;
    private final boolean awaitData;
    private final boolean exhaust;
    private final boolean partial;    

    private QueryRequest(
            String database,
            AttributeMap attributes, 
            String collection, 
            BsonDocument query,
            BsonDocument projection,
            int numberToSkip, 
            int limit, 
            boolean autoclose, 
            boolean tailable, 
            boolean slaveOk, 
            boolean oplogReplay, 
            boolean noCursorTimeout, 
            boolean awaitData, 
            boolean exhaust, 
            boolean partial) {
        super(database, attributes, collection);
        this.query = query;
        this.projection = projection;
        this.numberToSkip = numberToSkip;
        this.limit = limit;
        this.autoclose = autoclose;
        this.tailable = tailable;
        this.slaveOk = slaveOk;
        this.oplogReplay = oplogReplay;
        this.noCursorTimeout = noCursorTimeout;
        this.awaitData = awaitData;
        this.exhaust = exhaust;
        this.partial = partial;
    }

    @Nullable
    public BsonDocument getQuery() {
        return query;
    }

    @Nullable
    public BsonDocument getProjection() {
        return projection;
    }

    @Nonnegative
    public int getNumberToSkip() {
        return numberToSkip;
    }

    @Nonnegative
    public int getLimit() {
        return limit;
    }

    public boolean isAutoclose() {
        return autoclose;
    }

    public boolean isTailable() {
        return tailable;
    }

    public boolean isSlaveOk() {
        return slaveOk;
    }

    public boolean isOplogReplay() {
        return oplogReplay;
    }

    public boolean isNoCursorTimeout() {
        return noCursorTimeout;
    }

    public boolean isAwaitData() {
        return awaitData;
    }

    public boolean isExhaust() {
        return exhaust;
    }

    public boolean isPartial() {
        return partial;
    }

    public static class Builder {

        private final String database;
        private final AttributeMap attributes;
        private String collection;
        private BsonDocument query;
        private BsonDocument projection;
        private int numberToSkip;
        private int limit;
        private boolean autoclose;
        private boolean tailable;
        private boolean slaveOk;
        private boolean oplogReplay;
        private boolean noCursorTimeout;
        private boolean awaitData;
        private boolean exhaust;
        private boolean partial;    

        public Builder(String database, AttributeMap attributes) {
            this.database = database;
            this.attributes = attributes;
        }

        public Builder(String database, AttributeMap attributes, String collection) {
            this.database = database;
            this.attributes = attributes;
            this.collection = collection;
        }

        public String getCollection() {
            return collection;
        }

        public Builder setCollection(String collection) {
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

        public BsonDocument getProjection() {
            return projection;
        }

        public Builder setProjection(BsonDocument projection) {
            this.projection = projection;
            return this;
        }

        public int getNumberToSkip() {
            return numberToSkip;
        }

        public Builder setNumberToSkip(int numberToSkip) {
            this.numberToSkip = numberToSkip;
            return this;
        }

        public int getLimit() {
            return limit;
        }

        public Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public boolean isAutoclose() {
            return autoclose;
        }

        public Builder setAutoclose(boolean autoclose) {
            this.autoclose = autoclose;
            return this;
        }

        public boolean isTailable() {
            return tailable;
        }

        public Builder setTailable(boolean tailable) {
            this.tailable = tailable;
            return this;
        }

        public boolean isSlaveOk() {
            return slaveOk;
        }

        public Builder setSlaveOk(boolean slaveOk) {
            this.slaveOk = slaveOk;
            return this;
        }

        public boolean isOplogReplay() {
            return oplogReplay;
        }

        public Builder setOplogReplay(boolean oplogReplay) {
            this.oplogReplay = oplogReplay;
            return this;
        }

        public boolean isNoCursorTimeout() {
            return noCursorTimeout;
        }

        public Builder setNoCursorTimeout(boolean noCursorTimeout) {
            this.noCursorTimeout = noCursorTimeout;
            return this;
        }

        public boolean isAwaitData() {
            return awaitData;
        }

        public Builder setAwaitData(boolean awaitData) {
            this.awaitData = awaitData;
            return this;
        }

        public boolean isExhaust() {
            return exhaust;
        }

        public Builder setExhaust(boolean exhaust) {
            this.exhaust = exhaust;
            return this;
        }

        public boolean isPartial() {
            return partial;
        }

        public Builder setPartial(boolean partial) {
            this.partial = partial;
            return this;
        }
        
        public QueryRequest build() {
            return new QueryRequest(
                    database,
                    attributes, 
                    collection, 
                    query, 
                    projection, 
                    numberToSkip, 
                    limit, 
                    autoclose, 
                    tailable, 
                    slaveOk, 
                    oplogReplay, 
                    noCursorTimeout, 
                    awaitData, 
                    exhaust, 
                    partial
            );
        }
    }

}
