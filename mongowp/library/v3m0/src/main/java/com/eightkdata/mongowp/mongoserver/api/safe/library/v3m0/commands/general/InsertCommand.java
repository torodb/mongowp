
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandReply;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleWriteOpResult;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleWriteOpResult.ReplicationInformation;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleWriteOpResult.ShardingInformation;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.WriteConcernMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.FailedToParseException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.google.common.collect.ImmutableList;
import com.mongodb.WriteConcern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
@Immutable
public class InsertCommand extends AbstractCommand<InsertArgument, InsertReply>{

    public static final InsertCommand INSTANCE = new InsertCommand();

    private InsertCommand() {
        super("insert");
    }

    @Override
    public Class<? extends InsertArgument> getArgClass() {
        return InsertArgument.class;
    }

    @Override
    public InsertArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        return InsertArgument.unmarshall(requestDoc, this);
    }

    @Override
    public Class<? extends InsertReply> getReplyClass() {
        return InsertReply.class;
    }

    @Override
    public BsonDocument marshallReply(InsertReply reply) throws
            MongoServerException {
        return reply.marshall();
    }

    @Immutable
    public static class InsertArgument implements CommandArgument {
        private static final BsonField<String> COLL_NAME_FIELD = BsonField.create("insert");
        private static final BsonField<BsonArray> DOCUMENTS_FIELD = BsonField.create("documents");
        private static final BsonField<BsonDocument> WRITE_CONCERN_FIELD = BsonField.create("writeConcern");
        private static final BsonField<Boolean> ORDERED_FIELD = BsonField.create("ordered");
        private static final BsonField<BsonDocument> METADATA_FIELD = BsonField.create("metadata");

        private final @Nonnull InsertCommand command;
        private final @Nonnull String collection;
        private final @Nonnull ImmutableList<BsonDocument> documents;
        private final WriteConcern writeConcern;
        private final boolean ordered;
        private final @Nullable BsonDocument metadata; //TODO: parse metadata

        public InsertArgument(
                InsertCommand command,
                String collection,
                ImmutableList<BsonDocument> documents,
                WriteConcern writeConcern,
                boolean ordered,
                @Nullable BsonDocument metadata) {
            this.command = command;
            this.collection = collection;
            this.documents = documents;
            this.writeConcern = writeConcern;
            this.ordered = ordered;
            this.metadata = metadata;
        }

        @Override
        public Command getCommand() {
            return command;
        }

        @Nonnull
        public String getCollection() {
            return collection;
        }

        @Nonnull
        public ImmutableList<BsonDocument> getDocuments() {
            return documents;
        }

        @Nonnull
        public WriteConcern getWriteConcern() {
            return writeConcern;
        }

        public boolean isOrdered() {
            return ordered;
        }

        @Nullable
        public BsonDocument getMetadata() {
            return metadata;
        }

        private static InsertArgument unmarshall(BsonDocument doc, InsertCommand command)
                throws TypesMismatchException, NoSuchKeyException, FailedToParseException {
            String collection = BsonReaderTool.getString(doc, COLL_NAME_FIELD);

            BsonArray docsArray = BsonReaderTool.getArray(doc, DOCUMENTS_FIELD);
            ImmutableList.Builder<BsonDocument> documentsBuilder = ImmutableList.builder();
            for (BsonValue docToInsert : docsArray) {
                if (!docToInsert.isDocument()) {
                    throw new FailedToParseException("An element of the array "
                            + "of documents to insert is not a document but a "
                            + docToInsert.getBsonType()
                    );
                }
                documentsBuilder.add(docToInsert.asDocument());
            }
            ImmutableList documents = documentsBuilder.build();

            BsonDocument writeConcernDoc = BsonReaderTool.getDocument(
                    doc,
                    WRITE_CONCERN_FIELD,
                    null
            );
            WriteConcern writeConcern;
            if (writeConcernDoc != null) {
                writeConcern = WriteConcernMarshaller.unmarshall(
                        writeConcernDoc
                );
            }
            else {
                writeConcern = WriteConcern.ACKNOWLEDGED;
            }

            boolean orderend = BsonReaderTool.getBoolean(doc, ORDERED_FIELD);

            BsonDocument metadata = BsonReaderTool.getDocument(doc, METADATA_FIELD, null);

            return new InsertArgument(command, collection, documents, writeConcern, orderend, metadata);
        }
    }

    @Immutable
    public static class InsertReply implements CommandReply {

        private static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");
        private static final BsonField<Double> OK_FIELD = BsonField.create("ok");

        private final @Nonnull Command<? extends InsertArgument, ? extends InsertReply> command;
        private final int n;
        private final @Nonnull MongoWP.ErrorCode errorCode;
        private final @Nullable String errorMessage;
        private final ImmutableList<WriteError> writeErrors;
        private final ImmutableList<WriteConcernError> writeConcernErrors;
        private final ReplicationInformation replInfo;
        private final ShardingInformation shardInfo;
        private final OpTime optime;

        public InsertReply(
                Command<? extends InsertArgument, ? extends InsertReply> command,
                int n,
                @Nullable ReplicationInformation replInfo,
                @Nullable ShardingInformation shardInfo,
                @Nonnull OpTime optime) {
            this.command = command;
            this.n = n;
            this.writeErrors = ImmutableList.of();
            this.writeConcernErrors = ImmutableList.of();
            this.errorCode = MongoWP.ErrorCode.OK;
            this.errorMessage = null;
            this.replInfo = replInfo;
            this.shardInfo = shardInfo;
            this.optime = optime;
        }

        public InsertReply(
                Command<? extends InsertArgument, ? extends InsertReply> command,
                MongoWP.ErrorCode errorCode,
                String errorMessage,
                int n,
                ImmutableList<WriteError> writeErrors,
                ImmutableList<WriteConcernError> writeConcernErrors,
                @Nullable ReplicationInformation replInfo,
                @Nullable ShardingInformation shardInfo,
                @Nonnull OpTime optime) {
            this.command = command;
            this.n = n;
            this.writeErrors = writeErrors;
            this.writeConcernErrors = writeConcernErrors;
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
            this.replInfo = replInfo;
            this.shardInfo = shardInfo;
            this.optime = optime;
        }

        @Override
        public WriteOpResult getWriteOpResult() {
            return new SimpleWriteOpResult(errorCode, errorMessage, replInfo, shardInfo, optime);
        }

        public int getN() {
            return n;
        }

        public ImmutableList<WriteError> getWriteErrors() {
            return writeErrors;
        }

        public ImmutableList<WriteConcernError> getWriteConcernErrors() {
            return writeConcernErrors;
        }

        private static final BsonField<Integer> N_FIELD = BsonField.create("n");
        private static final BsonField<BsonArray> WRITE_ERRORS_FIELD = BsonField.create("writeErrors");
        private static final BsonField<BsonArray> WRITE_CONCERN_ERRORS_FIELD = BsonField.create("writeConcernError");

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            String finalErrorMessage = errorMessage;
            if (finalErrorMessage != null) {
                builder.append(ERR_MSG_FIELD, finalErrorMessage);
                builder.append(OK_FIELD, MongoWP.KO);
            }
            else {
                builder.append(OK_FIELD, MongoWP.OK);
            }

            builder.append(N_FIELD, getN());

            if (!getWriteErrors().isEmpty()) {
                BsonArray bsonWriteErrors = new BsonArray();
                for (WriteError writeError : getWriteErrors()) {
                    bsonWriteErrors.add(writeError.marshall());
                }
                builder.append(WRITE_ERRORS_FIELD, bsonWriteErrors);
            }

            if (!getWriteConcernErrors().isEmpty()) {
                BsonArray bsonWriteConcernErrors = new BsonArray();
                for (WriteConcernError writeConcernError : getWriteConcernErrors()) {
                    bsonWriteConcernErrors.add(writeConcernError.marshall());
                }
                builder.append(WRITE_CONCERN_ERRORS_FIELD, bsonWriteConcernErrors);
            }

            return builder.build();
        }

        @Override
        public boolean isOk() {
            return getErrorCode().equals(MongoWP.ErrorCode.OK);
        }

        @Override
        public ErrorCode getErrorCode() {
            return errorCode;
        }

        @Override
        public Command<? extends InsertArgument, ? extends InsertReply> getCommand() {
            return command;
        }

        @Immutable
        public static class WriteError {
            private static final BsonField<Integer> INDEX_FIELD = BsonField.create("index");
            private static final BsonField<Integer> CODE_FIELD = BsonField.create("code");
            private static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");

            private final int index;
            private final int code;
            private final String errmsg;

            public WriteError(int index, int code, String errmsg) {
                this.index = index;
                this.code = code;
                this.errmsg = errmsg;
            }

            public int getIndex() {
                return index;
            }

            public int getCode() {
                return code;
            }

            public String getErrmsg() {
                return errmsg;
            }

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 19 * hash + this.index;
                hash = 19 * hash + this.code;
                hash
                        = 19 * hash +
                        (this.errmsg != null ? this.errmsg.hashCode() : 0);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final WriteError other = (WriteError) obj;
                if (this.index != other.index) {
                    return false;
                }
                if (this.code != other.code) {
                    return false;
                }
                return !((this.errmsg == null) ? (other.errmsg != null)
                        : !this.errmsg.equals(other.errmsg));
            }

            private BsonValue marshall() {
                BsonDocumentBuilder bsonWriteError = new BsonDocumentBuilder();
                bsonWriteError.append(INDEX_FIELD, index);
                bsonWriteError.append(CODE_FIELD, code);
                bsonWriteError.append(ERR_MSG_FIELD, errmsg);

                return bsonWriteError.build();
            }
        }

        @Immutable
        public static class WriteConcernError {
            private static final BsonField<Integer> CODE_FIELD = BsonField.create("code");
            private static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");

            private final int code;
            private final String errmsg;

            public WriteConcernError(int code, String errmsg) {
                this.code = code;
                this.errmsg = errmsg;
            }

            public int getCode() {
                return code;
            }

            public String getErrmsg() {
                return errmsg;
            }

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 79 * hash + this.code;
                hash
                        = 79 * hash +
                        (this.errmsg != null ? this.errmsg.hashCode() : 0);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final WriteConcernError other = (WriteConcernError) obj;
                if (this.code != other.code) {
                    return false;
                }
                return !((this.errmsg == null) ? (other.errmsg != null)
                        : !this.errmsg.equals(other.errmsg));
            }

            private BsonValue marshall() {
                BsonDocumentBuilder bsonWriteConcernError = new BsonDocumentBuilder();
                bsonWriteConcernError.append(CODE_FIELD, code);
                bsonWriteConcernError.append(ERR_MSG_FIELD, errmsg);

                return bsonWriteConcernError.build();
            }
        }
    }
}


