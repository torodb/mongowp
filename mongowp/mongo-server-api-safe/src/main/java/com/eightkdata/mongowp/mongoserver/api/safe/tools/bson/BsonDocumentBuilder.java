
package com.eightkdata.mongowp.mongoserver.api.safe.tools.bson;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;
import org.bson.*;
import org.bson.types.ObjectId;
import org.threeten.bp.Instant;

/**
 *
 */
public class BsonDocumentBuilder {

    private final BsonDocument doc;
    private boolean built;

    public BsonDocumentBuilder() {
        this.doc = new BsonDocument();
        built = false;
    }

    public boolean containsField(BsonField<Integer> field) {
        return doc.containsKey(field.getFieldName());
    }

    public BsonDocumentBuilder appendUnsafe(String fieldName, BsonValue value) {
        Preconditions.checkState(!built);
        doc.append(fieldName, value);
        return this;
    }

    public <T> BsonDocumentBuilder append(BsonField<T> field, T value, Function<T, BsonValue> translator) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), translator.apply(value));
        return this;
    }

    public BsonDocumentBuilder append(BsonField<Boolean> field, boolean value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), BsonBoolean.valueOf(value));
        return this;
    }

    public BsonDocumentBuilder appendNumber(BsonField<Number> field, Number value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), toBsonNumber(value));
        return this;
    }

    private BsonNumber toBsonNumber(Number number) {
        if (number instanceof Double || number instanceof Float) {
            return new BsonDouble(number.doubleValue());
        }
        if (number instanceof Long) {
            long longValue = number.longValue();
            if (longValue <= Integer.MAX_VALUE && longValue >= Integer.MAX_VALUE) {
                return new BsonInt32((int) longValue);
            }
            return new BsonInt64(longValue);
        }
        if (number instanceof Integer || number instanceof Byte || number instanceof Short) {
            return new BsonInt32(number.intValue());
        }
        throw new IllegalArgumentException("Numbers of class " + number.getClass() + " are not supported");
    }

    public BsonDocumentBuilder appendNumber(BsonField<Number> field, int value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonInt32(value));
        return this;
    }

    public BsonDocumentBuilder appendNumber(BsonField<Number> field, long value) {
        Preconditions.checkState(!built);
        if (value < Integer.MAX_VALUE && value > Integer.MIN_VALUE) {
            doc.append(field.getFieldName(), new BsonInt32((int) value));
        }
        else {
            doc.append(field.getFieldName(), new BsonInt64(value));
        }
        return this;
    }

    public BsonDocumentBuilder append(BsonField<Integer> field, int value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonInt32(value));
        return this;
    }

    public BsonDocumentBuilder append(BsonField<Long> field, long value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonInt64(value));
        return this;
    }

    public BsonDocumentBuilder append(BsonField<String> field, String value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonString(value));
        return this;
    }

    public BsonDocumentBuilder append(BsonField<Double> field, double value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonDouble(value));
        return this;
    }

    public BsonDocumentBuilder append(BsonField<Instant> field, Instant value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonDateTime(value.toEpochMilli()));
        return this;
    }

    /**
     *
     * @param field
     * @param value millis since Epoch
     * @return
     */
    public BsonDocumentBuilder appendInstant(BsonField<Instant> field, long value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonDateTime(value));
        return this;
    }

    public BsonDocumentBuilder append(BsonField<OpTime> field, OpTime value) {
        Preconditions.checkState(!built);
        doc.append(
                field.getFieldName(),
                value.asBsonTimestamp()
        );
        return this;
    }

    public BsonDocumentBuilder append(BsonField<BsonArray> field, BsonArray value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), value);
        return this;
    }

    public BsonDocumentBuilder append(BsonField<BsonDocument> field, BsonDocument value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), value);
        return this;
    }

    public BsonDocumentBuilder append(BsonField<BsonDocument> field, BsonDocumentBuilder value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), value.build());
        return this;
    }

    public BsonDocumentBuilder append(BsonField<HostAndPort> field, HostAndPort value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonString(value.toString()));
        return this;
    }

    public BsonDocumentBuilder append(BsonField<ObjectId> field, ObjectId value) {
        Preconditions.checkState(!built);
        doc.append(field.getFieldName(), new BsonObjectId(value));
        return this;
    }

    public BsonDocumentBuilder appendNull(BsonField<?> field) {
        doc.append(field.getFieldName(), BsonNull.VALUE);
        return this;
    }

    public BsonDocument build() {
        built = true;
        return doc;
    }

}
