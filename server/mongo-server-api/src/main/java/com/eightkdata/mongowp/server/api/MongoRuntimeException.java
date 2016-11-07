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
 * along with mongo-server-api. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.server.api;

/**
 *
 */
public class MongoRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 966582736492030L;

    public MongoRuntimeException() {
    }

    public MongoRuntimeException(String message) {
        super(message);
    }

    public MongoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MongoRuntimeException(Throwable cause) {
        super(cause);
    }

}
