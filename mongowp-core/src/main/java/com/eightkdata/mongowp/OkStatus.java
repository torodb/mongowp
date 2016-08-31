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

/**
 *
 */
class OkStatus<Result> implements Status<Result> {

    static final Status<?> OK = new OkStatus<>(null);
    private static final long serialVersionUID = -2094761991032243073L;
    private final Result result;

    public OkStatus(Result result) {
        this.result = result;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.OK;
    }

    @Override
    public Result getResult() throws IllegalStateException {
        return result;
    }

    @Override
    public boolean isOK() {
        return true;
    }

    @Override
    public String getErrorMsg() throws IllegalStateException {
        throw new IllegalStateException("This status is OK, so there is no error msg");
    }

}
