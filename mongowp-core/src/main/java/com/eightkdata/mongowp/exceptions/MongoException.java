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
import java.text.MessageFormat;
import javax.annotation.Nonnull;

/**
 *
 */
public class MongoException extends Exception {
    private static final long serialVersionUID = 1L;

    private final ErrorCode errorCode;

    public MongoException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public MongoException(
            @Nonnull String customMessage, 
            @Nonnull ErrorCode errorCode) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public MongoException(
            @Nonnull String customMessage, 
            @Nonnull Throwable cause, 
            @Nonnull ErrorCode errorCode) {
        super(customMessage, cause);
        this.errorCode = errorCode;
    }
    
    public MongoException(
            @Nonnull ErrorCode errorCode, 
            @Nonnull Object... args) {
        super(calculateMessage(errorCode, args));
        this.errorCode = errorCode;
    }

    public MongoException(
            @Nonnull Throwable cause, 
            @Nonnull ErrorCode errorCode, 
            @Nonnull Object... args) {
        super(calculateMessage(errorCode, args), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    private static String calculateMessage(ErrorCode errorCode, Object... args) {
        try {
            return MessageFormat.format(errorCode.getErrorMessage(), args);
        } catch (IllegalArgumentException ex) {
            return "Unknown error message";
        }
    }
}
