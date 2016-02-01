
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.IntField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class WriteError {
    private static final IntField INDEX_FIELD = new IntField("index");
    private static final IntField CODE_FIELD = new IntField("code");
    private static final StringField ERR_MSG_FIELD = new StringField("errmsg");

    private final int index;
    private final int code;
    private final String errmsg;

    public WriteError(int index, int code, String errmsg) {
        this.index = index;
        this.code = code;
        this.errmsg = errmsg;
    }

    public int getIndex() {
        return index;
    }

    public int getCode() {
        return code;
    }

    public String getErrmsg() {
        return errmsg;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.index;
        hash = 19 * hash + this.code;
        hash = 19 * hash + (this.errmsg != null ? this.errmsg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WriteError other = (WriteError) obj;
        if (this.index != other.index) {
            return false;
        }
        if (this.code != other.code) {
            return false;
        }
        return !((this.errmsg == null) ? (other.errmsg != null)
                : !this.errmsg.equals(other.errmsg));
    }

    public BsonValue<?> marshall() {
        BsonDocumentBuilder bsonWriteError = new BsonDocumentBuilder();
        bsonWriteError.append(INDEX_FIELD, index);
        bsonWriteError.append(CODE_FIELD, code);
        bsonWriteError.append(ERR_MSG_FIELD, errmsg);
        return bsonWriteError.build();
    }

    public static WriteError unmarshall(BsonDocument doc) throws TypesMismatchException, NoSuchKeyException {
        return new WriteError(
                BsonReaderTool.getNumeric(doc, INDEX_FIELD).intValue(),
                BsonReaderTool.getNumeric(doc, CODE_FIELD).intValue(),
                BsonReaderTool.getString(doc, ERR_MSG_FIELD, null)
        );
    }

}
