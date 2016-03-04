
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class OplogOperation {
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
