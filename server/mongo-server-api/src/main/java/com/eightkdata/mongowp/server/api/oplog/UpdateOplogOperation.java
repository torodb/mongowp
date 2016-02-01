
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class UpdateOplogOperation extends CollectionOplogOperation {

    private static final DocField FILTER_FIELD = new DocField("o");
    private static final DocField MODIFICATION_FIELD = new DocField("o2");
    private static final BooleanField UPSERT_FIELD = new BooleanField("b");

    private final BsonDocument filter;
    //TODO: Change to a type safe object
    private final BsonDocument modification;
    private final boolean upsert;

    public UpdateOplogOperation(
            BsonDocument filter,
            String database,
            String collection,
            OpTime optime,
            long h,
            OplogVersion version,
            boolean fromMigrate,
            BsonDocument modification,
            boolean upsert) {
        super(database, collection, optime, h, version, fromMigrate);
        this.filter = filter;
        this.modification = modification;
        this.upsert = upsert;
    }

    public BsonDocument getFilter() {
        return filter;
    }

    public BsonDocument getModification() {
        return modification;
    }

    public boolean isUpsert() {
        return upsert;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.UPDATE;
    }

    @Override
    public BsonDocumentBuilder toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName())
                .append(FILTER_FIELD, filter)
                .append(MODIFICATION_FIELD, modification)
                .append(UPSERT_FIELD, upsert);
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }
    
}
