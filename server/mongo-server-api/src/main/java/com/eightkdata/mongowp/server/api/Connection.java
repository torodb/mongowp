
package com.eightkdata.mongowp.server.api;

import javax.annotation.concurrent.NotThreadSafe;

/**
 *
 */
@NotThreadSafe
public interface Connection extends AutoCloseable {
    public int getConnectionId();

    @Override
    public void close();
}
