
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
public class DeleteOplogOperation extends CollectionOplogOperation {
    private static final DocField FILFER_FIELD = new DocField("o");
    private static final BooleanField JUST_ONE_FIELD = new BooleanField("b");


    private final BsonDocument filter;
    private final boolean justOne;

    public DeleteOplogOperation(
            BsonDocument filter,
            String database,
            String collection,
            OpTime optime,
            long h,
            OplogVersion version,
            boolean fromMigrate,
            boolean justOne) {
        super(database, collection, optime, h, version, fromMigrate);
        this.filter = filter;
        this.justOne = justOne;
    }

    public BsonDocument getFilter() {
        return filter;
    }

    public boolean isJustOne() {
        return justOne;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.DELETE;
    }

    @Override
    public BsonDocumentBuilder toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName())
                .append(FILFER_FIELD, filter)
                .append(JUST_ONE_FIELD, justOne);
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
