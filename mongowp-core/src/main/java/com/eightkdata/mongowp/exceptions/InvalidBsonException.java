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

package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class InvalidBsonException extends MongoException {

    private static final long serialVersionUID = 1L;

    public InvalidBsonException(String reason) {
        super(reason, ErrorCode.INVALID_BSON);
    }

    public InvalidBsonException(Throwable cause) {
        super(cause, ErrorCode.INVALID_BSON);
    }

    public InvalidBsonException(String reason, Throwable cause) {
        super(reason, cause, ErrorCode.INVALID_BSON);
    }
}
