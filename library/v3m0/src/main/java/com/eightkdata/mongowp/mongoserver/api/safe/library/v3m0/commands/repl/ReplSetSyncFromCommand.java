package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.HostAndPortField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetSyncFromCommand.ReplSetSyncFromReply;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.net.HostAndPort;

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
        return DefaultBsonValues.newDocument(getCommandName(), DefaultBsonValues.newString(request.toString()));
    }

    @Override
    public Class<? extends ReplSetSyncFromReply> getResultClass() {
        return ReplSetSyncFromReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetSyncFromReply reply) {
        return reply.marshall();
    }

    @Override
    public ReplSetSyncFromReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Immutable
    public static class ReplSetSyncFromReply {
    	private static final HostAndPortField PREV_SYNC_TARGET_FIELD = new HostAndPortField("prevSyncTarget"); 
    	private static final HostAndPortField SYNC_FROM_REQUESTED_FIELD = new HostAndPortField("syncFromRequested"); 
    	private static final StringField WARNING_FIELD = new StringField("warning"); 

        @Nullable
        private final HostAndPort prevSyncTarget;
        @Nullable
        private final HostAndPort syncFromRequested;
        @Nullable
        private final String warning;

        public ReplSetSyncFromReply(
                @Nullable HostAndPort prevSyncTarget,
                @Nullable HostAndPort syncFromRequested,
                @Nullable String warning) {
            this.prevSyncTarget = prevSyncTarget;
            this.syncFromRequested = syncFromRequested;
            this.warning = warning;
        }

        @Nullable
        public HostAndPort getPrevSyncTarget() {
            return prevSyncTarget;
        }

        @Nullable
        public String getWarning() {
            return warning;
        }

        @Nullable
        public HostAndPort getSyncFromRequested() {
            return syncFromRequested;
        }
        
        private BsonDocument marshall() {
            BsonDocumentBuilder result = new BsonDocumentBuilder();
            if (prevSyncTarget != null) {
                result.append(PREV_SYNC_TARGET_FIELD, prevSyncTarget);
            }

            if (warning != null) {
                result.append(WARNING_FIELD, warning);
            }

            if (syncFromRequested != null) {
                result.append(SYNC_FROM_REQUESTED_FIELD, syncFromRequested);
            }

            return result.build();
        }
    }

}
