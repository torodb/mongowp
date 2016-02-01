
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.fields.ObjectIdField;
import java.io.IOException;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

/**
 *
 */
public class UpdateOpResult extends SimpleWriteOpResult {

    private static final BooleanField UPDATE_EXISTING_FIELD_NAME = new BooleanField("updatedExisting");
    private static final ObjectIdField UPSERTED_FIELD_NAME = new ObjectIdField("upserted");
    private static final long serialVersionUID = 1L;

    private final long candidates;
    private final long modified;
    private final boolean docReplacement;
    private final @Nullable BsonObjectId upsertedId;

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
            BsonObjectId upsertedId,
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
            BsonObjectId upsertedId,
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

    public BsonObjectId getUpsertedId() {
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
