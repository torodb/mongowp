
package com.eightkdata.mongowp.server.api.deprecated.commands;

import com.google.common.base.Preconditions;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;

/**
 *
 */
public class CommandRequest {

    private final String database;
    private final AttributeMap attributes;

    protected CommandRequest(
            @Nonnull String database, 
            @Nonnull AttributeMap attributes) {
        Preconditions.checkArgument(database != null);
        Preconditions.checkArgument(attributes != null);
        this.database = database;
        this.attributes = attributes;
    }

    public String getDatabase() {
        return database;
    }

    public AttributeMap getAttributes() {
        return attributes;
    }
}
