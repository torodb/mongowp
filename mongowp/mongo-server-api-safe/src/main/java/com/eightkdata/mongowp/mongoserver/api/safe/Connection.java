
package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.mongoserver.api.callback.WriteOpResult;
import io.netty.util.AttributeMap;
import java.util.concurrent.Future;

/**
 *
 */
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

    public Future<? extends WriteOpResult> getLastWriteOp() {
        return lastWriteOp;
    }

    public void setLastWriteOp(Future<? extends WriteOpResult> lastWriteOp) {
        this.lastWriteOp = lastWriteOp;
    }

}
