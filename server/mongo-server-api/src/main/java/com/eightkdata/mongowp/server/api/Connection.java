
package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.server.callback.WriteOpResult;
import io.netty.util.AttributeMap;
import java.util.concurrent.Future;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 */
@NotThreadSafe
public class Connection {

    private final int connectionId;
    private final AttributeMap attributeMap;
    private Future<? extends WriteOpResult> lastWriteOp;
    
    public Connection(int connectionId, AttributeMap attributeMap) {
        this.connectionId = connectionId;
        this.attributeMap = attributeMap;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public AttributeMap getAttributeMap() {
        return attributeMap;
    }

    @Nullable
    public Future<? extends WriteOpResult> getAppliedLastWriteOp() {
        return lastWriteOp;
    }

    public void setAppliedWriteOp(Future<? extends WriteOpResult> lastWriteOp) {
        this.lastWriteOp = lastWriteOp;
    }

}
