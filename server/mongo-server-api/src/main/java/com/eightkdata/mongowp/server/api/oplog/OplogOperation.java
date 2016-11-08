/*
 * MongoWP - Mongo Server: API
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class OplogOperation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    static final StringField OP_FIELD = new StringField("op");
    private static final TimestampField OPTIME_FIELD = new TimestampField("ts");
    private static final LongField HASH_FIELD = new LongField("h");
    private static final IntField VERSION_FIELD = new IntField("v");
    private static final BooleanField FROM_MIGRATE_FIELD = new BooleanField("fromMigrate");

    private final @Nonnull String database;
    /**
     * when this operation was performed
     */
    private final @Nonnull OpTime optime;
    private final long hash;
    private final @Nonnull OplogVersion version;
    private final boolean fromMigrate;

    public OplogOperation(
            @Nonnull String database,
            @Nonnull OpTime optime,
            long h,
            @Nonnull OplogVersion version,
            boolean fromMigrate) {
        this.database = database;
        this.optime = optime;
        this.hash = h;
        this.version = version;
        this.fromMigrate = fromMigrate;
    }

    public abstract OplogOperationType getType();

    public abstract <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg);

    public String getDatabase() {
        return database;
    }

    public OpTime getOpTime() {
        return optime;
    }

    public long getHash() {
        return hash;
    }

    public OplogVersion getVersion() {
        return version;
    }

    public boolean isFromMigrate() {
        return fromMigrate;
    }

    public BsonDocumentBuilder toDescriptiveBson() {
        BsonDocumentBuilder result = new BsonDocumentBuilder()
                .append(OPTIME_FIELD, optime)
                .append(HASH_FIELD, hash)
                .append(VERSION_FIELD, version.getNumericValue());
        if (fromMigrate) {
            result.append(FROM_MIGRATE_FIELD, true);
        }
        return result;
    }

    @Override
    public String toString() {
        BsonDocumentBuilder bson = toDescriptiveBson();
        return bson.build().toString();
    }
}
