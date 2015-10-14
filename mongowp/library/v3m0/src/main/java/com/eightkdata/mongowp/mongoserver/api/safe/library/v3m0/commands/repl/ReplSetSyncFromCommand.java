package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetSyncFromCommand.ReplSetSyncFromReply;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.google.common.net.HostAndPort;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 */
public class ReplSetSyncFromCommand extends AbstractCommand<HostAndPort, ReplSetSyncFromReply>{

    public static final ReplSetSyncFromCommand INSTANCE = new ReplSetSyncFromCommand();

    private ReplSetSyncFromCommand() {
        super("replSetSyncFrom");
    }

    @Override
    public Class<? extends HostAndPort> getArgClass() {
        return HostAndPort.class;
    }

    @Override
    public HostAndPort unmarshallArg(BsonDocument requestDoc) throws TypesMismatchException {
        return BsonReaderTool.getHostAndPort(
                requestDoc,
                getCommandName(),
                null
        );
    }

    @Override
    public BsonDocument marshallArg(HostAndPort request) {
        return new BsonDocument(getCommandName(), new BsonString(request.toString()));
    }

    @Override
    public Class<? extends ReplSetSyncFromReply> getResultClass() {
        return ReplSetSyncFromReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetSyncFromReply reply) {
        BsonDocument result = new BsonDocument();
        if (reply.getPrevSyncTarget() != null) {
            result.append("prevSyncTarget", new BsonString(reply.getPrevSyncTarget().toString()));
        }

        if (reply.getWarning() != null) {
            result.append("warning", new BsonString(reply.getWarning()));
        }

        return result;
    }

    @Override
    public ReplSetSyncFromReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Immutable
    public static class ReplSetSyncFromReply {

        @Nullable
        private final HostAndPort prevSyncTarget;
        @Nullable
        private final String warning;

        public ReplSetSyncFromReply(
                @Nonnull HostAndPort prevSyncTarget,
                @Nullable String warning) {
            this.prevSyncTarget = prevSyncTarget;
            this.warning = warning;
        }

        public HostAndPort getPrevSyncTarget() {
            return prevSyncTarget;
        }

        public String getWarning() {
            return warning;
        }
    }

}
