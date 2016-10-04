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

import com.google.common.base.Preconditions;

/**
 *
 */
class ErrorStatus<Result> implements Status<Result> {

    private static final long serialVersionUID = 7915632879225884360L;

    private final ErrorCode errorCode;
    private final String errorMsg;

    public ErrorStatus(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public ErrorStatus(ErrorCode errorCode, String errorMsg) {
        Preconditions.checkArgument(errorCode != ErrorCode.OK);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public boolean isOk() {
        return errorCode == ErrorCode.OK;
    }

    @Override
    public Result getResult() throws IllegalStateException {
        throw new IllegalArgumentException("This status is not OK, so there is no result");
    }

    @Override
    public String getErrorMsg() throws IllegalStateException {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "{errCode :" + errorCode + ", errorMsg:" + errorMsg + '}';
    }

}
