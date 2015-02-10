
package com.eightkdata.mongowp.mongoserver.api.commands;

import io.netty.util.AttributeMap;

/**
 *
 */
public class CommandRequest {

    private final AttributeMap attributes;

    protected CommandRequest(AttributeMap attributes) {
        this.attributes = attributes;
    }

    public AttributeMap getAttributes() {
        return attributes;
    }
}
