
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.InsertCommand.InsertResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.WriteConcernError;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.WriteError;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.WriteConcernMarshaller;
import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.FailedToParseException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.mongoserver.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonReaderTool;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mongodb.WriteConcern;
import java.util.List;
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
public class InsertCommand extends AbstractCommand<InsertArgument, InsertResult>{

    public static final InsertCommand INSTANCE = new InsertCommand();

    private InsertCommand() {
        super("insert");
    }

    @Override
    public boolean isReadyToReplyResult(InsertResult r) {
        return true;
    }

    @Override
    public Class<? extends InsertArgument> getArgClass() {
        return InsertArgument.class;
    }

    @Override
    public InsertArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException, FailedToParseException {
        return InsertArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(InsertArgument request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends InsertResult> getResultClass() {
        return InsertResult.class;
    }

    @Override
    public BsonDocument marshallResult(InsertResult reply) {
        return reply.marshall();
    }

    @Override
    public InsertResult unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    public static class InsertArgument {
        private static final BsonField<String> COLL_NAME_FIELD = BsonField.create("insert");
        private static final BsonField<BsonArray> DOCUMENTS_FIELD = BsonField.create("documents");
        private static final BsonField<BsonDocument> WRITE_CONCERN_FIELD = BsonField.create("writeConcern");
        private static final BsonField<Boolean> ORDERED_FIELD = BsonField.create("ordered");
        private static final BsonField<BsonDocument> METADATA_FIELD = BsonField.create("metadata");

        private final @Nonnull String collection;
        private final @Nonnull FluentIterable<BsonDocument> documents;
        private final WriteConcern writeConcern;
        private final boolean ordered;
        private final @Nullable BsonDocument metadata; //TODO: parse metadata

        public InsertArgument(
                String collection,
                Iterable<BsonDocument> documents,
                WriteConcern writeConcern,
                boolean ordered,
                @Nullable BsonDocument metadata) {
            this.collection = collection;
            this.documents = FluentIterable.from(documents);
            this.writeConcern = writeConcern;
            this.ordered = ordered;
            this.metadata = metadata;
        }

        @Nonnull
        public String getCollection() {
            return collection;
        }

        @Nonnull
        public FluentIterable<BsonDocument> getDocuments() {
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

        private static InsertArgument unmarshall(BsonDocument doc)
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
            ImmutableList<BsonDocument> documents = documentsBuilder.build();

            WriteConcern writeConcern = WriteConcernMarshaller.unmarshall(
                    BsonReaderTool.getDocument(
                            doc,
                            WRITE_CONCERN_FIELD,
                            null
                    ),
                    WriteConcern.ACKNOWLEDGED
            );

            boolean orderend = BsonReaderTool.getBoolean(doc, ORDERED_FIELD);

            BsonDocument metadata = BsonReaderTool.getDocument(doc, METADATA_FIELD, null);

            return new InsertArgument(collection, documents, writeConcern, orderend, metadata);
        }

        public static class Builder {
            private String collection;
            private List<BsonDocument> documents = Lists.newArrayList();
            private WriteConcern writeConcern;
            private boolean ordered = true;
            private BsonDocument metadata;

            public Builder(@Nonnull String collection) {
                this.collection = collection;
                writeConcern = WriteConcern.FSYNCED;
            }

            public Builder addDocument(BsonDocument document) {
                documents.add(document);
                return this;
            }

            public Builder addDocuments(Iterable<? extends BsonDocument> documents) {
                for (BsonDocument document : documents) {
                    this.documents.add(document);
                }
                return this;
            }

            public Builder setWriteConcern(WriteConcern writeConcern) {
                this.writeConcern = writeConcern;
                return this;
            }

            public Builder setOrdered(boolean ordered) {
                this.ordered = ordered;
                return this;
            }

            public Builder setMetadata(BsonDocument metadata) {
                this.metadata = metadata;
                return this;
            }

            public InsertArgument build() {
                Preconditions.checkState(collection != null, "No collection has been provided");
                Preconditions.checkState(!documents.isEmpty(), "No document to insert has been provided");
                Preconditions.checkState(writeConcern != null, "No write concern has been provided");

                return new InsertArgument(collection, documents, writeConcern, ordered, metadata);
            }


        }
    }

    @Immutable
    public static class InsertResult {

        private static final BsonField<Integer> N_FIELD = BsonField.create("n");
        private static final BsonField<BsonArray> WRITE_ERRORS_FIELD = BsonField.create("writeErrors");
        private static final BsonField<BsonArray> WRITE_CONCERN_ERRORS_FIELD = BsonField.create("writeConcernError");
        private static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");
        private static final BsonField<Double> OK_FIELD = BsonField.create("ok");

        private final int n;
        private final @Nullable String errorMessage;
        private final ImmutableList<WriteError> writeErrors;
        private final ImmutableList<WriteConcernError> writeConcernErrors;

        public InsertResult(int n) {
            this.n = n;
            this.writeErrors = ImmutableList.of();
            this.writeConcernErrors = ImmutableList.of();
            this.errorMessage = null;
        }

        public InsertResult(
                ErrorCode errorCode,
                String errorMessage,
                int n,
                ImmutableList<WriteError> writeErrors,
                ImmutableList<WriteConcernError> writeConcernErrors) {
            this.n = n;
            this.writeErrors = writeErrors;
            this.writeConcernErrors = writeConcernErrors;
            this.errorMessage = errorMessage;
        }

//        @Override
//        public WriteOpResult getWriteOpResult() {
//            return new SimpleWriteOpResult(errorCode, errorMessage, replInfo, shardInfo, optime);
//        }

        public int getN() {
            return n;
        }

        public ImmutableList<WriteError> getWriteErrors() {
            return writeErrors;
        }

        public ImmutableList<WriteConcernError> getWriteConcernErrors() {
            return writeConcernErrors;
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            String finalErrorMessage = errorMessage;
            if (finalErrorMessage != null) {
                builder.append(ERR_MSG_FIELD, finalErrorMessage);
                builder.append(OK_FIELD, MongoConstants.KO);
            }
            else {
                builder.append(OK_FIELD, MongoConstants.OK);
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


    }


}


