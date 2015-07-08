
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.api.safe.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;
import java.text.MessageFormat;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.BsonDocument;

/**
 *
 */
public class SimpleWriteOpResult implements WriteOpResult {

    private static final BsonField<String> ERR_FIELD_NAME = BsonField.create("err");
    private static final BsonField<Integer> CODE_FIELD_NAME = BsonField.create("code");
    private static final BsonField<Long> N_FIELD_NAME = BsonField.create("n");

    private final @Nonnull MongoWP.ErrorCode error;
    private final @Nullable String errorDesc;
    private final @Nullable ReplicationInformation replInfo;
    private final @Nullable ShardingInformation shardInfo;

    public SimpleWriteOpResult(
            @Nonnull ErrorCode error,
            @Nonnull String errorDesc,
            @Nullable ReplicationInformation replInfo,
            @Nullable ShardingInformation shardInfo) {
        this.error = error;
        this.replInfo = replInfo;
        this.shardInfo = shardInfo;

        if (error.equals(MongoWP.ErrorCode.OK)) {
            throw new IllegalArgumentException("Error description must be "
                    + "null when the given error code is OK");
        }
        this.errorDesc = errorDesc;
    }

    public SimpleWriteOpResult(
            @Nonnull ErrorCode error,
            @Nullable ReplicationInformation replInfo,
            @Nullable ShardingInformation shardInfo,
            Object... args) {
        this.error = error;
        this.replInfo = replInfo;
        this.shardInfo = shardInfo;

        if (error.equals(MongoWP.ErrorCode.OK)) {
            if (args.length != 0) {
                throw new IllegalArgumentException("Error description must be "
                        + "null when the given error code is OK");
            }
            this.errorDesc = null;
        }
        else {
            errorDesc = MessageFormat.format(error.getErrorMessage(), args);
        }
    }

    public ErrorCode getError() {
        return error;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public ReplicationInformation getReplInfo() {
        return replInfo;
    }

    public ShardingInformation getShardInfo() {
        return shardInfo;
    }

    public long getN() {
        return 0;
    }

    protected void marshall(BsonDocumentBuilder builder) {
        if (errorDesc == null) {
            assert error.equals(MongoWP.ErrorCode.OK);
            builder.appendNull(ERR_FIELD_NAME);
        }
        else {
            builder.append(ERR_FIELD_NAME, errorDesc);
            builder.append(CODE_FIELD_NAME, error.getErrorCode());
        }
        builder.append(N_FIELD_NAME, getN());

        if (replInfo != null) {
            replInfo.marshall(builder);
        }
        if (shardInfo != null) {
            shardInfo.marshall(builder);
        }
    }

    @Override
    public BsonDocument marshall() {
        BsonDocumentBuilder builder = new BsonDocumentBuilder();

        marshall(builder);

        return builder.build();
    }

    @Override
    public boolean errorOcurred() {
        return !error.equals(ErrorCode.OK);
    }

    public static class ReplicationInformation {
        private static final BsonField<OpTime> LAST_OP_FIELD = BsonField.create("lastOp");
        private final @Nonnull OpTime precedingOpTime;

        public ReplicationInformation(@Nonnull OpTime precedingOpTime) {
            this.precedingOpTime = precedingOpTime;
        }

        public OpTime getPrecedingOpTime() {
            return precedingOpTime;
        }

        private void marshall(BsonDocumentBuilder builder) {
            builder.append(LAST_OP_FIELD, precedingOpTime);
        }
    }

    //TODO: Implement this class
    public static class ShardingInformation {
        private ShardingInformation() {}

        private void marshall(BsonDocumentBuilder builder) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
