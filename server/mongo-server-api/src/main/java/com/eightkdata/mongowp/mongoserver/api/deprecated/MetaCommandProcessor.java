package com.eightkdata.mongowp.mongoserver.api.deprecated;

import com.eightkdata.mongowp.mongoserver.api.deprecated.commands.QueryRequest;
import com.eightkdata.mongowp.mongoserver.api.deprecated.commands.CollStatsRequest;
import com.eightkdata.mongowp.mongoserver.api.deprecated.commands.CollStatsReply;
import com.eightkdata.mongowp.mongoserver.api.deprecated.commands.CountReply;
import com.eightkdata.mongowp.mongoserver.api.deprecated.commands.QueryReply;
import com.eightkdata.mongowp.mongoserver.api.deprecated.commands.CountRequest;
import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.mongoserver.api.deprecated.pojos.InsertResponse;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mongodb.WriteConcern;
import io.netty.util.AttributeMap;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
public abstract class MetaCommandProcessor {
    
    private static final String NAMESPACES_COLLECTION = "system.namespaces";
    private static final String INDEXES_COLLECTION = "system.indexes";
    private static final String PROFILE_COLLECTION = "system.profile";
    private static final String JS_COLLECTION = "system.js";

    protected abstract Iterable<BsonDocument> queryNamespaces(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;

    protected abstract Iterable<BsonDocument> queryIndexes(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;

    protected abstract Iterable<BsonDocument> queryProfile(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;

    protected abstract Iterable<BsonDocument> queryJS(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BsonDocument query
    ) throws Exception;
    
    public abstract CollStatsReply collStats(
            @Nonnull String database,
            @Nonnull CollStatsRequest request, 
            @Nonnull Supplier<Iterable<BsonDocument>> docsSupplier
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
    
    private Iterable<BsonDocument> getDocuments(
            AttributeMap attributeMap,
            @Nonnull String database,
            String collection,
            BsonDocument query)
            throws Exception {
        if (NAMESPACES_COLLECTION.equals(collection)) {
            return queryNamespaces(database, attributeMap, query);
        }
        else if (INDEXES_COLLECTION.equals(collection)) {
            return queryIndexes(database, attributeMap, query);
        }
        else if (PROFILE_COLLECTION.equals(collection)) {
            return queryProfile(database, attributeMap, query);
        }
        else if (JS_COLLECTION.equals(collection)) {
            return queryJS(database, attributeMap, query);
        }
        else {
            throw new IllegalArgumentException("The given query is not a meta query");
        }
    }
    
    QueryReply query(QueryRequest request) throws Exception {
        BsonDocument projection = request.getProjection();
        if (projection != null && !projection.keySet().isEmpty()) {
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
                        request.getDatabase(),
                        request.getCollection(), 
                        request.getQuery()
                )
        );
        return new CountReply(count);
    }
    
    public CollStatsReply collStats(final CollStatsRequest request) throws Exception {
        return collStats(request.getDatabase(), request, new Supplier<Iterable<BsonDocument>>() {

            @Override
            public Iterable<BsonDocument> get() {
                try {
                    return getDocuments(request.getAttributes(), request.getDatabase(), request.getCollection(), null);
                }
                catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    
    public Future<InsertResponse> insert(
            @Nonnull AttributeMap attributeMap,
            String collection, 
            BsonDocument document) throws Exception {
        assert isMetaCollection(collection);
        BsonValue o;
        
        o = document.get("documents");
        List<BsonDocument> documents;
        if (o.isArray()) {
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
        
        WriteConcern writeConcern = getWriteConcern(document);
        
        if (NAMESPACES_COLLECTION.equals(collection)) {
            return insertNamespace(attributeMap, documents, ordered, writeConcern);
        }
        else if (INDEXES_COLLECTION.equals(collection)) {
            return insertIndex(attributeMap, documents, ordered, writeConcern);
        }
        else if (PROFILE_COLLECTION.equals(collection)) {
            return insertProfile(attributeMap, documents, ordered, writeConcern);
        }
        else if (JS_COLLECTION.equals(collection)) {
            return insertJS(attributeMap, documents, ordered, writeConcern);
        }
        else {
            throw new IllegalArgumentException("The given query is not a meta query");
        }
    }

	private WriteConcern getWriteConcern(BsonDocument document) {
		WriteConcern writeConcern = WriteConcern.ACKNOWLEDGED;
    	if (document.containsKey("writeConcern")) {
	    	BsonDocument writeConcernObject = document.get("writeConcern").asDocument();
	    	BsonValue w = writeConcernObject.get("w");
	        int wtimeout = 0;
	        boolean fsync = false;
	        boolean j = false;
	        BsonValue jObject = writeConcernObject.get("j");
	        if (jObject !=null && jObject.isBoolean() && jObject.asBoolean().getValue()) {
	        	fsync = true;
	        	j = true;
	        }
	        BsonValue wtimeoutObject = writeConcernObject.get("wtimneout");
	        if (wtimeoutObject !=null && wtimeoutObject.isNumber()) {
	        	wtimeout = wtimeoutObject.asNumber().intValue();
	        }
	    	if (w != null) {
	    		if (w.isNumber()) {
                    if (w.asNumber().intValue() <= 1 && wtimeout > 0) {
	    				throw new IllegalArgumentException("wtimeout cannot be grater than 0 for w <= 1");
	    			}
	    			
	    			writeConcern = new WriteConcern(w.asNumber().intValue(), wtimeout, fsync, j);
	    		} else
	       		if (w.isString() && w.asString().getValue().equals("majority")) {
	       			if (wtimeout > 0) {
	       				throw new IllegalArgumentException("wtimeout cannot be grater than 0 for w <= 1");
	       			}
	       			
	       			writeConcern = new WriteConcern.Majority(wtimeout, fsync, j);
	    		} else {
    				throw new IllegalArgumentException("w:" + w + " is not supported");
	    		}
	    	}
    	}
		return writeConcern;
	}
    
    public static enum META_COLLECTION {
        NAMESPACES,
        INDEXES,
        PROFILE,
        JS
    }

}
