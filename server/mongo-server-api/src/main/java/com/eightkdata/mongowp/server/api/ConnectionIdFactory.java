
package com.eightkdata.mongowp.server.api;

import javax.annotation.concurrent.ThreadSafe;

/**
 *
 */
@ThreadSafe
public interface ConnectionIdFactory {

    public int newConnectionId();

}
