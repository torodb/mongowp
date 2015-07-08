
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ApplyOpsCommand.ApplyOpsArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ApplyOpsCommand.ApplyOpsReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.OplogOperationParser;
import com.eightkdata.mongowp.mongoserver.api.safe.oplog.OplogOperation;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.google.common.collect.ImmutableList;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.*;

/**
 *
 */
public class ApplyOpsCommand extends AbstractCommand<ApplyOpsArgument, ApplyOpsReply> {

    public static final ApplyOpsCommand INSTANCE = new ApplyOpsCommand();

    private ApplyOpsCommand() {
        super("applyOps");
    }

    @Override
    public boolean isSlaveOk() {
        return false;
    }

    @Override
    public Class<? extends ApplyOpsArgument> getArgClass() {
        return ApplyOpsArgument.class;
    }

    @Override
    public ApplyOpsArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        return ApplyOpsArgument.fromBson(requestDoc, this);
    }

    @Override
    public Class<? extends ApplyOpsReply> getReplyClass() {
        return ApplyOpsReply.class;
    }

    @Override
    public BsonDocument marshallReply(ApplyOpsReply reply) throws
            MongoServerException {
        return reply.marshall();
    }

    @Immutable
    public static class ApplyOpsArgument extends SimpleArgument {

        private final boolean alwaysUpsert;
        private final ImmutableList<OplogOperation> operations;
        private final ImmutableList<Precondition> preconditions;

        public ApplyOpsArgument(
                ApplyOpsCommand command,
                boolean alwaysUpsert,
                ImmutableList<OplogOperation> operations,
                ImmutableList<Precondition> preconditions) {
            super(command);
            this.alwaysUpsert = alwaysUpsert;
            this.operations = operations;
            this.preconditions = preconditions;
        }

        public boolean isAlwaysUpsert() {
            return alwaysUpsert;
        }

        public ImmutableList<OplogOperation> getOperations() {
            return operations;
        }

        public ImmutableList<Precondition> getPreconditions() {
            return preconditions;
        }

        private static ApplyOpsArgument fromBson(BsonDocument requestDoc, ApplyOpsCommand command) 
                throws BadValueException, TypesMismatchException, NoSuchKeyException {
            final String commandName = command.getCommandName();
            if (!requestDoc.containsKey(commandName) || !requestDoc.get(commandName).isArray()) {
                throw new BadValueException("ops has to be an array");
            }

            boolean alwaysUpsert = BsonReaderTool.getBoolean(requestDoc, "alwaysUpsert", true);

            BsonArray opsArray = requestDoc.get(commandName).asArray();
            ImmutableList.Builder<OplogOperation> ops = ImmutableList.builder();
            for (BsonValue uncastedOp : opsArray) {
                ops.add(OplogOperationParser.fromBson(uncastedOp));
            }

            ImmutableList.Builder<Precondition> preconditions = ImmutableList.builder();
            if (requestDoc.containsKey("preCondition") && requestDoc.get("preCondition").isArray()) {
                for (BsonValue uncastedPrecondition : requestDoc.get("preCondition").asArray()) {
                    preconditions.add(Precondition.fromBson(uncastedPrecondition));
                }
            }
            return new ApplyOpsArgument(command, alwaysUpsert, ops.build(), preconditions.build());
        }


        public static class Precondition {

            private final String namespace;
            private final BsonDocument query;
            private final BsonDocument restriction;

            public Precondition(String namespace, BsonDocument query, BsonDocument restriction) {
                this.namespace = namespace;
                this.query = query;
                this.restriction = restriction;
            }

            public String getNamespace() {
                return namespace;
            }

            public BsonDocument getQuery() {
                return query;
            }

            public BsonDocument getRestriction() {
                return restriction;
            }

            private static Precondition fromBson(BsonValue uncastedPrecondition) throws BadValueException, TypesMismatchException, NoSuchKeyException {
                if (!uncastedPrecondition.isDocument()) {
                    throw new BadValueException("applyOps preconditions must "
                            + "be an array of documents, but one of their "
                            + "elements has the non document value '"+uncastedPrecondition);
                }
                BsonDocument preconditionDoc = uncastedPrecondition.asDocument();
                String namespace = BsonReaderTool.getString(preconditionDoc, "ns");
                BsonDocument query = BsonReaderTool.getDocument(preconditionDoc, "q");
                BsonDocument req = BsonReaderTool.getDocument(preconditionDoc, "res");

                return new Precondition(namespace, query, req);
            }
        }

    }

    @Immutable
    public static class ApplyOpsReply extends SimpleReply {
        private static final BsonField<BsonDocument> GOT_FIELD = BsonField.create("got");
        private static final BsonField<BsonDocument> WHAT_FAILED_FIELD = BsonField.create("whatFailed");
        private static final BsonField<String> ERRMSG_FIELD = BsonField.create("errmsg");
        private static final BsonField<Integer> APPLIED_FIELD = BsonField.create("applied");
        private static final BsonField<BsonArray> RESULT_FIELD = BsonField.create("result");

        private final int num;
        private final ImmutableList<Boolean> results;
        private final BsonDocument got;
        private final BsonDocument whatFailed;

        private ApplyOpsReply(
                ApplyOpsCommand command,
                BsonDocument got,
                BsonDocument whatFailed) {
            super(command, ErrorCode.COMMAND_FAILED);

            this.got = got;
            this.whatFailed = whatFailed;

            this.num = 0;
            this.results = ImmutableList.of();
        }

        public ApplyOpsReply(int num, ImmutableList<Boolean> results, Command command) {
            super(command);
            this.num = num;
            this.results = results;

            this.got = null;
            this.whatFailed = null;
        }

        public int getNum() {
            return num;
        }

        public ImmutableList<Boolean> getResults() {
            return results;
        }

        @Nullable
        public BsonDocument getGot() {
            return got;
        }

        @Nullable
        public BsonDocument getWhatFailed() {
            return whatFailed;
        }

        @Override
        public ApplyOpsCommand getCommand() {
            return (ApplyOpsCommand) super.getCommand();
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();
            if (!isOk()) {
                builder.append(GOT_FIELD, got)
                        .append(WHAT_FAILED_FIELD, whatFailed)
                        .append(ERRMSG_FIELD, "pre-condition failed");
            }
            else {
                builder.append(APPLIED_FIELD, getNum());

                BsonArray bsonResult = new BsonArray();
                for (Boolean iestResult : results) {
                    bsonResult.add(BsonBoolean.valueOf(iestResult));
                }
                builder.append(RESULT_FIELD, bsonResult);
            }
            return builder.build();
        }
    }

}
