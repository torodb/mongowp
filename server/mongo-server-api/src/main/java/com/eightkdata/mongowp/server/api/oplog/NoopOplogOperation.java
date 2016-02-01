
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import javax.annotation.Nullable;

/**
 *
 */
public class NoopOplogOperation extends OplogOperation {

    private static final StringField NS_FIELD = new StringField("ns");
    private static final DocField MSG_FIELD = new DocField("o");
    private final @Nullable BsonDocument msg;

    public NoopOplogOperation(@Nullable BsonDocument msg, String database, OpTime optime, long h, OplogVersion version, boolean fromMigrate) {
        super(database, optime, h, version, fromMigrate);
        this.msg = msg;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.NOOP;
    }

    @Override
    public BsonDocumentBuilder toDescriptiveBson() {
        BsonDocumentBuilder doc = super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName())
                .append(NS_FIELD, "");
        if (msg != null) {
            doc.append(MSG_FIELD, msg);
        }
        return doc;
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
