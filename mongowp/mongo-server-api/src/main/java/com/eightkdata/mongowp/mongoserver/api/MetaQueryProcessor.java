package com.eightkdata.mongowp.mongoserver.api;

import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.mongoserver.api.commands.*;
import com.eightkdata.nettybson.api.BSONDocument;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import io.netty.util.AttributeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public abstract CollStatsReply collStats(
            @Nonnull CollStatsRequest request, 
            @Nonnull Supplier<Iterable<BSONDocument>> docsSupplier
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
    
    public CollStatsReply collStats(final CollStatsRequest request) throws Exception {
        return collStats(request, new Supplier<Iterable<BSONDocument>>() {

            @Override
            public Iterable<BSONDocument> get() {
                try {
                    return getDocuments(request.getAttributes(), JS_COLLECTION, null);
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    
    public static enum META_COLLECTION {
        NAMESPACES,
        INDEXES,
        PROFILE,
        JS
    }

}
