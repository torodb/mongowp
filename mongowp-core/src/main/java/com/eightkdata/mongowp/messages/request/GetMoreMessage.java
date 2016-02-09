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


package com.eightkdata.mongowp.messages.request;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 */
public class GetMoreMessage extends AbstractRequestMessage {

    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_GET_MORE;

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnegative private final int numberToReturn;
    @Nonnegative private final long cursorId;

    public GetMoreMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull String database,
            @Nonnull String collection,
            int numberToReturn,
            long cursorId
    ) {
    	super(requestBaseMessage, EmptyBsonContext.getInstance());
        this.database = database;
        this.collection = collection;
        this.numberToReturn = numberToReturn;
        this.cursorId = cursorId;
    }

    @Override
    public RequestOpCode getOpCode() {
        return REQUEST_OP_CODE;
    }

    @Nonnull
    public String getDatabase() {
        return database;
    }

    @Nonnull
    public String getCollection() {
        return collection;
    }

    public int getNumberToReturn() {
        return numberToReturn;
    }

    public long getCursorId() {
        return cursorId;
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return "GetMoreMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", numberToReturn=" + numberToReturn +
                ", cursorId=" + cursorId +
                '}';
    }

}