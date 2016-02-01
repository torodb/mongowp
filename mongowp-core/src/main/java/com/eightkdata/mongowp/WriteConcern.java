/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mongowp-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp;

import com.eightkdata.mongowp.annotations.Material;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.exceptions.FailedToParseException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.IntField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Immutable
public abstract class WriteConcern {
    private static final org.slf4j.Logger LOGGER
            = LoggerFactory.getLogger(WriteConcern.class);

    private static final WriteConcern ACKNOWLEDGED = new IntWriteConcern(SyncMode.NONE, 0, 1);
    private static final WriteConcern UNACKNOWLEDGED = new IntWriteConcern(SyncMode.NONE, 0, 0);

    private final SyncMode syncMode;
    private final int timeout;

    WriteConcern(@Nonnull SyncMode syncMode, @Nonnegative int timeout) {
        this.syncMode = syncMode;
        this.timeout = timeout;
    }

    public @Nonnull SyncMode getSyncMode() {
        return syncMode;
    }

    /**
     *
     * @return the timeout in millis
     */
    @Nonnegative
    public int getTimeout() {
        return timeout;
    }

    public abstract int getWInt() throws UnsupportedOperationException;

    public abstract @Nonnull String getWString() throws UnsupportedOperationException;

    public abstract @Nonnull WType getWType();

    public static WriteConcern acknowledged() {
        return new IntWriteConcern(SyncMode.NONE, 0, 0);
    }

    /**
     * Write operations wait until flush data to disk.
     * @return
     */
    public static WriteConcern fsync() {
        return new IntWriteConcern(SyncMode.FSYNC, 0, 0);
    }

    /**
     * Write operations wait until flush journa to disk.
     * @return
     */
    public static WriteConcern journal() {
        return new IntWriteConcern(SyncMode.JOURNAL, 0, 0);
    }

    public static WriteConcern majority() {
        return with(SyncMode.NONE, 0, "majority");
    }

    public static WriteConcern with(SyncMode syncMode) {
        return new IntWriteConcern(syncMode, 0, 1);
    }

    public static WriteConcern with(SyncMode syncMode, int timeout) {
        return new IntWriteConcern(syncMode, timeout, 1);
    }

    public static WriteConcern with(SyncMode syncMode, int timeout, int w) {
        return new IntWriteConcern(syncMode, timeout, w);
    }

    public static WriteConcern with(SyncMode syncMode, int timeout, String w) {
        return new StringWriteConcern(syncMode, timeout, w);
    }

    public static WriteConcern fromDocument(@Nullable BsonDocument doc, WriteConcern defaultValue) {
        if (doc == null || doc.isEmpty()) {
            return defaultValue;
        }
        try {
            return fromDocument(doc);
        } catch (FailedToParseException ex) {
            return defaultValue;
        }
    }

    public static WriteConcern fromDocument(@Nonnull BsonDocument doc) throws FailedToParseException {
        //Same behaviour as mongo/src/mongo/db/write_concern_options.cpp#parse(BSONObj)
        if (doc.isEmpty()) {
            throw new FailedToParseException("write concern object cannot be empty");
        }
        
        boolean j;
        boolean fsync;
        
        try {
            j = BsonReaderTool.getBooleanOrNumeric(doc, "j", false);
        } catch (TypesMismatchException ex) {
            throw new FailedToParseException("j must be numeric or boolean value");
        }
        try {
            fsync = BsonReaderTool.getBooleanOrNumeric(doc, "fsync", false);
        } catch (TypesMismatchException ex) {
            throw new FailedToParseException("fsync must be numeric or a boolean value");
        }

        SyncMode syncMode;

        if (j) {
            syncMode = SyncMode.JOURNAL;
            if (fsync) {
                throw new FailedToParseException("fsync and j options cannot be used together");
            }
        }
        else if (fsync) {
            syncMode = SyncMode.FSYNC;
        }
        else {
            syncMode = SyncMode.NONE;
        }

        int timeout;
        try {
            timeout = BsonReaderTool.getNumeric(doc, "timeout").intValue();
        } catch (NoSuchKeyException  ex) {
            timeout = 0;
        } catch (TypesMismatchException ex) {
            LOGGER.warn("Unexpected write concern timeout '{}'. Using the default value", doc.get("timeout"));
            timeout = 0;
        }

        BsonValue<?> wValue = doc.get("w");
        if (wValue == null) {
            return new IntWriteConcern(syncMode, timeout, 1);
        }
        if (wValue.isNumber()) {
            return new IntWriteConcern(syncMode, timeout, wValue.asNumber().intValue());
        }
        if (wValue.isString()) {
            return new StringWriteConcern(syncMode, timeout, wValue.asString().getValue());
        }
        throw new FailedToParseException("w has to be a number or a string");
    }

    private static final StringField W_TEXT_FIELD = new StringField("w");
    private static final IntField W_INT_FIELD = new IntField("w");
    private static final BooleanField FSYNC_FIELD = new BooleanField("fsync");
    private static final BooleanField JOURNAL_FIELD = new BooleanField("j");
    private static final IntField TIMEOUT_FIELD = new IntField("timeout");


    @Material
    public BsonDocument toDocument() {
        BsonDocumentBuilder builder = new BsonDocumentBuilder(3);
        if (getWType().equals(WType.TEXT)) {
            builder.append(W_TEXT_FIELD, getWString());
        }
        else {
            builder.append(W_INT_FIELD, getWInt());
        }
        switch (getSyncMode()) {
            case FSYNC:
                builder.append(FSYNC_FIELD, true);
                break;
            case JOURNAL:
                builder.append(JOURNAL_FIELD, true);
                break;
            default:
        }

        builder.append(TIMEOUT_FIELD, getTimeout());

        return builder.build();
    }

    @Override
    public String toString() {
        return toDocument().toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.syncMode);
        hash = 29 * hash + this.timeout;
        switch (getWType()) {
            case INT:
                hash = 29 * hash + getWInt();
                break;
            case TEXT:
                hash = 29 * hash + getWString().hashCode();
                break;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WriteConcern other = (WriteConcern) obj;
        if (this.timeout != other.timeout) {
            return false;
        }
        if (this.syncMode != other.syncMode) {
            return false;
        }
        if (this.getWType() != other.getWType()) {
            return false;
        }
        switch(this.getWType()) {
            case INT:
                return this.getWInt() == other.getWInt();
            case TEXT:
                return this.getWString().equals(other.getWString());
            default:
                throw new AssertionError("Unexpected WType");
        }
    }

    public static enum SyncMode {
        NONE, FSYNC, JOURNAL
    }

    public static enum WType {
        INT, TEXT;
    }
}
