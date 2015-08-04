
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;

/**
 *
 */
public class UpdateOpResult extends SimpleWriteOpResult {

    private static final BsonField<Boolean> UPDATE_EXISTING_FIELD_NAME = BsonField.create("updatedExisting");
    private static final BsonField<ObjectId> UPSERTED_FIELD_NAME = BsonField.create("upserted");

    private final long matchedDocumentsCounter;
    private final boolean updateObjects;
    private final @Nullable ObjectId upsertedId;

    public UpdateOpResult(
            long matchedDocumentsCounter,
            boolean updateObjects,
            ErrorCode error,
            String errorDesc,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime) {
        super(error, errorDesc, replInfo, shardInfo, optime);
        this.matchedDocumentsCounter = matchedDocumentsCounter;
        this.updateObjects = updateObjects;
        this.upsertedId = null;
    }

    public UpdateOpResult(
            long matchedDocumentsCounter,
            boolean updateObjects,
            ErrorCode error,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime,
            Object... args) {
        super(error, replInfo, shardInfo, optime, args);
        this.matchedDocumentsCounter = matchedDocumentsCounter;
        this.updateObjects = updateObjects;
        this.upsertedId = null;
    }

    public UpdateOpResult(
            ObjectId upsertedId,
            boolean updateObjects,
            ErrorCode error,
            String errorDesc,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime) {
        super(error, errorDesc, replInfo, shardInfo, optime);
        this.matchedDocumentsCounter = 0;
        this.updateObjects = updateObjects;
        this.upsertedId = upsertedId;
    }

    public UpdateOpResult(
            ObjectId upsertedId,
            boolean updateObjects,
            ErrorCode error,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime,
            Object... args) {
        super(error, replInfo, shardInfo, optime, args);
        this.matchedDocumentsCounter = 0;
        this.updateObjects = updateObjects;
        this.upsertedId = upsertedId;
    }

    @Override
    public long getN() {
        if (upsertedId != null) {
            assert matchedDocumentsCounter == 0;
            return 1;
        }
        return matchedDocumentsCounter;
    }

    @Override
    public BsonDocument marshall() {
        BsonDocumentBuilder builder = new BsonDocumentBuilder();
        marshall(builder);

        builder.append(UPDATE_EXISTING_FIELD_NAME, updateObjects);
        if (upsertedId != null) {
            builder.append(UPSERTED_FIELD_NAME, upsertedId);
        }

        return builder.build();
    }


}
