
package com.eightkdata.mongowp.mongoserver.protocol.exceptions;

import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;
import javax.annotation.Nullable;
import org.bson.BsonDocument;

/**
 *
 */
public class OplogOperationUnsupported extends MongoException {
    private static final long serialVersionUID = 1L;
    private final BsonDocument oplogOperationDoc;

    public OplogOperationUnsupported() {
        super(ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = null;
    }

    public OplogOperationUnsupported(@Nullable BsonDocument oplogOperationDoc) {
        super(ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = oplogOperationDoc;
    }

    public OplogOperationUnsupported(Throwable cause) {
        super(cause, ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = null;
    }

    public OplogOperationUnsupported(@Nullable BsonDocument oplogOperationDoc, Throwable cause) {
        super(cause, ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = oplogOperationDoc;
    }

    @Nullable
    public BsonDocument getOplogOperationDoc() {
        return oplogOperationDoc;
    }
}
