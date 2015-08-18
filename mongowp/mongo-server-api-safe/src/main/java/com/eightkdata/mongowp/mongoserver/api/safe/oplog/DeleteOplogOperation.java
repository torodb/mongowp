
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonString;

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
    public BsonDocument toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append("op", new BsonString(getType().getOplogName()))
                .append("o", filter)
                .append("b", BsonBoolean.valueOf(justOne));
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
