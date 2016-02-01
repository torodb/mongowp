
package com.eightkdata.mongowp.server.api.deprecated.commands;

import io.netty.util.AttributeMap;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class CollStatsRequest extends CollectionCommandRequest {

    private final Number scale;

    public CollStatsRequest(
            @Nonnull String database, 
            @Nonnull AttributeMap attributes, 
            @Nonnull String collection, 
            @Nullable @Nonnegative Number scale) {
        super(database, attributes, collection);
        this.scale = scale != null ? scale : 1;
    }

    @Nonnegative
    public Number getScale() {
        return scale;
    }

}
