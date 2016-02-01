
package com.eightkdata.mongowp.fields;

import com.eightkdata.mongowp.bson.BsonValue;

/**
 *
 * @param <JV>
 * @param <V>
 */
public class BsonField<JV, V extends BsonValue<JV>> {
    private final String fieldName;
    
    BsonField(String fieldName) {
        this.fieldName = fieldName;
    }

    public final String getFieldName() {
        return fieldName;
    }
}
