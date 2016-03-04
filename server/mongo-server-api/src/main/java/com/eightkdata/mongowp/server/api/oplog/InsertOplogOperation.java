
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class InsertOplogOperation extends CollectionOplogOperation {
    private static final DocField DOC_TO_INSERT_FIELD = new DocField("o");

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
    public BsonDocumentBuilder toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName())
                .append(DOC_TO_INSERT_FIELD, docToInsert);
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
