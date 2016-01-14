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

import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import com.eightkdata.mongowp.messages.utils.SimpleIterableDocumentsProvider;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

/**
 *
 */
@Immutable
public class InsertMessage extends AbstractRequestMessage implements RequestMessage {
    
    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_INSERT;

    @Nonnull private final String database;
    @Nonnull private final String collection;
    @Nonnull private final IterableDocumentProvider<?> documents;

    public InsertMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull String database,
            @Nonnull String collection,
            boolean continueOnError,
            @Nonnull IterableDocumentProvider<?> documents
    ) {
        super(requestBaseMessage);
        this.database = database;
        this.collection = collection;
        this.documents = documents;
    }

    public InsertMessage(
            @Nonnull RequestBaseMessage requestBaseMessage,
            @Nonnull String database,
            @Nonnull String collection,
            boolean continueOnError,
            @Nonnull Iterable<? extends BsonDocument> documents
    ) {
        this(requestBaseMessage, database, collection, continueOnError, new SimpleIterableDocumentsProvider<>(documents));
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
    public FluentIterable<? extends BsonDocument> getDocuments() {
        return documents;
    }

    @Override
    public void close() throws Exception {
        documents.close();
    }

    @Override
    public String toString() {
        int docsLimit = 10;
        return "InsertMessage{" + super.toString() +
                ", database='" + database + '\'' +
                ", collection='" + collection + '\'' +
                ", documents (limited to " + docsLimit + ")="
                + Iterables.toString(documents.limit(docsLimit)) +
                '}';
    }
}