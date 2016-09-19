
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import java.util.Optional;

/**
 *
 */
public class DbCmdOplogOperation extends OplogOperation {
    private static final long serialVersionUID = 1L;
    
    private static final DocField REQUEST_FIELD = new DocField("o");
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

    public Optional<String> getCommandName() {
        return Optional.ofNullable(getRequest().getFirstEntry().getKey());
    }

    @Override
    public BsonDocumentBuilder toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName())
                .append(REQUEST_FIELD, request);
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
