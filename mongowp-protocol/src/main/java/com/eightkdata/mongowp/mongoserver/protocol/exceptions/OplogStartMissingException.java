
package com.eightkdata.mongowp.mongoserver.protocol.exceptions;

import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;
import com.google.common.net.HostAndPort;

/**
 *
 */
public class OplogStartMissingException extends MongoException {
    private static final long serialVersionUID = 1L;

    private final HostAndPort syncSource;

    public OplogStartMissingException(HostAndPort syncSource) {
        super(ErrorCode.OPLOG_START_MISSING, syncSource);
        this.syncSource = syncSource;
    }

    public HostAndPort getSyncSource() {
        return syncSource;
    }

}
