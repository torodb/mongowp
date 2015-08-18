
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;

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

    public OpTime getOptime() {
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
    
    public BsonDocument toDescriptiveBson() {
        BsonDocument doc = new BsonDocument()
                .append("ts", optime.asBsonTimestamp())
                .append("h", new BsonInt64(hash))
                .append("v", new BsonInt32(version.getNumericValue()));
        if (fromMigrate) {
            doc.append("fromMigrate", BsonBoolean.TRUE);
        }
        return doc;
    }

    @Override
    public String toString() {
        BsonDocument bson = toDescriptiveBson();
        return bson.toString();
    }
}
