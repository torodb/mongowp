
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;

/**
 *
 */
public class DeleteOpResult extends SimpleWriteOpResult {

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
