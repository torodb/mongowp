
package com.eightkdata.mongowp.mongoserver.api.commands;

import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;

/**
 *
 */
public class CollectionCommandRequest extends CommandRequest {

    private final String collection;

    public CollectionCommandRequest(
            @Nonnull AttributeMap attributes, 
            @Nonnull String collection) {
        super(attributes);
        this.collection = collection;
    }

    @Nonnull
    public String getCollection() {
        return collection;
    }
}
