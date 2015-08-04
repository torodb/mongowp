
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

/**
 *
 */
@Immutable
public class UpdateOplogOperation extends CollectionOplogOperation {

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
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }
    
}
