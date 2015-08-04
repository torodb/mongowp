
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class OplogOperation {

    private final @Nonnull String database;
    /**
     * when this operation was performed
     */
    private final @Nonnull OpTime optime;
    private final long h;
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
        this.h = h;
        this.version = version;
        this.fromMigrate = fromMigrate;
    }

    public abstract OplogOperationType getType();

    public abstract <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg);

    public String getDatabase() {
        return database;
    }

    public OpTime getOptime() {
        return optime;
    }

    public long getH() {
        return h;
    }

    public OplogVersion getVersion() {
        return version;
    }

    public boolean isFromMigrate() {
        return fromMigrate;
    }
}
