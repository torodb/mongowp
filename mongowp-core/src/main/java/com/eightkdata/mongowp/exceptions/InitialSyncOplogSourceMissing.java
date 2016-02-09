
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.google.common.net.HostAndPort;

/**
 *
 */
public class InitialSyncOplogSourceMissing extends MongoException {
    private static final long serialVersionUID = 1L;
    private final HostAndPort syncSource;

    public InitialSyncOplogSourceMissing(HostAndPort syncSource) {
        super(ErrorCode.INITIAL_SYNC_OPLOG_SOURCE_MISSING, syncSource);
        this.syncSource = syncSource;
    }

    public HostAndPort getSyncSource() {
        return syncSource;
    }
}
