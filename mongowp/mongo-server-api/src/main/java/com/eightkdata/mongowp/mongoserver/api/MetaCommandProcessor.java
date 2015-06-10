package com.eightkdata.mongowp.mongoserver.api;

import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.mongoserver.api.commands.*;
import com.eightkdata.mongowp.mongoserver.api.pojos.InsertResponse;
import com.eightkdata.nettybson.api.BSONDocument;
import com.eightkdata.nettybson.mongodriver.MongoBSONDocument;
import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mongodb.WriteConcern;
import io.netty.util.AttributeMap;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.Nonnull;
import org.bson.BSONObject;

/**
 *
 */
public abstract class MetaCommandProcessor {
    
    private static final String NAMESPACES_COLLECTION = "system.namespaces";
    private static final String INDEXES_COLLECTION = "system.indexes";
    private static final String PROFILE_COLLECTION = "system.profile";
    private static final String JS_COLLECTION = "system.js";

    protected abstract Iterable<BSONDocument> queryNamespaces(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;

    protected abstract Iterable<BSONDocument> queryIndexes(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;

    protected abstract Iterable<BSONDocument> queryProfile(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;

    protected abstract Iterable<BSONDocument> queryJS(
            @Nonnull String database,
            @Nonnull AttributeMap attributeMap,
            @Nonnull BSONObject query
    ) throws Exception;
    
    public abstract CollStatsReply collStats(
            @Nonnull String database,
            @Nonnull CollStatsRequest request, 
            @Nonnull Supplier<Iterable<BSONDocument>> docsSupplier
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertIndex(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BSONDocument> docsToInsert,
            boolean ordered,
            @Nonnull WriteConcern wc
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertNamespace(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BSONDocument> docsToInsert,
            boolean ordered,
            @Nonnull WriteConcern wc
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertProfile(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BSONDocument> docsToInsert,
            boolean ordered,
            @Nonnull WriteConcern wc
    ) throws Exception;
    
    public abstract Future<InsertResponse> insertJS(
            @Nonnull AttributeMap attributeMap,
            @Nonnull List<BSONDocument> docsToInsert,
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
    
    private Iterable<BSONDocument> getDocuments(
            AttributeMap attributeMap,
            @Nonnull String database,
            String collection,
            BSONObject query) 
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
        BSONObject projection = request.getProjection();
        if (projection != null && !projection.keySet().isEmpty()) {
            throw new UnsupportedOperationException(
                    "Projections are not supported on meta collections queries"
            );
        }
        Iterable<BSONDocument> documents = getDocuments(
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
        return collStats(request.getDatabase(), request, new Supplier<Iterable<BSONDocument>>() {

            @Override
            public Iterable<BSONDocument> get() {
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
            BSONDocument document) throws Exception {
        assert isMetaCollection(collection);
        Object o;
        
        o = document.getValue("documents");
        List<BSONDocument> documents;
        if (o instanceof List) {
            List<BSONObject> objs = (List<BSONObject>) o;
            documents = Lists.newArrayListWithCapacity(objs.size());
            for (BSONObject obj : objs) {
                documents.add(new MongoBSONDocument(obj));
            }
        }
        else {
            documents = Collections.emptyList();
        }
        
        o = document.getValue("ordered");
        boolean ordered = o instanceof Boolean && ((Boolean) o);
        
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

	private WriteConcern getWriteConcern(BSONDocument document) {
		WriteConcern writeConcern = WriteConcern.ACKNOWLEDGED;
    	if (document.hasKey("writeConcern")) {
	    	BSONObject writeConcernObject = (BSONObject) document.getValue("writeConcern");
	    	Object w = writeConcernObject.get("w");
	        int wtimeout = 0;
	        boolean fsync = false;
	        boolean j = false;
	        boolean continueOnError = false;
	        Object jObject = writeConcernObject.get("j");
	        if (jObject !=null && jObject instanceof Boolean && 
	        		(Boolean)jObject) {
	        	fsync = true;
	        	j = true;
	        	continueOnError = true;
	        }
	        Object wtimeoutObject = writeConcernObject.get("wtimneout");
	        if (wtimeoutObject !=null && wtimeoutObject instanceof Number) {
	        	wtimeout = ((Number)wtimeoutObject).intValue();
	        }
	    	if (w != null) {
	    		if (w instanceof Number) {
	    			if (((Number) w).intValue() <= 1 && wtimeout > 0) {
	    				throw new IllegalArgumentException("wtimeout cannot be grater than 0 for w <= 1");
	    			}
	    			
	    			writeConcern = new WriteConcern(((Number) w).intValue(), wtimeout, fsync, j);
	    		} else
	       		if (w instanceof String && w.equals("majority")) {
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
