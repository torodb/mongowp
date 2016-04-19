/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mongowp-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp;

import com.eightkdata.mongowp.exceptions.MongoException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class Status<E> {
    private static final Status<?> OK = new Status<>(null);

    private final @Nonnull ErrorCode errorCode;
    private final @Nullable String reason;
    private final @Nullable E value;
    private final @Nullable MongoException exception;

    public Status(E value) {
        this(value, ErrorCode.OK, null, null);
    }

    public Status(ErrorCode errorCode, String errorMsg) {
        this(null, errorCode, errorMsg, null);
    }

    public Status(ErrorCode errorCode, String errorMsg, @Nullable MongoException exception) {
        this(null, errorCode, errorMsg, exception);
    }

    private Status(@Nullable E value, @Nonnull ErrorCode errorCode, @Nullable String reason,
            @Nullable MongoException exception) {
        assert value != null || reason != null;
        this.value = value;
        this.errorCode = errorCode;
        this.reason = reason;
        this.exception = exception;
    }

    public static Status<?> ok() {
        return OK;
    }

    public boolean isOk() {
        return errorCode != ErrorCode.OK;
    }

    public ErrorCode getCode() {
        return errorCode;
    }

    public E getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }

    public MongoException getException() {
        return exception;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(errorCode.toString());
        if (!isOk()) {
            sb.append(' ').append(getReason());
        }
        return sb.toString();
    }

}
