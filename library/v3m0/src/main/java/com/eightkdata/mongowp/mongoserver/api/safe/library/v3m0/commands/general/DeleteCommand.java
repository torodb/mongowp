
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general;

import com.eightkdata.mongowp.WriteConcern;
import com.eightkdata.mongowp.WriteConcern.SyncMode;
import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.annotations.NotMutable;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.*;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.general.DeleteCommand.DeleteArgument;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.utils.BsonArrayBuilder;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
//TODO: Change the command to return writeErrors like InsertCommand
public class DeleteCommand extends AbstractCommand<DeleteArgument, Long>{

    private static final NumberField<?> N_FIELD = new NumberField<>("n");
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
        private static final StringField COLLECTION_FIELD = new StringField(COMMAND_NAME);
        private static final ArrayField STATEMENTS_FIELD = new ArrayField("deletes");
        private static final BooleanField ORDERED_FIELD = new BooleanField("ordered");
        private static final DocField WRITE_CONCERN_FIELD = new DocField("writeConcern");

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

            WriteConcern writeConcern = WriteConcern.fromDocument(
                    BsonReaderTool.getDocument(doc, WRITE_CONCERN_FIELD, null),
                    WriteConcern.with(SyncMode.NONE)
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
            BsonArrayBuilder statementsBson = new BsonArrayBuilder();
            for (DeleteStatement statement : statements) {
                statementsBson.add(statement.marshall());
            }

            return new BsonDocumentBuilder()
                    .append(COLLECTION_FIELD, collection)
                    .append(STATEMENTS_FIELD, statementsBson.build())
                    .append(ORDERED_FIELD, ordered)
                    .append(WRITE_CONCERN_FIELD, writeConcern.toDocument())
                    .build();
        }

        public static class Builder {
            private final String collection;
            private final List<DeleteStatement> statements;
            private boolean ordered = true;
            private WriteConcern writeConcern = WriteConcern.fsync();

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

        private static final DocField QUERY_FIELD = new DocField("q");
        private static final BooleanField LIMIT_FIELD = new BooleanField("limit");

        private final BsonDocument query;
        private final boolean justOne;

        public DeleteStatement(@NotMutable @Nullable BsonDocument query, boolean justOne) {
            this.query = query == null ? DefaultBsonValues.EMPTY_DOC : query;
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
                    BsonReaderTool.getBooleanOrNumeric(uncastedStatement, LIMIT_FIELD, false)
            );
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(QUERY_FIELD, query)
                    .append(LIMIT_FIELD, justOne)
                    .build();
        }
    }
}
