/*
 * MongoWP - Mongo Server: Wire Protocol Layer
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.server.exception;

import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.server.callback.WriteOpResult;
import com.google.common.base.Preconditions;

/**
 *
 */
public class ErrorOnWriteException extends MongoException {
    private static final long serialVersionUID = 1L;

    private transient final WriteOpResult writeOpResult;

    public ErrorOnWriteException(WriteOpResult writeOpResult, String customMessage) {
        super(customMessage, writeOpResult.getErrorCode());
        Preconditions.checkArgument(writeOpResult.errorOcurred(),
                "trying to create an " + getClass().getName() +" with a "
                        + "WriteOpResult that does not contain an error!");
        this.writeOpResult = writeOpResult;
    }

    public WriteOpResult getWriteOpResult() {
        return writeOpResult;
    }
}
