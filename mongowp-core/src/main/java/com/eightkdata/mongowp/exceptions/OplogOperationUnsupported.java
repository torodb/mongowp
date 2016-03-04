
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.annotations.Material;
import com.eightkdata.mongowp.bson.BsonDocument;
import javax.annotation.Nullable;

/**
 *
 */
public class OplogOperationUnsupported extends MongoException {
    private static final long serialVersionUID = 1L;
    @Material
    private final transient BsonDocument oplogOperationDoc;
    private final String docAsString;

    public OplogOperationUnsupported() {
        super(ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = null;
        docAsString = null;
    }

    public OplogOperationUnsupported(@Material @Nullable BsonDocument oplogOperationDoc) {
        super(ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = oplogOperationDoc;
        docAsString = oplogOperationDoc != null ? oplogOperationDoc.toString() : null;
    }

    public OplogOperationUnsupported(Throwable cause) {
        super(cause, ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = null;
        docAsString = null;
    }

    public OplogOperationUnsupported(@Nullable @Material BsonDocument oplogOperationDoc, Throwable cause) {
        super(cause, ErrorCode.OPLOG_OPERATION_UNSUPPORTED);
        this.oplogOperationDoc = oplogOperationDoc;
        docAsString = oplogOperationDoc != null ? oplogOperationDoc.toString() : null;
    }

    @Nullable
    @Material
    public BsonDocument getOplogOperationDoc() {
        return oplogOperationDoc;
    }

    public String getDocAsString() {
        return docAsString;
    }
}
