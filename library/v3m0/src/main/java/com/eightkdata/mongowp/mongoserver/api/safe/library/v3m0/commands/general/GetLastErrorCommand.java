
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GetLastErrorCommand.GetLastErrorArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.GetLastErrorCommand.GetLastErrorReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.WriteConcernMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.*;
import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;
import com.mongodb.WriteConcern;
import java.util.Map.Entry;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.types.ObjectId;

/**
 *
 */
public class GetLastErrorCommand extends AbstractCommand<GetLastErrorArgument, GetLastErrorReply>{

    public static final GetLastErrorCommand INSTANCE = new GetLastErrorCommand();
    
    private GetLastErrorCommand() {
        super("getLastError");
    }

    @Override
    public Class<? extends GetLastErrorArgument> getArgClass() {
        return GetLastErrorArgument.class;
    }

    @Override
    public GetLastErrorArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return GetLastErrorArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(GetLastErrorArgument request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends GetLastErrorReply> getResultClass() {
        return GetLastErrorReply.class;
    }

    @Override
    public BsonDocument marshallResult(GetLastErrorReply reply) {
        return reply.marshall();
    }

    @Override
    public GetLastErrorReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public boolean isReadyToReplyResult(GetLastErrorReply r) {
        return true;
    }

    public static class GetLastErrorArgument {

        private static final BsonField<OpTime> W_OP_TIME_FIELD = BsonField.create("wOpTime");
        private static final BsonField<ObjectId> W_ELECTION_ID_FIELD = BsonField.create("wElectionId");

        private final @Nullable WriteConcern writeConcern;
        private final @Nullable BsonDocument badGLE;
        private final @Nullable OpTime wOpTime;
        private final @Nullable ObjectId wElectionId;
        private final @Nullable String badGLEMessage;
        private final ErrorCode badGLEErrorCode;

        public GetLastErrorArgument(
                WriteConcern writeConcern,
                BsonDocument badGLE,
                String badGLEMessage,
                @Nonnull ErrorCode badGLEErrorCode,
                OpTime wOpTime,
                ObjectId wElectionId) {
            this.writeConcern = writeConcern;
            this.badGLE = badGLE;
            this.badGLEMessage = badGLEMessage;
            this.badGLEErrorCode = badGLEErrorCode;
            this.wOpTime = wOpTime;
            this.wElectionId = wElectionId;
        }

        public boolean isValid() {
            return badGLE == null;
        }

        public String getBadGLEMessage() {
            return badGLEMessage;
        }

        @Nonnull
        public ErrorCode getBadGLEErrorCode() {
            return badGLEErrorCode;
        }

        @Nullable
        public WriteConcern getWriteConcern() {
            return writeConcern;
        }

        @Nullable
        public BsonDocument getBadGLE() {
            return badGLE;
        }

        @Nullable
        public OpTime getWOpTime() {
            return wOpTime;
        }

        @Nullable
        public ObjectId getWElectionId() {
            return wElectionId;
        }

        private static GetLastErrorArgument unmarshall(BsonDocument requestDoc) {
            OpTime opTime;
            try {
                opTime = BsonReaderTool.getOpTime(requestDoc, W_OP_TIME_FIELD, null);
            }
            catch (TypesMismatchException ex) {
                return new GetLastErrorArgument(
                        null,
                        requestDoc,
                        ex.getLocalizedMessage(),
                        ex.getErrorCode(),
                        null,
                        null
                );
            }
            ObjectId electionId;
            try {
                electionId = BsonReaderTool.getObjectId(requestDoc, W_ELECTION_ID_FIELD, null);
            } catch (TypesMismatchException ex) {
                return new GetLastErrorArgument(
                        null,
                        requestDoc,
                        ex.getLocalizedMessage(),
                        ex.getErrorCode(),
                        opTime,
                        null
                );
            }
            WriteConcern wc;
            try {
                wc = WriteConcernMarshaller.unmarshall(requestDoc);
            }
            catch (TypesMismatchException ex) {
                return new GetLastErrorArgument(
                        null,
                        requestDoc,
                        ex.getLocalizedMessage(),
                        ex.getErrorCode(),
                        opTime,
                        electionId
                );
            }
            catch (FailedToParseException ex) {
                return new GetLastErrorArgument(
                        null,
                        requestDoc,
                        ex.getLocalizedMessage(),
                        ex.getErrorCode(),
                        opTime,
                        electionId
                );
            }
            return new GetLastErrorArgument(
                    wc,
                    null,
                    null,
                    ErrorCode.OK,
                    opTime,
                    electionId
            );
        }

    }

    public static class GetLastErrorReply {
        private static final BsonField<Double> OK_FIELD = BsonField.create("ok");
        private static final BsonField<Double> CONNECTION_ID_FIELD = BsonField.create("connectionId");
        private static final BsonField<BsonDocument> BAD_GLE_FIELD = BsonField.create("badGLE");
        private static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");
        private static final BsonField<String> ERR_FIELD = BsonField.create("err");
        private static final BsonField<Integer> CODE_FIELD = BsonField.create("code");

        private final @Nonnull ErrorCode thisError;
        private final @Nullable String thisErrorMessage;
        private final long connectionId;
        private final @Nullable WriteOpResult writeOpResult;
        private final @Nullable GetLastErrorArgument arg;
        private final @Nullable WriteConcernEnforcementResult wcer;

        public GetLastErrorReply(
                @Nonnull ErrorCode thisError,
                @Nullable String thisErrorMessage,
                long connectionId,
                @Nullable WriteOpResult writeOpResult,
                @Nonnull GetLastErrorArgument arg,
                @Nullable WriteConcernEnforcementResult wcer) {
            this.thisError = thisError;
            this.thisErrorMessage = thisErrorMessage;
            this.connectionId = connectionId;
            this.writeOpResult = writeOpResult;
            this.arg = arg;
            this.wcer = wcer;
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();
            builder.append(CONNECTION_ID_FIELD, connectionId);

            if (arg.getBadGLE() != null) {
                builder.append(BAD_GLE_FIELD, arg.getBadGLE());
                builder.append(ERR_MSG_FIELD, arg.getBadGLEMessage());
                return builder.build();
            }

            if (writeOpResult != null) {
                for (Entry<String, BsonValue> entry : writeOpResult.marshall().entrySet()) {
                    builder.appendUnsafe(entry.getKey(), entry.getValue());
                }

                if (writeOpResult.errorOcurred()) {
                    return builder.build();
                }
            }

            if (wcer != null) {
                wcer.marshall(builder);
            }

            if (!thisError.equals(ErrorCode.OK)) {
                builder.append(CODE_FIELD, thisError.getErrorCode());
                if (thisErrorMessage != null) {
                    builder.append(ERR_FIELD, thisErrorMessage);
                }
            }

            boolean ok = thisError.equals(ErrorCode.OK);
            builder.append(OK_FIELD, ok ? MongoWP.OK : MongoWP.KO);

            return builder.build();
        }
    }

    @Immutable
    public static class WriteConcernEnforcementResult {
        private static final BsonField<Integer> SYNC_MILLIS_FIELD = BsonField.create("syncMillis");
        private static final BsonField<Integer> FSYNC_FILES_FIELD = BsonField.create("fsyncFiles");
        private static final BsonField<String> ERR_FIELD = BsonField.create("err");
        private static final BsonField<Integer> W_TIMED_FIELD = BsonField.create("wtimeout");
        private static final BsonField<Integer> WAITED_FIELD = BsonField.create("waited");
        private static final BsonField<Boolean> W_TIMEDOUT_FIELD = BsonField.create("wtimeout");
        private static final BsonField<BsonArray> WRITTEN_TO_FIELD = BsonField.create("writtenTo");

        private final @Nonnull WriteConcern writeConcern;
        private final @Nullable String err;
        private final @Nullable @Nonnegative Integer syncMillis;
        private final @Nullable @Nonnegative Integer fsyncFiles;
        private final boolean wTimedOut;
        private final @Nullable @Nonnegative Integer wTime;
        private final ImmutableList<HostAndPort> writtenTo;

        public WriteConcernEnforcementResult(
                @Nonnull WriteConcern writeConcern,
                @Nullable String err,
                @Nullable Integer syncMillis,
                @Nullable Integer fsyncFiles,
                boolean wTimedOut,
                @Nullable Integer wTime,
                @Nullable ImmutableList<HostAndPort> writtenTo) {
            this.writeConcern = writeConcern;
            this.err = err;
            this.syncMillis = syncMillis;
            this.fsyncFiles = fsyncFiles;
            this.wTimedOut = wTimedOut;
            this.wTime = wTime;
            this.writtenTo = writtenTo;
        }

        private void marshall(BsonDocumentBuilder builder) {
            if (syncMillis != null) {
                builder.append(SYNC_MILLIS_FIELD, syncMillis);
            }
            if (fsyncFiles != null) {
                builder.append(FSYNC_FILES_FIELD, fsyncFiles);
            }
            if (wTime != null) {
                if (wTimedOut) {
                    builder.append(WAITED_FIELD, wTime);
                }
                else {
                    builder.append(W_TIMED_FIELD, wTime);
                }
            }

            // *** 2.4 SyncClusterConnection compatibility ***
            // 2.4 expects either fsync'd files, or a "waited" field exist after running an fsync : true
            // GLE, but with journaling we don't actually need to run the fsync (fsync command is
            // preferred in 2.6).  So we add a "waited" field if one doesn't exist.
            if (writeConcern.fsync() && !builder.containsField(WAITED_FIELD) && !builder.containsField(FSYNC_FILES_FIELD)) {
                if (wTime != null) {
                    builder.append(WAITED_FIELD, wTime);
                }
                else {
                    assert syncMillis != null;
                    builder.append(WAITED_FIELD, syncMillis);
                }
            }

            if (wTimedOut) {
                builder.append(W_TIMEDOUT_FIELD, wTimedOut);
            }

            if (writtenTo != null && !writtenTo.isEmpty()) {
                BsonArray array = new BsonArray();
                for (HostAndPort w : writtenTo) {
                    array.add(new BsonString(w.toString()));
                }
                builder.append(WRITTEN_TO_FIELD, array);
            }
            else {
                builder.appendNull(WRITTEN_TO_FIELD);
            }

            if (err == null || err.isEmpty()) {
                builder.appendNull(ERR_FIELD);
            }
            else {
                builder.append(ERR_FIELD, err);
            }
        }
    }

}
