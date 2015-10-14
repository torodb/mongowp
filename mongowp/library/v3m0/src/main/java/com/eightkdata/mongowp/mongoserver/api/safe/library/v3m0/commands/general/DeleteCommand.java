
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.mongoserver.api.safe.MarshalException;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.DeleteCommand.DeleteArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.WriteConcernMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mongodb.WriteConcern;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
//TODO: Change the command to return writeErrors like InsertCommand
public class DeleteCommand extends AbstractCommand<DeleteArgument, Long>{

    private static final BsonField<Number> N_FIELD = BsonField.create("n");
    private static final String COMMAND_NAME = "delete";
    public static final DeleteCommand INSTANCE = new DeleteCommand();

    private DeleteCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public Class<? extends DeleteArgument> getArgClass() {
        return DeleteArgument.class;
    }

    @Override
    public DeleteArgument unmarshallArg(BsonDocument requestDoc) throws
            BadValueException, TypesMismatchException, NoSuchKeyException,
            FailedToParseException {
        return DeleteArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(DeleteArgument request) throws
            MarshalException {
        return request.marshall();
    }

    @Override
    public Class<? extends Long> getResultClass() {
        return Long.class;
    }


    @Override
    public Long unmarshallResult(BsonDocument resultDoc) throws
            BadValueException, TypesMismatchException, NoSuchKeyException,
            FailedToParseException, MongoException {
        if (resultDoc == null) {
            return null;
        }
        return BsonReaderTool.getNumeric(resultDoc, N_FIELD).asNumber().longValue();
    }

    @Override
    public BsonDocument marshallResult(Long result) throws MarshalException {
        if (result == null) {
            return null;
        }
        BsonDocumentBuilder builder = new BsonDocumentBuilder();
        builder.appendNumber(N_FIELD, result);

        return builder.build();
    }


    public static class DeleteArgument {
        private static final BsonField<String> COLLECTION_FIELD = BsonField.create(COMMAND_NAME);
        private static final BsonField<BsonArray> STATEMENTS_FIELD = BsonField.create("deletes");
        private static final BsonField<Boolean> ORDERED_FIELD = BsonField.create("ordered");
        private static final BsonField<BsonDocument> WRITE_CONCERN_FIELD = BsonField.create("writeConcern");

        private final String collection;
        private final Iterable<DeleteStatement> statements;
        private final boolean ordered;
        private final WriteConcern writeConcern;

        public DeleteArgument(String collection, Iterable<DeleteStatement> statements, boolean ordered, WriteConcern writeConcern) {
            this.collection = collection;
            this.statements = statements;
            this.ordered = ordered;
            this.writeConcern = writeConcern;
        }

        public String getCollection() {
            return collection;
        }

        public Iterable<DeleteStatement> getStatements() {
            return statements;
        }

        public boolean isOrdered() {
            return ordered;
        }

        public WriteConcern getWriteConcern() {
            return writeConcern;
        }

        public static DeleteArgument unmarshall(BsonDocument doc) throws TypesMismatchException, NoSuchKeyException, FailedToParseException, BadValueException {
            String collection = BsonReaderTool.getString(doc, COLLECTION_FIELD);
            boolean ordered = BsonReaderTool.getBoolean(doc, ORDERED_FIELD, true);

            WriteConcern writeConcern = WriteConcernMarshaller.unmarshall(
                    BsonReaderTool.getDocument(
                            doc,
                            WRITE_CONCERN_FIELD,
                            null
                    ),
                    WriteConcern.ACKNOWLEDGED
            );
            ImmutableList.Builder<DeleteStatement> deletes = ImmutableList.builder();
            BsonArray array = BsonReaderTool.getArray(doc, STATEMENTS_FIELD);
            for (BsonValue uncastedStatement : array) {
                if (!uncastedStatement.isDocument()) {
                    throw new BadValueException(STATEMENTS_FIELD.getFieldName()
                            + " array contains the unexpected value '"
                            + uncastedStatement + "' which is not a document");
                }
                deletes.add(DeleteStatement.unmarshall(uncastedStatement.asDocument()));
            }
            return new DeleteArgument(collection, deletes.build(), ordered, writeConcern);
        }

        public BsonDocument marshall() {
            BsonArray statementsBson = new BsonArray();
            for (DeleteStatement statement : statements) {
                statementsBson.add(statement.marshall());
            }

            return new BsonDocumentBuilder()
                    .append(COLLECTION_FIELD, collection)
                    .append(STATEMENTS_FIELD, statementsBson)
                    .append(ORDERED_FIELD, ordered)
                    .append(WRITE_CONCERN_FIELD, WriteConcernMarshaller.marshall(writeConcern))
                    .build();
        }

        public static class Builder {
            private final String collection;
            private final List<DeleteStatement> statements;
            private boolean ordered = true;
            private WriteConcern writeConcern = WriteConcern.FSYNCED;

            public Builder(@Nonnull String collection) {
                this.collection = collection;
                this.statements = Lists.newArrayList();
            }

            public Builder ordered(boolean ordered) {
                this.ordered = ordered;
                return this;
            }

            public Builder addStatement(DeleteStatement statement) {
                this.statements.add(statement);
                return this;
            }

            public Builder writeConcern(WriteConcern wc) {
                this.writeConcern = wc;
                return this;
            }

            public DeleteArgument build() {
                Preconditions.checkState(!statements.isEmpty(), "No statement has been provided");
                return new DeleteArgument(collection, statements, ordered, writeConcern);
            }
        }

    }

    public static class DeleteStatement {

        private static final BsonField<BsonDocument> QUERY_FIELD = BsonField.create("q");
        private static final BsonField<Integer> LIMIT_FIELD = BsonField.create("limit");

        private final BsonDocument query;
        private final boolean justOne;

        public DeleteStatement(@Nullable BsonDocument query, boolean justOne) {
            this.query = query == null ? new BsonDocument() : query;
            this.justOne = justOne;
        }

        public BsonDocument getQuery() {
            return query;
        }

        public boolean isJustOne() {
            return justOne;
        }

        private static DeleteStatement unmarshall(BsonDocument uncastedStatement) throws TypesMismatchException, NoSuchKeyException {
            return new DeleteStatement(
                    BsonReaderTool.getDocument(uncastedStatement, QUERY_FIELD),
                    BsonReaderTool.getInteger(uncastedStatement, LIMIT_FIELD) != 0
            );
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(QUERY_FIELD, query)
                    .append(LIMIT_FIELD, justOne ? 1 : 0)
                    .build();
        }
    }
}
