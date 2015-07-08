package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetSyncFromCommand.ReplSetSyncFromArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetSyncFromCommand.ReplSetSyncFromReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import com.google.common.net.HostAndPort;
import java.util.Arrays;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 */
public class ReplSetSyncFromCommand extends AbstractCommand<ReplSetSyncFromArgument, ReplSetSyncFromReply>{

    public static final ReplSetSyncFromCommand INSTANCE = new ReplSetSyncFromCommand();

    private ReplSetSyncFromCommand() {
        super("replSetSyncFrom");
    }

    @Override
    public Class<? extends ReplSetSyncFromArgument> getArgClass() {
        return ReplSetSyncFromArgument.class;
    }

    @Override
    public ReplSetSyncFromArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        return new ReplSetSyncFromArgument(
                this,
                BsonReaderTool.getHostAndPort(
                        requestDoc,
                        getCommandName(),
                        null)
        );
    }

    @Override
    public Class<? extends ReplSetSyncFromReply> getReplyClass() {
        return ReplSetSyncFromReply.class;
    }

    @Override
    public BsonDocument marshallReply(ReplSetSyncFromReply reply) throws
            MongoServerException {
        BsonDocument result = SimpleReplyMarshaller.marshall(reply);

        if (reply.getPrevSyncTarget() != null) {
            result.append("prevSyncTarget", new BsonString(reply.getPrevSyncTarget().toString()));
        }

        if (reply.getWarning() != null) {
            result.append("warning", new BsonString(reply.getWarning()));
        }

        return result;
    }

    @Immutable
    public static class ReplSetSyncFromArgument extends SimpleArgument {

        private final HostAndPort newTarget;

        public ReplSetSyncFromArgument(ReplSetSyncFromCommand command, HostAndPort newTarget) {
            super(command);
            this.newTarget = newTarget;
        }

        public HostAndPort getNewTarget() {
            return newTarget;
        }

        @Override
        public String toString() {
            return "replSetSyncFromRequest(" + newTarget + ')';
        }

    }

    @Immutable
    public static class ReplSetSyncFromReply extends SimpleReply {

        @Nullable
        private final HostAndPort prevSyncTarget;
        @Nullable
        private final String warning;

        public ReplSetSyncFromReply(
                @Nonnull ReplSetSyncFromCommand command,
                @Nonnull HostAndPort prevSyncTarget,
                @Nullable String warning) {
            super(command);
            this.prevSyncTarget = prevSyncTarget;
            this.warning = warning;
        }

        public ReplSetSyncFromReply(
                @Nonnull ReplSetSyncFromCommand command,
                @Nullable String warning,
                @Nonnull MongoWP.ErrorCode errorCode,
                @Nonnull String errorMessage) {
            super(command, errorCode, errorMessage);
            this.prevSyncTarget = null;
            this.warning = warning;
        }

        public ReplSetSyncFromReply(
                @Nonnull ReplSetSyncFromCommand command,
                @Nullable String warning,
                @Nonnull MongoWP.ErrorCode errorCode, Object... args) {
            super(command, errorCode, args);
            this.prevSyncTarget = null;
            this.warning = warning;
        }

        public HostAndPort getPrevSyncTarget() {
            return prevSyncTarget;
        }

        public String getWarning() {
            return warning;
        }


        public static class Builder {

            private final @Nonnull ReplSetSyncFromCommand command;
            private MongoWP.ErrorCode errorCode;
            private String errorMessage;
            private HostAndPort prevSyncTarget;
            private Object[] args;
            private String warning;

            public Builder(ReplSetSyncFromCommand command, @Nonnull MongoWP.ErrorCode errorCode) {
                this.command = command;
                this.errorCode = errorCode;
            }

            public Builder(ReplSetSyncFromCommand command, @Nonnull MongoWP.ErrorCode errorCode, @Nonnull String errorMessage) {
                this.command = command;
                this.errorCode = errorCode;
                this.errorMessage = errorMessage;
            }

            public Builder(ReplSetSyncFromCommand command, @Nonnull String errorMessage) {
                this.command = command;
                this.errorMessage = errorMessage;
            }

            public Builder(ReplSetSyncFromCommand command, @Nonnull HostAndPort prevSyncTarget) {
                this.command = command;
                this.prevSyncTarget = prevSyncTarget;
            }

            public Builder withArgs(Object[] args) {
                this.args = Arrays.copyOf(args, args.length);
                return this;
            }

            public Builder setWarning(String warning) {
                this.warning = warning;
                return this;
            }

            public ReplSetSyncFromReply build() {
                if (errorMessage != null) {
                    assert errorCode != null;
                    return new ReplSetSyncFromReply(command, warning, errorCode, errorMessage);
                }
                if (!errorCode.equals(MongoWP.ErrorCode.OK)) {
                    return new ReplSetSyncFromReply(command, warning, errorCode, args);
                }
                return new ReplSetSyncFromReply(command, prevSyncTarget, warning);
            }
        }
    }

}
