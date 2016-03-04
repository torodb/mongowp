/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.bson.BsonType;
import java.util.Locale;

/**
 *
 */
public class TypesMismatchException extends MongoException {
    private static final long serialVersionUID = 1L;

    private final String fieldId;
    private final String expectedType;
    private final BsonType foundType;

    public TypesMismatchException(String fieldId, BsonType expectedType, BsonType foundType) {
        super(ErrorCode.TYPE_MISMATCH, fieldId, expectedType, foundType);
        this.fieldId = fieldId;
        this.expectedType = expectedType.toString().toLowerCase(Locale.ROOT);
        this.foundType = foundType;
    }

    public TypesMismatchException(String fieldId, BsonType expectedType, BsonType foundType, String customMessage) {
        super(customMessage, ErrorCode.TYPE_MISMATCH);
        this.fieldId = fieldId;
        this.expectedType = expectedType.toString().toLowerCase(Locale.ROOT);
        this.foundType = foundType;
    }

    public TypesMismatchException(String fieldId, String expectedType, BsonType foundType) {
        super(ErrorCode.TYPE_MISMATCH, fieldId, expectedType, foundType);
        this.fieldId = fieldId;
        this.expectedType = expectedType;
        this.foundType = foundType;
    }
    
    public TypesMismatchException(String fieldId, String expectedType, BsonType foundType, String customMessage) {
        super(customMessage, ErrorCode.TYPE_MISMATCH);
        this.fieldId = fieldId;
        this.expectedType = expectedType;
        this.foundType = foundType;
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getExpectedType() {
        return expectedType;
    }

    public BsonType getFoundType() {
        return foundType;
    }
    
    public TypesMismatchException newWithMessage(String customeMessage) {
        return new TypesMismatchException(getFieldId(), getExpectedType(), getFoundType(), customeMessage);
    }
}
