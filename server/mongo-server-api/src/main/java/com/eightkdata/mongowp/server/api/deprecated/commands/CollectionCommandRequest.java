
package com.eightkdata.mongowp.server.api.deprecated.commands;

import com.google.common.base.Preconditions;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;

/**
 *
 */
public class CollectionCommandRequest extends CommandRequest {

    private final String collection;

    public CollectionCommandRequest(
            @Nonnull String database,
            @Nonnull AttributeMap attributes, 
            @Nonnull String collection) {
        super(database, attributes);
        Preconditions.checkArgument(collection != null);
        this.collection = collection;
    }

    @Nonnull
    public String getCollection() {
        return collection;
    }
}
