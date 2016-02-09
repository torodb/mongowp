
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;

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
    public BsonDocumentBuilder toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName());
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
