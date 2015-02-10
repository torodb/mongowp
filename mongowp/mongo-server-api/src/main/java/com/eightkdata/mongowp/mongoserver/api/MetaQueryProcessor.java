package com.eightkdata.mongowp.mongoserver.api;

import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.mongoserver.api.callback.MessageReplier;
import com.eightkdata.mongowp.mongoserver.api.commands.CountReply;
import com.eightkdata.mongowp.mongoserver.api.commands.CountRequest;
import com.eightkdata.mongowp.mongoserver.api.commands.QueryReply;
import com.eightkdata.mongowp.mongoserver.api.commands.QueryRequest;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.nettybson.api.BSONDocument;
import com.google.common.collect.Iterables;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;
import org.bson.BSONObject;

/**
 *
 */
public abstract class MetaQueryProcessor {

    private static final String NAMESPACES_COLLECTION = "system.namespaces";
    private static final String INDEXES_COLLECTION = "system.indexes";
    private static final String PROFILE_COLLECTION = "system.profile";
    private static final String JS_COLLECTION = "system.js";

    protected abstract Iterable<BSONDocument> queryNamespaces(
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;

    protected abstract Iterable<BSONDocument> queryIndexes(
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;

    protected abstract Iterable<BSONDocument> queryProfile(
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;

    protected abstract Iterable<BSONDocument> queryJS(
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;

    public boolean isMetaCollection(@Nonnull String collection) {
        return NAMESPACES_COLLECTION.equals(collection)
                || INDEXES_COLLECTION.equals(collection)
                || PROFILE_COLLECTION.equals(collection)
                || JS_COLLECTION.equals(collection);
    }
    
    public boolean isMetaQuery(@Nonnull QueryMessage queryMessage) {
        final String collection = queryMessage.getCollection();
        return NAMESPACES_COLLECTION.equals(collection)
                || INDEXES_COLLECTION.equals(collection)
                || PROFILE_COLLECTION.equals(collection)
                || JS_COLLECTION.equals(collection);
    }
    
    private Iterable<BSONDocument> getDocuments(
            AttributeMap attributeMap,
            String collection,
            BSONObject query) 
            throws Exception {
        if (NAMESPACES_COLLECTION.equals(collection)) {
            return queryNamespaces(attributeMap, query);
        }
        else if (INDEXES_COLLECTION.equals(collection)) {
            return queryIndexes(attributeMap, query);
        }
        else if (PROFILE_COLLECTION.equals(collection)) {
            return queryProfile(attributeMap, query);
        }
        else if (JS_COLLECTION.equals(collection)) {
            return queryJS(attributeMap, query);
        }
        else {
            throw new IllegalArgumentException("The given query is not a meta query");
        }
    }
    
    QueryReply query(QueryRequest request) throws Exception {
        BSONObject projection = request.getProjection();
        if (projection != null && !projection.keySet().isEmpty()) {
            throw new UnsupportedOperationException(
                    "Projections are not supported on meta collections queries"
            );
        }
        Iterable<BSONDocument> documents = getDocuments(
                request.getAttributes(),
                request.getCollection(),
                request.getQuery()
        );
        return new QueryReply.Builder()
                .setCursorId(0)
                .setDocuments(documents)
                .setStartingFrom(0)
                .build();
    }

    CountReply count(CountRequest request) throws Exception {
        int count = Iterables.size(
                getDocuments(
                        request.getAttributes(),
                        request.getCollection(), 
                        request.getQuery()
                )
        );
        return new CountReply(count);
    }
    
    public static enum META_COLLECTION {
        NAMESPACES,
        INDEXES,
        PROFILE,
        JS
    }

}
