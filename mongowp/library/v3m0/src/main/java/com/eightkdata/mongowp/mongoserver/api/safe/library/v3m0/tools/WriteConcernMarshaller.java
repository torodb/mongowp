
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.FailedToParseException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.mongodb.WriteConcern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;

/**
 *
 */
public class WriteConcernMarshaller {
    private static final BsonField<Boolean> J_FIELD = BsonField.create("j");
    private static final BsonField<Boolean> FSYNC_FIELD = BsonField.create("fsync");
    private static final BsonField<Integer> W_TIMEOUT_FIELD = BsonField.create("wtimeout");

    @Nonnull
    public static WriteConcern unmarshall(@Nullable BsonDocument doc, @Nonnull WriteConcern defaultWriteConcern) throws TypesMismatchException, FailedToParseException {
        if (doc == null) {
            return defaultWriteConcern;
        }
        return unmarshall(doc);
    }

    @Nonnull
    public static WriteConcern unmarshall(@Nonnull BsonDocument doc) throws TypesMismatchException, FailedToParseException {
        try {

            if (doc.isEmpty()) {
                throw new FailedToParseException("write concern object cannot be empty");
            }
            boolean j;
            try {
                j = BsonReaderTool.getBooleanOrNumeric(doc, J_FIELD, false);
            } catch (TypesMismatchException ex) {
                throw new FailedToParseException("j must be numeric or boolean");
            }
            boolean fsync;
            try {
                fsync = BsonReaderTool.getBooleanOrNumeric(doc, FSYNC_FIELD, false);
            } catch (TypesMismatchException ex) {
                throw new FailedToParseException("fsyn must be numeric or boolean");
            }

            if (fsync && j) {
                throw new FailedToParseException("fsync or j options cannot be used together");
            }

            int wTimeout = BsonReaderTool.getInteger(doc, W_TIMEOUT_FIELD);

            BsonValue uncastedW = doc.get("w");
            if (uncastedW == null) {
                int w = 1;
                return new WriteConcern(w, wTimeout, fsync, j);
            }
            else if (uncastedW.isNumber()) {
                int w = uncastedW.asNumber().intValue();
                return new WriteConcern(w, wTimeout, fsync, j);
            }
            else if (uncastedW.isString()) {
                return new WriteConcern(uncastedW.asString().getValue(), wTimeout, fsync, j);
            }
            else {
                throw new FailedToParseException("w has to be a number or a string");
            }
        } catch (NoSuchKeyException ex) {
            return new WriteConcern(0, 0, false, false);
        }
    }

    @Nonnull
    public static BsonDocument marshall(WriteConcern wc) {
        BsonDocumentBuilder builder = new BsonDocumentBuilder();
        builder.append(J_FIELD, wc.getJ());
        builder.append(FSYNC_FIELD, wc.getFsync());

        if (wc.getWtimeout() != 0) {
            builder.append(W_TIMEOUT_FIELD, wc.getWtimeout());
        }
        if (wc.getWObject() instanceof Integer) {
            builder.appendUnsafe("w", new BsonInt32(wc.getW()));
        }
        else {
            assert wc.getWObject() instanceof String;
            builder.appendUnsafe("w", new BsonString(wc.getWString()));
        }
        return builder.build();
    }


}
