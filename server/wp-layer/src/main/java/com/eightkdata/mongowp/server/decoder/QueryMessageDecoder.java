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


package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.*;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonDouble;
import com.eightkdata.mongowp.bson.impl.SingleEntryBsonDocument;
import com.eightkdata.mongowp.bson.netty.NettyBsonDocumentReader;
import com.eightkdata.mongowp.bson.netty.NettyStringReader;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReaderException;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.InvalidBsonException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.messages.request.QueryMessage.Builder;
import com.eightkdata.mongowp.messages.request.QueryMessage.ExplainOption;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOption;
import com.eightkdata.mongowp.messages.request.QueryMessage.QueryOptions;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.server.util.EnumBitFlags;
import com.eightkdata.mongowp.server.util.EnumInt32FlagsUtil;
import io.netty.buffer.ByteBuf;
import java.util.EnumSet;
import java.util.Locale;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType.*;


/**
 *
 */
@Singleton
public class QueryMessageDecoder extends AbstractMessageDecoder<QueryMessage> {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(QueryMessageDecoder.class);

    private final NettyStringReader stringReader;
    private final NettyBsonDocumentReader docReader;

    @Inject
    public QueryMessageDecoder(NettyStringReader stringReader, NettyBsonDocumentReader docReader) {
        this.stringReader = stringReader;
        this.docReader = docReader;
    }

    @Override
    public @Nonnegative
    QueryMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws MongoException {

        try {
            MyBsonContext bsonContext = new MyBsonContext(buffer);

            int flags = buffer.readInt();
            String fullCollectionName = stringReader.readCString(buffer, true);
            int numberToSkip = buffer.readInt();
            int numberToReturn = buffer.readInt();

            //TODO: improve the way database and cache are pooled
            QueryMessage.Builder queryBuilder = new Builder(
                    requestBaseMessage,
                    bsonContext,
                    getDatabase(fullCollectionName).intern(),
                    getCollection(fullCollectionName).intern(),
                    getQueryOptions(flags)
            );

            analyzeDoc(buffer, queryBuilder);
            
            BsonDocument returnFieldsSelector = null;
            if (buffer.readableBytes() > 0) {
                returnFieldsSelector = docReader.readDocument(HEAP, buffer);
            }

            assert buffer.readableBytes() == 0;

            queryBuilder.setReturnFieldsSelector(returnFieldsSelector)
                    .setNumberToReturn(numberToReturn)
                    .setNumberToSkip(numberToSkip);

            return queryBuilder.build();
        } catch (BsonDocumentReaderException ex) {
            throw new InvalidBsonException(ex);
        }
    }

    private QueryOptions getQueryOptions(int flags) {
        EnumSet<QueryOption> qoSet = EnumSet.noneOf(QueryOption.class);
        if (EnumInt32FlagsUtil.isActive(Flag.TAILABLE_CURSOR, flags)) {
            qoSet.add(QueryOption.TAILABLE_CURSOR);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.SLAVE_OK, flags)) {
            qoSet.add(QueryOption.SLAVE_OK);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.OPLOG_REPLAY, flags)) {
            qoSet.add(QueryOption.OPLOG_REPLAY);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.NO_CURSOR_TIMEOUT, flags)) {
            qoSet.add(QueryOption.NO_CURSOR_TIMEOUT);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.AWAIT_DATA, flags)) {
            qoSet.add(QueryOption.AWAIT_DATA);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.EXHAUST, flags)) {
            qoSet.add(QueryOption.EXHAUST);
        }
        if (EnumInt32FlagsUtil.isActive(Flag.PARTIAL, flags)) {
            qoSet.add(QueryOption.PARTIAL);
        }

        return new QueryOptions(qoSet);
    }

    private void analyzeDoc(@Loose @ModifiesIndexes ByteBuf docByteBuf, Builder messageBuilder) throws BadValueException, BsonDocumentReaderException {
        BsonDocument doc = docReader.readDocument(OFFHEAP_VALUES, docByteBuf);

        if (doc.containsKey("$query")) {

            for (Entry<?> entry : doc) {
                switch (entry.getKey().toLowerCase(Locale.ENGLISH)) {
                    case "$query": {
                        messageBuilder.setQuery(getQuery(entry.getValue()));
                        break;
                    }
                    case "$comment": {
                        messageBuilder.setComment(getComment(entry.getValue()));
                        break;
                    }
                    case "$explain": {
                        messageBuilder.setExplainOption(getExplain(entry.getValue()));
                        break;
                    }
                    case "$hint": {
                        messageBuilder.setHint(getHint(entry.getValue()));
                        break;
                    }
                    case "$maxScan": {
                        messageBuilder.setMaxScan(getMaxScan(entry.getValue()));
                        break;
                    }
                    case "$maxTimeMS": {
                        messageBuilder.setMaxTimeMS(getMaxTimeMs(entry.getValue()));
                        break;
                    }
                    case "$max": {
                        messageBuilder.setMax(getMax(entry.getValue()));
                        break;
                    }
                    case "$min": {
                        messageBuilder.setMin(getMin(entry.getValue()));
                        break;
                    }
                    case "$orderBy": {
                        messageBuilder.setOrderBy(getOrderBy(entry.getValue()));
                        break;
                    }
                    case "$returnKey": {
                        messageBuilder.setReturnKey(getReturnKey(entry.getValue()));
                        break;
                    }
                    case "$showDiskLoc": {
                        messageBuilder.setShowDiscLoc(getShowDiskLoc(entry.getValue()));
                        break;
                    }
                    case "$snapshot": {
                        messageBuilder.setSnapshot(getSnapshot(entry.getValue()));
                        break;
                    }
                    default: {
                        LOGGER.warn("Ignored attribute/query operator '{}' "
                                + "because it is not recognized", entry.getKey());
                    }
                }
                assert messageBuilder.getQuery() != null;
            }
        } else {
            messageBuilder.setQuery(doc);
        }
    }

    private BsonDocument getQuery(BsonValue<?> value) throws BadValueException {
        if (!(value instanceof BsonDocument)) {
            throw new BadValueException("Unknown top level operator: $query");
        }
        return (BsonDocument) value;
    }

    @Nullable
    private String getComment(BsonValue<?> value) {
        if (value instanceof BsonString) {
            return ((BsonString) value).getValue();
        }
        return null;
    }

    @Nonnull
    private ExplainOption getExplain(BsonValue<?> value) {
        //TODO: Parse $explain
        return ExplainOption.NONE;
    }

    @Nullable
    private BsonDocument getHint(BsonValue<?> value) throws BadValueException {
        if (value instanceof BsonDocument) {
            return (BsonDocument) value;
        }
        if (value instanceof BsonString) {
            return new SingleEntryBsonDocument(((BsonString) value).getValue(), PrimitiveBsonDouble.newInstance(1));
        }
        throw new BadValueException("$hint must be either a string or a nested object");
    }

    private long getMaxScan(BsonValue<?> value) {
        switch (value.getType()) {
            case INT32:
                return ((BsonInt32) value).intValue();
            case INT64:
                return ((BsonInt64) value).longValue();
            case DOUBLE:
                return Math.round(((BsonDouble) value).doubleValue());
            default:
                return -1;
        }
    }

    private int getMaxTimeMs(BsonValue<?> value) {
        //TODO: Parse $maxScan
        return -1;
    }

    @Nullable
    private BsonDocument getMax(BsonValue<?> value) {
        //TODO: readDocument $max
        return null;
    }

    @Nullable
    private BsonDocument getMin(BsonValue<?> value) {
        //TODO: readDocument $min
        return null;
    }

    private boolean getReturnKey(BsonValue<?> value) {
        switch (value.getType()) {
            case BOOLEAN:
                return ((BsonBoolean) value).getPrimitiveValue();
            case UNDEFINED:
            case NULL:
                return false;
            default:
                return true;
        }
    }

    @Nullable
    private BsonDocument getOrderBy(BsonValue<?> value) {
        //TODO: readDocument $orderBy
        return null;
    }

    private boolean getSnapshot(BsonValue<?> value) {
        switch (value.getType()) {
            case BOOLEAN:
                return ((BsonBoolean) value).getPrimitiveValue();
            case UNDEFINED:
            case NULL:
                return false;
            default:
                return true;
        }
    }

    private boolean getShowDiskLoc(BsonValue<?> value) {
        switch (value.getType()) {
            case BOOLEAN:
                return ((BsonBoolean) value).getPrimitiveValue();
            case UNDEFINED:
            case NULL:
                return false;
            default:
                return true;
        }
    }

    private enum Flag implements EnumBitFlags {
        TAILABLE_CURSOR(1),
        SLAVE_OK(2),
        OPLOG_REPLAY(3),
        NO_CURSOR_TIMEOUT(4),
        AWAIT_DATA(5),
        EXHAUST(6),
        PARTIAL(7);

        @Nonnegative private final int flagBitPosition;

        private Flag(@Nonnegative int flagBitPosition) {
            this.flagBitPosition = flagBitPosition;
        }

        @Override
        public int getFlagBitPosition() {
            return flagBitPosition;
        }
    }
}
