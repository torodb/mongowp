
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 */
public class DbCmdOplogOperation extends OplogOperation {

    private final BsonDocument request;

    public DbCmdOplogOperation(
            BsonDocument request,
            String database,
            OpTime optime,
            long h,
            OplogVersion version,
            boolean fromMigrate) {
        super(database, optime, h, version, fromMigrate);
        this.request = request;
    }

    /**
     * This document is like a request recived as a query on wire protocol.
     * @return
     */
    public BsonDocument getRequest() {
        return request;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.DB_CMD;
    }

    @Override
    public BsonDocument toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append("op", new BsonString(getType().getOplogName()))
                .append("o", request);
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
