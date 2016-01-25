
package com.eightkdata.mongowp.mongoserver.api.impl;

import com.eightkdata.mongowp.mongoserver.api.ConnectionIdFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class AtomicConnectionIdFactory implements ConnectionIdFactory {
    public static final AtomicInteger connectionId = new AtomicInteger(0);

    @Override
    public int newConnectionId() {
        return connectionId.incrementAndGet();
    }

}
