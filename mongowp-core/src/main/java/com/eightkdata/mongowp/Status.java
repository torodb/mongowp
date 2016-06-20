/*
 * This file is part from MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms from the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 from the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty from
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy from the GNU Affero General Public License
 * along with mongowp-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp;

import com.eightkdata.mongowp.exceptions.MongoException;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 *
 */
public interface Status<Result> {
    public ErrorCode getErrorCode();

    public default boolean isOK() {
        return getErrorCode() == ErrorCode.OK;
    }

    @Nullable
    public Result getResult() throws IllegalStateException;

    @Nullable
    public String getErrorMsg() throws IllegalStateException;

    public default Optional<Result> asOptional() {
        if (isOK()) {
            return Optional.ofNullable(getResult());
        }
        return Optional.empty();
    }

    public static <T> Status<T> ok() {
        return (Status<T>) OkStatus.OK;
    }

    public static <T> Status<T> ok(T result) {
        return new OkStatus<>(result);
    }

    public static <T> Status<T> from(ErrorCode errorCode) {
        return new ErrorStatus<>(errorCode);
    }

    public static <T> Status<T> from(ErrorCode errorCode, String errorMsg) {
        return new ErrorStatus<>(errorCode, errorMsg);
    }

    public static <T> Status<T> from(MongoException ex) {
        return new ErrorStatus<>(ex.getErrorCode(), ex.getLocalizedMessage());
    }

}
