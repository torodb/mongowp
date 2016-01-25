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

import javax.annotation.Nonnull;
import org.bson.BsonDocument;

/**
 *
 */
public class DeleteMessage extends AbstractRequestMessage implements RequestMessage {
    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_DELETE;

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnull private final BsonDocument document;
    private final boolean singleRemove;

    public DeleteMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull String database,
            @Nonnull String collection,
            @Nonnull BsonDocument document,
            boolean singleRemove) {
        super(requestBaseMessage);
        this.database = database;
        this.collection = collection;
        this.document = document;
        this.singleRemove = singleRemove;
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

    @Nonnull
    public BsonDocument getDocument() {
        return document;
    }

    public boolean isSingleRemove() {
        return singleRemove;
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return "DeleteMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", document=" + document +
                '}';
    }
}