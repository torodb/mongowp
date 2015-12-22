
package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
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
    private OpTime lastRequestedWriteOpTime;

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
    public OpTime getLastRequestedWriteOpTime() {
        return lastRequestedWriteOpTime;
    }

    public void setLastRequestedWriteOpTime(OpTime lastRequestedWriteOpTime) {
        this.lastRequestedWriteOpTime = lastRequestedWriteOpTime;
    }

    @Nullable
    public Future<? extends WriteOpResult> getAppliedLastWriteOp() {
        return lastWriteOp;
    }

    public void setAppliedWriteOp(Future<? extends WriteOpResult> lastWriteOp) {
        this.lastWriteOp = lastWriteOp;
    }

}
