
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class DeleteOpResult extends SimpleWriteOpResult {
    private static final long serialVersionUID = 1L;

    private final long deletedDocsCounter;

    public DeleteOpResult(
            long deletedDocsCounter,
            ErrorCode error,
            String errorDesc,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime) {
        super(error, errorDesc, replInfo, shardInfo, optime);
        this.deletedDocsCounter = deletedDocsCounter;
    }

    public long getDeletedDocsCounter() {
        return deletedDocsCounter;
    }

    @Override
    public long getN() {
        return deletedDocsCounter;
    }
}
