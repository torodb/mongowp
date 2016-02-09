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

import com.eightkdata.mongowp.annotations.Ethereal;
import com.eightkdata.mongowp.bson.BsonDocument;
import javax.annotation.Nonnull;

/**
 *
 */
public class DeleteMessage extends AbstractRequestMessage {
    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_DELETE;

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnull private final BsonDocument document;
    private final boolean singleRemove;

    public DeleteMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull BsonContext dataContext,
            @Nonnull String database,
            @Nonnull String collection,
            @Nonnull @Ethereal("dataContext") BsonDocument document,
            boolean singleRemove) {
        super(requestBaseMessage, dataContext);
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
    @Ethereal("this")
    public BsonDocument getDocument() {
        return document;
    }

    public boolean isSingleRemove() {
        return singleRemove;
    }

    @Override
    public String toString() {
        //TODO: This must be changed to preserve privacy on logs
        return "DeleteMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", document=" + (getDataContext().isValid() ? document : "<not available>") +
                '}';
    }
}