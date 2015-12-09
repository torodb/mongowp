
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 */
public class DbOplogOperation extends OplogOperation {

    public DbOplogOperation(String database, OpTime optime, long h, OplogVersion version, boolean fromMigrate) {
        super(database, optime, h, version, fromMigrate);
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.DB;
    }

    @Override
    public BsonDocument toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append("op", new BsonString(getType().getOplogName()));
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
