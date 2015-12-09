
package com.eightkdata.mongowp.mongoserver.api.safe;

import javax.annotation.concurrent.ThreadSafe;

/**
 *
 */
@ThreadSafe
public interface ConnectionIdFactory {

    public int newConnectionId();

}
