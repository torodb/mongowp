
package com.eightkdata.mongowp.mongoserver.api.impl;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonField;
import com.eightkdata.mongowp.ErrorCode;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;

/**
 *
 */
public class UpdateOpResult extends SimpleWriteOpResult {

    private static final BsonField<Boolean> UPDATE_EXISTING_FIELD_NAME = BsonField.create("updatedExisting");
    private static final BsonField<ObjectId> UPSERTED_FIELD_NAME = BsonField.create("upserted");
    private static final long serialVersionUID = 1L;

    private final long candidates;
    private final long modified;
    private final boolean docReplacement;
    private final @Nullable ObjectId upsertedId;

    public UpdateOpResult(
            @Nonnegative long candidates,
            @Nonnegative long modified,
            boolean docReplacement,
            ErrorCode error,
            String errorDesc,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime) {
        super(error, errorDesc, replInfo, shardInfo, optime);
        this.candidates = candidates;
        this.modified = modified;
        this.upsertedId = null;
        this.docReplacement = docReplacement;
    }

    public UpdateOpResult(
            @Nonnegative long candidates,
            @Nonnegative long modified,
            boolean docReplacement,
            ErrorCode error,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime,
            Object... args) {
        super(error, replInfo, shardInfo, optime, args);
        this.candidates = candidates;
        this.modified = modified;
        this.upsertedId = null;
        this.docReplacement = docReplacement;
    }

    public UpdateOpResult(
            ObjectId upsertedId,
            boolean docReplacement,
            ErrorCode error,
            String errorDesc,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime) {
        super(error, errorDesc, replInfo, shardInfo, optime);
        this.candidates = 0;
        this.modified = 0;
        this.upsertedId = upsertedId;
        this.docReplacement = docReplacement;
    }

    public UpdateOpResult(
            ObjectId upsertedId,
            boolean updateObjects,
            boolean docReplacement,
            ErrorCode error,
            ReplicationInformation replInfo,
            ShardingInformation shardInfo,
            OpTime optime,
            Object... args) {
        super(error, replInfo, shardInfo, optime, args);
        this.candidates = 0;
        this.modified = 0;
        this.upsertedId = upsertedId;
        this.docReplacement = docReplacement;
    }

    @Override
    public long getN() {
        if (upsertedId != null) {
            assert candidates == 0;
            return 1;
        }
        return candidates;
    }

    @Nonnegative
    public long getCandidates() {
        return candidates;
    }

    @Nonnegative
    public long getModified() {
        return modified;
    }

    public boolean isDocReplacement() {
        return docReplacement;
    }

    public ObjectId getUpsertedId() {
        return upsertedId;
    }

    @Override
    public BsonDocument marshall() {
        BsonDocumentBuilder builder = new BsonDocumentBuilder();
        marshall(builder);

        builder.append(UPDATE_EXISTING_FIELD_NAME, modified != 0);
        if (upsertedId != null) {
            builder.append(UPSERTED_FIELD_NAME, upsertedId);
        }

        return builder.build();
    }


}
