
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.mongoserver.api.safe.oplog.*;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import java.util.Locale;
import javax.annotation.Nonnull;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
public class OplogOperationParser {

    public static OplogOperation fromBson(@Nonnull BsonValue uncastedOp) throws BadValueException, TypesMismatchException, NoSuchKeyException {
        if (!uncastedOp.isDocument()) {
            throw new BadValueException("found a "
                    + uncastedOp.getBsonType().toString().toLowerCase(Locale.ROOT)
                    + " where a document that represents a oplog operation "
                    + "was expected");
        }
        BsonDocument doc = uncastedOp.asDocument();

        OplogOperationType opType;
        String opString = BsonReaderTool.getString(doc, "op");
        try {
            opType = OplogOperationType.fromOplogName(opString);
        }
        catch (IllegalArgumentException ex) {
            throw new BadValueException("Unknown oplog operation with type '" + opString + "'");
        }

        String ns;
        try {
            ns = BsonReaderTool.getString(doc, "ns");
        }
        catch (NoSuchKeyException ex) {
            throw new NoSuchKeyException("ns", "op does not contain required \"ns\" field: " + uncastedOp);
        }
        catch (TypesMismatchException ex) {
            throw ex.newWithMessage("\"ns\" field is not a string: " + uncastedOp);
        }

        if (ns.isEmpty() && !opType.equals(OplogOperationType.NOOP)) {
            throw new BadValueException("\"ns\" field value cannot be empty "
                    + "when op type is not 'n': " + doc);
        }
        String db;
        String collection;
        int firstDotIndex = ns.indexOf('.');
        if (firstDotIndex == -1 || firstDotIndex + 1 == ns.length()) {
            db = ns;
            collection = null;
        }
        else {
            db = ns.substring(0, firstDotIndex);
            collection = ns.substring(firstDotIndex + 1);
        }
        
        OpTime optime = new OpTime(BsonReaderTool.getInstant(doc, "ts"));
        long h = BsonReaderTool.getLong(doc, "h");
        OplogVersion version = OplogVersion.valueOf(BsonReaderTool.getString(doc, "v"));
        boolean fromMigrate = doc.containsKey("fromMigrate"); //Note: Mongodb v3 checks if the key exists or not, but doesn't check the value
        BsonDocument o = BsonReaderTool.getDocument(doc, "o");

        switch (opType) {
            case DB:
                return new DbOplogOperation(
                        db,
                        optime,
                        h,
                        version,
                        fromMigrate
                );
            case DB_CMD:
                return new DbCmdOplogOperation(
                        o,
                        db,
                        optime,
                        h,
                        version,
                        fromMigrate
                );
            case DELETE:
                return new DeleteOplogOperation(
                        o,
                        db,
                        collection,
                        optime,
                        h,
                        version,
                        fromMigrate,
                        BsonReaderTool.getBoolean(doc, "b", false)
                );
            case INSERT:
                return new InsertOplogOperation(
                        o,
                        db,
                        collection,
                        optime,
                        h,
                        version,
                        fromMigrate
                );
            case NOOP:
                return new NoopOplogOperation(db, optime, h, version, fromMigrate);
            case UPDATE:
                return new UpdateOplogOperation(
                        o,
                        db,
                        collection,
                        optime,
                        h,
                        version,
                        fromMigrate,
                        BsonReaderTool.getDocument(doc, "o2"),
                        BsonReaderTool.getBoolean(doc, "b", false)
                );
            default:
                throw new AssertionError(OplogOperationParser.class + " is not prepared to work with oplog operations of type " + opType);
        }
    }
}
