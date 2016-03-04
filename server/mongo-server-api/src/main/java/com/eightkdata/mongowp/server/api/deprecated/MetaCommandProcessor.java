package com.eightkdata.mongowp.server.api.deprecated;

import com.eightkdata.mongowp.WriteConcern;
import com.eightkdata.mongowp.annotations.Material;
import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.messages.request.EmptyBsonContext;
import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.server.api.deprecated.commands.QueryReply;
import com.eightkdata.mongowp.server.api.deprecated.commands.QueryRequest;
import com.eightkdata.mongowp.server.api.deprecated.pojos.InsertResponse;
import com.google.common.collect.Lists;
import io.netty.util.AttributeMap;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;

/**
 *
 */
public abstract class MetaCommandProcessor {
    
    private static final String NAMESPACES_COLLECTION = "system.namespaces";
    private static final String INDEXES_COLLECTION = "system.indexes";
    private static final String PROFILE_COLLECTION = "system.profile";
    private static final String JS_COLLECTION = "system.js";

    @Material
    protected abstract Iterable<BsonDocument> queryNamespaces(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;

    @Material
    protected abstract Iterable<BsonDocument> queryIndexes(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;

    @Material
    protected abstract Iterable<BsonDocument> queryProfile(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;

    @Material
    protected abstract Iterable<BsonDocument> queryJS(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertIndex(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BsonDocument> docsToInsert,
            boolean ordered,
            @Nonnull WriteConcern wc
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertNamespace(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BsonDocument> docsToInsert,
            boolean ordered,
            @Nonnull WriteConcern wc
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertProfile(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BsonDocument> docsToInsert,
            boolean ordered,
            @Nonnull WriteConcern wc
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertJS(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BsonDocument> docsToInsert,
            boolean ordered,
            @Nonnull WriteConcern wc
    ) throws Exception;

    public boolean isMetaCollection(@Nonnull String collection) {
        return NAMESPACES_COLLECTION.equals(collection)
                || INDEXES_COLLECTION.equals(collection)
                || PROFILE_COLLECTION.equals(collection)
                || JS_COLLECTION.equals(collection);
    }
    
    public boolean isMetaQuery(@Nonnull QueryMessage queryMessage) {
        final String collection = queryMessage.getCollection();
        return isMetaCollection(collection);
    }

    @Material
    private Iterable<BsonDocument> getDocuments(
            AttributeMap attributeMap,
            @Nonnull String database,
            @Nonnull String collection,
            BsonDocument query)
            throws Exception {
        switch (collection) {
            case NAMESPACES_COLLECTION:
                return queryNamespaces(database, attributeMap, query);
            case INDEXES_COLLECTION:
                return queryIndexes(database, attributeMap, query);
            case PROFILE_COLLECTION:
                return queryProfile(database, attributeMap, query);
            case JS_COLLECTION:
                return queryJS(database, attributeMap, query);
            default:
                throw new IllegalArgumentException("The given query is not a meta query");
        }
    }
    
    QueryReply query(QueryRequest request) throws Exception {
        BsonDocument projection = request.getProjection();
        if (projection != null && !projection.isEmpty()) {
            throw new UnsupportedOperationException(
                    "Projections are not supported on meta collections queries"
            );
        }
        Iterable<BsonDocument> documents = getDocuments(
                request.getAttributes(),
                request.getDatabase(),
                request.getCollection(),
                request.getQuery()
        );
        return new QueryReply.Builder(EmptyBsonContext.getInstance())
                .setCursorId(0)
                .setDocuments(documents)
                .setStartingFrom(0)
                .build();
    }

    public Future<InsertResponse> insert(
            @Nonnull AttributeMap attributeMap,
            @Nonnull String collection,
            BsonDocument document) throws Exception {
        assert isMetaCollection(collection);
        BsonValue o;
        
        o = document.get("documents");
        List<BsonDocument> documents;
        if (o != null && o.isArray()) {
            BsonArray uncastedDocs = o.asArray();
            documents = Lists.newArrayListWithCapacity(uncastedDocs.size());
            for (BsonValue bsonValue : uncastedDocs) {
                documents.add(bsonValue.asDocument());
            }
        }
        else {
            documents = Collections.emptyList();
        }
        
        o = document.get("ordered");
        boolean ordered = o != null && o.isBoolean() && o.asBoolean().getValue();
        
        WriteConcern writeConcern = WriteConcern.fromDocument(document);
        
        switch (collection) {
            case NAMESPACES_COLLECTION:
                return insertNamespace(attributeMap, documents, ordered, writeConcern);
            case INDEXES_COLLECTION:
                return insertIndex(attributeMap, documents, ordered, writeConcern);
            case PROFILE_COLLECTION:
                return insertProfile(attributeMap, documents, ordered, writeConcern);
            case JS_COLLECTION:
                return insertJS(attributeMap, documents, ordered, writeConcern);
            default:
                throw new IllegalArgumentException("The given query is not a meta query");
        }
    }

    public static enum META_COLLECTION {
        NAMESPACES,
        INDEXES,
        PROFILE,
        JS
    }

}
