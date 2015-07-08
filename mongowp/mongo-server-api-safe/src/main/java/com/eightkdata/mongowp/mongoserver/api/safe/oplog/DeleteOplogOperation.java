
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.api.safe.pojos.OpTime;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

/**
 *
 */
@Immutable
public class DeleteOplogOperation extends CollectionOplogOperation {

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
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
