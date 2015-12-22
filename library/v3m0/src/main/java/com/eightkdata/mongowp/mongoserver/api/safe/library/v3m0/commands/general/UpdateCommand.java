
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.mongoserver.api.safe.MarshalException;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.UpdateCommand.UpdateArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.UpdateCommand.UpdateResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.WriteConcernError;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.WriteError;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.WriteConcernMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.*;
import com.google.common.collect.ImmutableList;
import com.mongodb.WriteConcern;
import javax.annotation.Nullable;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
public class UpdateCommand extends AbstractCommand<UpdateArgument, UpdateResult>{

    public static final UpdateCommand INSTANCE = new UpdateCommand();
    private static final String COMMAND_NAME = "update";

    private UpdateCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public Class<? extends UpdateArgument> getArgClass() {
        return UpdateArgument.class;
    }

    @Override
    public boolean isReadyToReplyResult(UpdateResult r) {
        return true;
    }

    @Override
    public UpdateArgument unmarshallArg(BsonDocument requestDoc) throws
            BadValueException, TypesMismatchException, NoSuchKeyException,
            FailedToParseException {
        return UpdateArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(UpdateArgument request) throws
            MarshalException {
        return request.marshall();
    }

    @Override
    public Class<? extends UpdateResult> getResultClass() {
        return UpdateResult.class;
    }

    @Override
    public UpdateResult unmarshallResult(BsonDocument resultDoc) throws
            BadValueException, TypesMismatchException, NoSuchKeyException,
            FailedToParseException, MongoException {
        return UpdateResult.unmarshall(resultDoc);
    }

    @Override
    public BsonDocument marshallResult(UpdateResult result) throws
            MarshalException {
        return result.marshall();
    }

    public static class UpdateArgument {

        private static final BsonField<String> COLLECTION_FIELD = BsonField.create(COMMAND_NAME);
        private static final BsonField<BsonArray> UPDATES_FIELD = BsonField.create("updates");
        private static final BsonField<Boolean> ORDERED_FIELD = BsonField.create("ordered");
        private static final BsonField<BsonDocument> WRITE_CONCERN_FIELD = BsonField.create("writeConcern");

        private final String collection;
        private final Iterable<UpdateStatement> statements;
        private final boolean ordered;
        private final WriteConcern writeConcern;

        public UpdateArgument(String collection, Iterable<UpdateStatement> statements, boolean ordered, WriteConcern writeConcern) {
            this.collection = collection;
            this.statements = statements;
            this.ordered = ordered;
            this.writeConcern = writeConcern;
        }

        private BsonDocument marshall() {
            BsonArray updatesArray = new BsonArray();
            for (UpdateStatement update : statements) {
                updatesArray.add(update.marshall());
            }

            return new BsonDocumentBuilder()
                    .append(COLLECTION_FIELD, collection)
                    .append(UPDATES_FIELD, updatesArray)
                    .append(ORDERED_FIELD, ordered)
                    .append(WRITE_CONCERN_FIELD, WriteConcernMarshaller.marshall(writeConcern))
                    .build();
        }

        private static UpdateArgument unmarshall(BsonDocument requestDoc) throws TypesMismatchException, NoSuchKeyException, FailedToParseException, BadValueException {
            ImmutableList.Builder<UpdateStatement> updates = ImmutableList.builder();

            for (BsonValue element : BsonReaderTool.getArray(requestDoc, UPDATES_FIELD)) {
                if (!element.isDocument()) {
                    throw new BadValueException(UPDATES_FIELD.getFieldName()
                            + " array contains the unexpected value '"
                            + element + "' which is not a document");
                }
                updates.add(UpdateStatement.unmarshall(element.asDocument()));
            }

            WriteConcern writeConcern = WriteConcernMarshaller.unmarshall(
                    BsonReaderTool.getDocument(
                            requestDoc,
                            WRITE_CONCERN_FIELD,
                            null
                    ),
                    WriteConcern.ACKNOWLEDGED
            );

            return new UpdateArgument(
                    BsonReaderTool.getString(requestDoc, COLLECTION_FIELD),
                    updates.build(),
                    BsonReaderTool.getBoolean(requestDoc, ORDERED_FIELD),
                    writeConcern
            );
        }

        public String getCollection() {
            return collection;
        }

        public Iterable<UpdateStatement> getStatements() {
            return statements;
        }

        public boolean isOrdered() {
            return ordered;
        }

        public WriteConcern getWriteConcern() {
            return writeConcern;
        }

    }

    public static class UpdateStatement {

        private static final BsonField<BsonDocument> QUERY_FIELD = BsonField.create("q");
        private static final BsonField<BsonDocument> UPDATE_FIELD = BsonField.create("u");
        private static final BsonField<Boolean> UPSERT_FIELD = BsonField.create("upsert");
        private static final BsonField<Boolean> MULTI_FIELD = BsonField.create("multi");

        private final BsonDocument query;
        private final BsonDocument update;
        private final boolean upsert;
        private final boolean multi;

        public UpdateStatement(BsonDocument query, BsonDocument update, boolean upsert, boolean multi) {
            this.query = query;
            this.update = update;
            this.upsert = upsert;
            this.multi = multi;
        }

        private static UpdateStatement unmarshall(BsonDocument element) throws TypesMismatchException, NoSuchKeyException {
            return new UpdateStatement(
                    BsonReaderTool.getDocument(element, QUERY_FIELD),
                    BsonReaderTool.getDocument(element, UPDATE_FIELD),
                    BsonReaderTool.getBoolean(element, UPSERT_FIELD),
                    BsonReaderTool.getBoolean(element, MULTI_FIELD)
            );
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(QUERY_FIELD, query)
                    .append(UPDATE_FIELD, update)
                    .append(UPSERT_FIELD, upsert)
                    .append(MULTI_FIELD, multi)
                    .build();
        }

        public BsonDocument getQuery() {
            return query;
        }

        public BsonDocument getUpdate() {
            return update;
        }

        public boolean isUpsert() {
            return upsert;
        }

        public boolean isMulti() {
            return multi;
        }
    }

    public static class UpdateResult {

        private static final BsonField<Long> MODIFIED_COUNTER_FIELD = BsonField.create("nModified");
        private static final BsonField<Long> CANDIDATE_COUNTER_FIELD = BsonField.create("n");
        private static final BsonField<BsonArray> WRITE_ERRORS_FIELD = BsonField.create("writeErrors");
        private static final BsonField<BsonArray> WRITE_CONCERN_ERRORS_FIELD = BsonField.create("writeConcernError");
        private static final BsonField<BsonArray> UPSERTED_ARRAY = BsonField.create("upserted");
        private static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");
        private static final BsonField<Double> OK_FIELD = BsonField.create("ok");

        private final long modifiedCounter;
        private final long candidateCounter;
        private final @Nullable String errorMessage;
        private final ImmutableList<WriteError> writeErrors;
        private final ImmutableList<WriteConcernError> writeConcernErrors;
        private final ImmutableList<UpsertResult> upserts;

        public UpdateResult(long modifiedCounter, long candidateCounter) {
            this.modifiedCounter = modifiedCounter;
            this.candidateCounter = candidateCounter;
            this.errorMessage = null;
            this.writeErrors = ImmutableList.of();
            this.writeConcernErrors = ImmutableList.of();
            this.upserts = ImmutableList.of();
        }

        public UpdateResult(long modifiedCounter, long candidateCounter, ImmutableList<UpsertResult> upserts) {
            this.modifiedCounter = modifiedCounter;
            this.candidateCounter = candidateCounter;
            this.upserts = upserts;
            this.errorMessage = null;
            this.writeErrors = ImmutableList.of();
            this.writeConcernErrors = ImmutableList.of();
        }

        public UpdateResult(
                long modifiedCounter,
                long candidateCounter,
                String errorMessage,
                ImmutableList<WriteError> writeErrors,
                ImmutableList<WriteConcernError> writeConcernErrors) {
            this.modifiedCounter = modifiedCounter;
            this.candidateCounter = candidateCounter;
            this.errorMessage = errorMessage;
            this.writeErrors = writeErrors;
            this.writeConcernErrors = writeConcernErrors;
            this.upserts = ImmutableList.of();
        }

        public UpdateResult(
                long modifiedCounter,
                long candidateCounter,
                ImmutableList<UpsertResult> upserts,
                String errorMessage,
                ImmutableList<WriteError> writeErrors,
                ImmutableList<WriteConcernError> writeConcernErrors) {
            this.modifiedCounter = modifiedCounter;
            this.candidateCounter = candidateCounter;
            this.errorMessage = errorMessage;
            this.writeErrors = writeErrors;
            this.writeConcernErrors = writeConcernErrors;
            this.upserts = upserts;
        }

        private static UpdateResult unmarshall(BsonDocument resultDoc) throws TypesMismatchException, NoSuchKeyException, BadValueException {
            boolean ok = BsonReaderTool.getNumeric(resultDoc, OK_FIELD).asNumber().longValue() != 0;
            long modified = BsonReaderTool.getNumeric(resultDoc, MODIFIED_COUNTER_FIELD).asNumber().longValue();
            long candidates = BsonReaderTool.getNumeric(resultDoc, CANDIDATE_COUNTER_FIELD).asNumber().longValue();

            if (ok) {
                return new UpdateResult(modified, candidates);
            }
            else {
                ImmutableList.Builder<WriteError> writeErrors = ImmutableList.builder();
                if (BsonReaderTool.containsField(resultDoc, WRITE_ERRORS_FIELD)) {
                    for (BsonValue element : BsonReaderTool.getArray(resultDoc, WRITE_ERRORS_FIELD)) {
                        if (!element.isDocument()) {
                            throw new BadValueException(WRITE_ERRORS_FIELD.getFieldName()
                                    + " array contains the unexpected value '"
                                    + element + "' which is not a document");
                        }
                        writeErrors.add(WriteError.unmarshall(element.asDocument()));
                    }
                }

                ImmutableList.Builder<WriteConcernError> writeConcernErrors = ImmutableList.builder();
                if (BsonReaderTool.containsField(resultDoc, WRITE_CONCERN_ERRORS_FIELD)) {
                    for (BsonValue element : BsonReaderTool.getArray(resultDoc, WRITE_CONCERN_ERRORS_FIELD)) {
                        if (!element.isDocument()) {
                            throw new BadValueException(WRITE_CONCERN_ERRORS_FIELD.getFieldName()
                                    + " array contains the unexpected value '"
                                    + element + "' which is not a document");
                        }
                        writeConcernErrors.add(WriteConcernError.unmarshall(element.asDocument()));
                    }
                }

                ImmutableList.Builder<UpsertResult> upserts = ImmutableList.builder();
                if (BsonReaderTool.containsField(resultDoc, UPSERTED_ARRAY)) {
                    for (BsonValue element : BsonReaderTool.getArray(resultDoc, UPSERTED_ARRAY)) {
                        if (!element.isDocument()) {
                            throw new BadValueException(UPSERTED_ARRAY.getFieldName()
                                    + " array contains the unexpected value '"
                                    + element + "' which is not a document");
                        }
                        upserts.add(UpsertResult.unmarshall(element.asDocument()));
                    }
                }

                return new UpdateResult(
                        modified,
                        candidates,
                        upserts.build(),
                        BsonReaderTool.getString(resultDoc, ERR_MSG_FIELD, null),
                        writeErrors.build(),
                        writeConcernErrors.build()
                );
            }
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder()
                    .append(OK_FIELD, isOk() ? MongoWP.OK : MongoWP.KO)
                    .appendNumber(MODIFIED_COUNTER_FIELD, modifiedCounter)
                    .appendNumber(CANDIDATE_COUNTER_FIELD, candidateCounter);

            if (!writeErrors.isEmpty()) {
                BsonArray array = new BsonArray();
                for (WriteError writeError : writeErrors) {
                    array.add(writeError.marshall());
                }
                builder.append(WRITE_ERRORS_FIELD, array);
            }

            if (!writeConcernErrors.isEmpty()) {
                BsonArray array = new BsonArray();
                for (WriteConcernError writeConcernError : writeConcernErrors) {
                    array.add(writeConcernError.marshall());
                }
                builder.append(WRITE_CONCERN_ERRORS_FIELD, array);
            }
            if (upserts.isEmpty()) {
                BsonArray array = new BsonArray();
                for (UpsertResult upsert : upserts) {
                    array.add(upsert.marshall());
                }
                builder.append(UPSERTED_ARRAY, array);
            }
            if (errorMessage != null) {
                builder.append(ERR_MSG_FIELD, errorMessage);
            }
            return builder.build();
        }

        public boolean isOk() {
            return writeConcernErrors.isEmpty() && writeErrors.isEmpty();
        }

        public long getModifiedCounter() {
            return modifiedCounter;
        }

        public long getCandidateCounter() {
            return candidateCounter;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public ImmutableList<WriteError> getWriteErrors() {
            return writeErrors;
        }

        public ImmutableList<WriteConcernError> getWriteConcernErrors() {
            return writeConcernErrors;
        }

        public ImmutableList<UpsertResult> getUpserts() {
            return upserts;
        }
    }

    public static class UpsertResult {
        private static final BsonField<Integer> INDEX_FIELD = BsonField.create("index");
        private static final BsonField<BsonValue> ID_FIELD = BsonField.create("_id");

        private final int index;
        private final BsonValue id;

        public UpsertResult(int index, BsonValue id) {
            this.index = index;
            this.id = id;
        }

        private static UpsertResult unmarshall(BsonDocument document) throws TypesMismatchException, NoSuchKeyException {
            return new UpsertResult(
                    BsonReaderTool.getInteger(document, INDEX_FIELD),
                    BsonReaderTool.getValue(document, ID_FIELD)
            );
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(INDEX_FIELD, index)
                    .append(ID_FIELD, id)
                    .build();
        }

        public int getIndex() {
            return index;
        }

        public BsonValue getId() {
            return id;
        }
    }

}
