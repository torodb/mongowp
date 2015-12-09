
package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 */
@Immutable
public class InsertOplogOperation extends CollectionOplogOperation {

    private final BsonDocument docToInsert;

    public InsertOplogOperation(
            BsonDocument docToInsert,
            String database,
            String collection,
            OpTime optime,
            long h,
            OplogVersion version,
            boolean fromMigrate) {
        super(database, collection, optime, h, version, fromMigrate);
        this.docToInsert = docToInsert;
    }

    public BsonDocument getDocToInsert() {
        return docToInsert;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.INSERT;
    }

    @Override
    public BsonDocument toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append("op", new BsonString(getType().getOplogName()))
                .append("o", docToInsert);
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
