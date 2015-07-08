
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.aggregation;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.aggregation.CountCommand.CountArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.aggregation.CountCommand.CountReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.BsonValue;

/**
 *
 */
public class CountCommand extends AbstractCommand<CountArgument, CountReply>{

    public static final CountCommand INSTANCE = new CountCommand();

    private CountCommand() {
        super("count");
    }

    @Override
    public Class<? extends CountArgument> getArgClass() {
        return CountArgument.class;
    }

    @Override
    public CountArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        return CountArgument.unmarshall(requestDoc, this);
    }

    @Override
    public Class<? extends CountReply> getReplyClass() {
        return CountReply.class;
    }

    @Override
    public BsonDocument marshallReply(CountReply reply) throws
            MongoServerException {
        return reply.marshall();
    }


    public static class CountArgument extends SimpleArgument {

        private final BsonDocument query; //TODO(gortiz) parse query
        private final String collection;
        private final @Nullable String hint;
        private final long limit;
        private final long skip;

        public CountArgument(
                Command command,
                @Nonnull String collection,
                @Nonnull BsonDocument query,
                @Nullable String hint,
                @Nonnegative long limit,
                @Nonnegative long skip) {
            super(command);
            this.collection = collection;
            this.query = query;
            this.hint = hint;
            this.limit = limit;
            this.skip = skip;
        }

        /**
         * A query that selects which documents to count in a collection.
         *
         * @return the filter or null if all documents shall be count
         */
        @Nullable
        public BsonDocument getQuery() {
            return query;
        }

        @Nonnegative
        public long getLimit() {
            return limit;
        }

        @Nonnegative
        public long getSkip() {
            return skip;
        }

        /**
         * The index to use.
         * Specify either the index name as a string.
         * @return
         */
        @Nullable
        public String getHint() {
            return hint;
        }

        private static final BsonField<String> COUNT_FIELD = BsonField.create("count");
        private static final BsonField<Long> SKIP_FIELD = BsonField.create("skip");
        private static final BsonField<Long> LIMIT_FIELD = BsonField.create("limit");
        private static final BsonField<BsonDocument> QUERY_FIELD = BsonField.create("query");
        private static final String HINT_FIELD_NAME = "hint";

        public static CountArgument unmarshall(BsonDocument doc, CountCommand command) throws TypesMismatchException, BadValueException, NoSuchKeyException {
            long skip = BsonReaderTool.getLong(doc, SKIP_FIELD, 0);
            if (skip < 0) {
                throw new BadValueException("Skip value is negative in the count query");
            }

            long limit = BsonReaderTool.getLong(doc, LIMIT_FIELD, 0);
            if (limit < 0) {
                // For counts, limit and -limit mean the same thing.
                limit = -limit;
            }

            BsonDocument query;
            try {
                query = BsonReaderTool.getDocument(doc, QUERY_FIELD);
            }
            catch (NoSuchKeyException ex) {
                query = new BsonDocument();
            }
            catch (TypesMismatchException ex) {
                //Some drivers send non object values on query field when no query is specified
                //see mongo SERVER-15456
                query = new BsonDocument();
            }

            String hint = null;
            if (doc.containsKey(HINT_FIELD_NAME)) {
                BsonValue uncastedHint = doc.get(HINT_FIELD_NAME);
                if (uncastedHint.getBsonType().equals(BsonType.STRING)) {
                    hint = uncastedHint.asString().getValue();
                }
                else if (uncastedHint.getBsonType().equals(BsonType.DOCUMENT)) {
                    BsonDocument docHint = uncastedHint.asDocument();
                    if (!docHint.isEmpty()) {
                        hint = docHint.entrySet().iterator().next().getKey();
                    }
                }
            }

            String collection = BsonReaderTool.getString(doc, COUNT_FIELD);

            return new CountArgument(command, collection, query, hint, limit, skip);
        }
    }



    public static class CountReply extends SimpleReply {

        private static final BsonField<Number> N_FIELD = BsonField.create("n");

        private final long count;

        public CountReply(CountCommand command, long count) {
            super(command);
            this.count = count;
        }

        public CountReply(CountCommand command, MongoWP.ErrorCode errorCode, String errorMessage) {
            super(command, errorCode, errorMessage);
            this.count = 0;
        }

        public CountReply(CountCommand command, MongoWP.ErrorCode errorCode, Object... args) {
            super(command, errorCode, args);
            this.count = 0;
        }

        public long getCount() {
            return count;
        }

        BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();
            SimpleReplyMarshaller.marshall(this, builder);

            if (getErrorCode().equals(MongoWP.ErrorCode.OK)) {
                builder.appendNumber(N_FIELD, count);
            }

            return builder.build();
        }
    }

}
