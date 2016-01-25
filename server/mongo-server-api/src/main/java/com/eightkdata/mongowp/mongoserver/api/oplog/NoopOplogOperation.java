
package com.eightkdata.mongowp.mongoserver.api.oplog;

import com.eightkdata.mongowp.OpTime;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 */
public class NoopOplogOperation extends OplogOperation {

    private final @Nullable BsonDocument msg;

    public NoopOplogOperation(@Nullable BsonDocument msg, String database, OpTime optime, long h, OplogVersion version, boolean fromMigrate) {
        super(database, optime, h, version, fromMigrate);
        this.msg = msg;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.NOOP;
    }

    @Override
    public BsonDocument toDescriptiveBson() {
        BsonDocument doc = super.toDescriptiveBson()
                .append("op", new BsonString(getType().getOplogName()))
                .append("ns", new BsonString(""));
        if (msg != null) {
            doc.append("o", msg);
        }
        return doc;
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
