
package com.eightkdata.mongowp.mongoserver.api.tools.bson;

/**
 *
 */
public class BsonField<O> {
    private final String fieldName;

    public BsonField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static <O1> BsonField<O1> create(String fieldName) {
        return new BsonField<O1>(fieldName);
    }

    public final String getFieldName() {
        return fieldName;
    }
}
