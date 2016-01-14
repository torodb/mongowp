
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;
import java.io.Serializable;
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
    private static final BsonField<Double> N_FIELD_NAME = BsonField.create("n");
    private static final long serialVersionUID = 1L;

    private final @Nonnull ErrorCode error;
    private final @Nullable String errorDesc;
    private final @Nullable ReplicationInformation replInfo;
    private final @Nullable ShardingInformation shardInfo;
    private final @Nonnull OpTime optime;

    public SimpleWriteOpResult(
            @Nonnull ErrorCode error,
            @Nullable String errorDesc,
            @Nullable ReplicationInformation replInfo,
            @Nullable ShardingInformation shardInfo,
            @Nonnull OpTime optime) {
        this.error = error;
        this.replInfo = replInfo;
        this.shardInfo = shardInfo;

        if (errorDesc != null && error.equals(ErrorCode.OK)) {
            throw new IllegalArgumentException("Error description must be "
                    + "null when the given error code is OK");
        }
        this.errorDesc = errorDesc;
        this.optime = optime;
    }

    public SimpleWriteOpResult(
            @Nonnull ErrorCode error,
            @Nullable ReplicationInformation replInfo,
            @Nullable ShardingInformation shardInfo,
            @Nonnull OpTime optime,
            Object... args) {
        this.error = error;
        this.replInfo = replInfo;
        this.shardInfo = shardInfo;

        if (error.equals(ErrorCode.OK)) {
            if (args.length != 0) {
                throw new IllegalArgumentException("Error description must be "
                        + "null when the given error code is OK");
            }
            this.errorDesc = null;
        }
        else {
            errorDesc = MessageFormat.format(error.getErrorMessage(), args);
        }
        this.optime = optime;
    }

    @Override
    public OpTime getOpTime() {
        return optime;
    }

    @Override
    public ErrorCode getErrorCode() {
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
            assert error.equals(ErrorCode.OK);
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

    public static class ReplicationInformation implements Serializable {
        private static final BsonField<OpTime> LAST_OP_FIELD = BsonField.create("lastOp");
        private static final long serialVersionUID = 1L;
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
    public static class ShardingInformation implements Serializable {
        private static final long serialVersionUID = 1L;
        private ShardingInformation() {}

        private void marshall(BsonDocumentBuilder builder) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

}
