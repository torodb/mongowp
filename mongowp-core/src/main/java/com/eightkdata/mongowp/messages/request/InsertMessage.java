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
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import com.google.common.collect.Iterables;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class InsertMessage extends AbstractRequestMessage {
    
    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_INSERT;

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnull @Ethereal("this") private final IterableDocumentProvider<?> documents;

    public InsertMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull BsonContext dataContext,
            @Nonnull String database,
            @Nonnull String collection,
            boolean continueOnError,
            @Nonnull @Ethereal("dataContext") IterableDocumentProvider<?> documents
    ) {
        super(requestBaseMessage, dataContext);
        this.database = database;
        this.collection = collection;
        this.documents = documents;
    }

    public InsertMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull BsonContext context,
            @Nonnull String database,
            @Nonnull String collection,
            boolean continueOnError,
            @Nonnull Iterable<? extends BsonDocument> documents
    ) {
        this(requestBaseMessage, context, database, collection, continueOnError, IterableDocumentProvider.of(documents));
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

    public IterableDocumentProvider<?> getDocuments() {
        return documents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("InsertMessage{")
                .append(super.toString())
                .append(", database='")
                .append(database)
                .append("' , collection='")
                .append(collection)
                .append('\'');


        if (getDataContext().isValid()) {
            //TODO: This must be changed to preserve privacy on logs
            int docsLimit = 10;
            sb.append(", documents (limited to ").append(docsLimit).append(")=")
                    .append(
                            Iterables.toString(
                                    documents.getIterable(AllocationType.HEAP).limit(docsLimit)
                            )
                    );
        }
        else {
            sb.append(", documents=<not available>");
        }
        return sb.append('}').toString();
    }
}