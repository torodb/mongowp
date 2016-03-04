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
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class UpdateMessage extends AbstractRequestMessage {

    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_UPDATE;

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Ethereal("getDataContext")
    @Nonnull private final BsonDocument selector;
    @Ethereal("getDataContext")
    @Nonnull private final BsonDocument update;
    private final boolean upsert;
    private final boolean multiUpdate;

    public UpdateMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull BsonContext dataContext,
            @Nonnull String database,
            @Nonnull String collection,
            @Nonnull @Ethereal("dataContext") BsonDocument selector,
            @Nonnull @Ethereal("dataContext") BsonDocument update,
            boolean upsert,
            boolean multiUpdate) {
        super(requestBaseMessage, dataContext);
        this.database = database;
        this.collection = collection;
        this.selector = selector;
        this.update = update;
        this.upsert = upsert;
        this.multiUpdate = multiUpdate;
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
    public BsonDocument getSelector() {
        return selector;
    }

    @Nonnull
    @Ethereal("this") 
    public BsonDocument getUpdate() {
        return update;
    }

    public boolean isUpsert() {
        return upsert;
    }

    public boolean isMultiUpdate() {
        return multiUpdate;
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        //TODO: This must be changed to preserve privacy on logs
        return "UpdateMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", selector=" + (getDataContext().isValid() ? selector : "<not available>") +
                ", update=" + (getDataContext().isValid() ? update : "<not avaiable>") +
                '}';
    }
}