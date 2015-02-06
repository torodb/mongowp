
package com.eightkdata.mongowp.mongoserver.api;

import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.mongoserver.api.callback.MessageReplier;
import javax.annotation.Nonnull;

/**
 *
 */
public abstract class MetaQueryProcessor {

    private static final String NAMESPACES_COLLECTION = "system.namespaces";
    private static final String INDEXES_COLLECTION = "system.indexes";
    private static final String PROFILE_COLLECTION = "system.profile";
    private static final String JS_COLLECTION = "system.js";
    
    protected abstract void queryNamespaces(
            @Nonnull QueryMessage queryMessage, 
            @Nonnull MessageReplier messageReplier
    ) throws Exception;
    
    protected abstract void queryIndexes(
            @Nonnull QueryMessage queryMessage, 
            @Nonnull MessageReplier messageReplier
    ) throws Exception;
    
    protected abstract void queryProfile(
            @Nonnull QueryMessage queryMessage, 
            @Nonnull MessageReplier messageReplier
    ) throws Exception;
    
    protected abstract void queryJS(
            @Nonnull QueryMessage queryMessage, 
            @Nonnull MessageReplier messageReplier
    ) throws Exception;
    
    public boolean isMetaQuery(@Nonnull QueryMessage queryMessage) {
        final String collection = queryMessage.getCollection();
        return NAMESPACES_COLLECTION.equals(collection)
                || INDEXES_COLLECTION.equals(collection)
                || PROFILE_COLLECTION.equals(collection)
                || JS_COLLECTION.equals(collection);
    }

    void queryMetaInf(QueryMessage queryMessage, MessageReplier messageReplier) throws Exception {
        final String collection = queryMessage.getCollection();
        if (NAMESPACES_COLLECTION.equals(collection)) {
            queryNamespaces(queryMessage, messageReplier);
        }
        else if (INDEXES_COLLECTION.equals(collection)) {
            queryIndexes(queryMessage, messageReplier);
        }
        else if (PROFILE_COLLECTION.equals(collection)) {
            queryProfile(queryMessage, messageReplier);
        }
        else if (JS_COLLECTION.equals(collection)) {
            queryJS(queryMessage, messageReplier);
        }
        else {
            throw new IllegalArgumentException("The given query is not a meta query");
        }
    }
    
}
