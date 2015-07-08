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
package com.eightkdata.mongowp.mongoserver.protocol.exceptions;

import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import java.text.MessageFormat;
import javax.annotation.Nonnull;

/**
 *
 */
public class MongoServerException extends Exception {
    private static final long serialVersionUID = 1L;

    private final MongoWP.ErrorCode errorCode;

    public MongoServerException(
            @Nonnull String customMessage, 
            @Nonnull MongoWP.ErrorCode errorCode) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public MongoServerException(
            @Nonnull String customMessage, 
            @Nonnull Throwable cause, 
            @Nonnull MongoWP.ErrorCode errorCode) {
        super(customMessage, cause);
        this.errorCode = errorCode;
    }
    
    public MongoServerException(
            @Nonnull MongoWP.ErrorCode errorCode, 
            @Nonnull Object... args) {
        super(MessageFormat.format(errorCode.getErrorMessage(), args));
        this.errorCode = errorCode;
    }

    public MongoServerException(
            @Nonnull Throwable cause, 
            @Nonnull MongoWP.ErrorCode errorCode, 
            @Nonnull Object... args) {
        super(MessageFormat.format(errorCode.getErrorMessage(), args), cause);
        this.errorCode = errorCode;
    }

    public MongoWP.ErrorCode getErrorCode() {
        return errorCode;
    }
}
