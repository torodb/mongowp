/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */


package com.eightkdata.mongowp.messages.request;

import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

import static com.eightkdata.mongowp.messages.request.QueryMessage.QueryOption.*;

/**
 *
 */
public class QueryMessage extends AbstractRequestMessage implements RequestMessage {

    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_QUERY;

    @Override
    public RequestOpCode getOpCode() {
        return REQUEST_OP_CODE;
    }

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnegative private final int numberToSkip;
    private final int numberToReturn;
    @Nonnull private final QueryOptions queryOptions;
    @Nonnull private final BsonDocument document;
    @Nullable private final BsonDocument returnFieldsSelector;

    public QueryMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull String database,
            @Nonnull String collection,
            @Nonnegative int numberToSkip,
            int numberToReturn,
            QueryOptions queryOptions,
            @Nonnull BsonDocument document,
            BsonDocument returnFieldsSelector
    ) {
        super(requestBaseMessage);
        this.database = database;
        this.collection = collection;
        this.numberToSkip = numberToSkip;
        this.numberToReturn = numberToReturn;
        this.document = document;
        this.returnFieldsSelector = returnFieldsSelector;
        this.queryOptions = queryOptions;
    }

    @Nonnull
    public String getDatabase() {
        return database;
    }

    @Nonnull
    public String getCollection() {
        return collection;
    }

    public int getNumberToSkip() {
        return numberToSkip;
    }

    public int getNumberToReturn() {
        return numberToReturn;
    }

    public QueryOptions getQueryOptions() {
        return queryOptions;
    }

    @Nonnull
    public BsonDocument getDocument() {
        return document;
    }

    public BsonDocument getReturnFieldsSelector() {
        return returnFieldsSelector;
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return "QueryMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", numberToSkip=" + numberToSkip +
                ", numberToReturn=" + numberToReturn +
                ", document=" + document +
                ", returnFieldsSelector=" + returnFieldsSelector +
                '}';
    }

    @Immutable
    public static class QueryOptions {
        final boolean tailable;
        final boolean slaveOk;
        final boolean oplogReplay;
        final boolean noCursorTimeout;
        final boolean awaitData;
        final boolean exhaust;
        final boolean partial;

        public QueryOptions(Set<QueryOption> queryOptions) {
            this.tailable = queryOptions.contains(TAILABLE_CURSOR);
            this.slaveOk = queryOptions.contains(SLAVE_OK);
            this.oplogReplay = queryOptions.contains(OPLOG_REPLAY);
            this.noCursorTimeout = queryOptions.contains(NO_CURSOR_TIMEOUT);
            this.awaitData = queryOptions.contains(AWAIT_DATA);
            this.exhaust = queryOptions.contains(EXHAUST);
            this.partial = queryOptions.contains(PARTIAL);
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

    }

    public static enum QueryOption {
        TAILABLE_CURSOR,
        SLAVE_OK,
        OPLOG_REPLAY,
        NO_CURSOR_TIMEOUT,
        AWAIT_DATA,
        EXHAUST,
        PARTIAL;
    }
}