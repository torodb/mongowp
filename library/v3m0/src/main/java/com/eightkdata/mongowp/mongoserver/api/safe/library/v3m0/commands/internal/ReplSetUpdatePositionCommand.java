
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetUpdatePositionCommand.ReplSetUpdatePositionArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetUpdatePositionCommand.ReplSetUpdatePositionArgument.UpdateInfo;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 *
 */
public class ReplSetUpdatePositionCommand extends AbstractCommand<ReplSetUpdatePositionArgument, Empty> {

    public static final ReplSetUpdatePositionCommand INSTANCE = new ReplSetUpdatePositionCommand();

    private static final String COMMAND_FIELD_NAME = "replSetUpdatePosition";
    private static final String UPDATE_ARRAY_FIELD_NAME = "optimes";

    private ReplSetUpdatePositionCommand() {
        super("replSetUpdatePosition");
    }

    @Override
    public Class<? extends ReplSetUpdatePositionArgument> getArgClass() {
        return ReplSetUpdatePositionArgument.class;
    }

    @Override
    public ReplSetUpdatePositionArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        if (requestDoc.containsKey("handshake")) {
            throw new IllegalArgumentException("A handshake command wrapped "
                    + "inside a replSetUpdatePosition has been recived, but it "
                    + "has been treated by the replSetUpdatePosition command");
        }
        BsonReaderTool.checkOnlyHasFields("UpdatePositionArgs", requestDoc, COMMAND_FIELD_NAME, UPDATE_ARRAY_FIELD_NAME);

        ImmutableList.Builder<UpdateInfo> updateInfo = ImmutableList.builder();
        BsonArray updateArray = BsonReaderTool.getArray(requestDoc, UPDATE_ARRAY_FIELD_NAME);
        for (BsonValue element : updateArray) {
            updateInfo.add(new UpdateInfo(element.asDocument()));
        }

        return new ReplSetUpdatePositionArgument(updateInfo.build());
    }

    @Override
    public BsonDocument marshallArg(ReplSetUpdatePositionArgument request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends Empty> getResultClass() {
        return Empty.class;
    }

    @Override
    public BsonDocument marshallResult(Empty reply) {
        return null;
    }

    @Override
    public Empty unmarshallResult(BsonDocument resultDoc) {
        return Empty.getInstance();
    }

    public static class ReplSetUpdatePositionArgument {

        private final ImmutableList<UpdateInfo> updates;

        public ReplSetUpdatePositionArgument(
                List<UpdateInfo> updates) {
            this.updates = ImmutableList.copyOf(updates);
        }

        @Nonnull
        public ImmutableList<UpdateInfo> getUpdates() throws IllegalStateException {
            return updates;
        }

        public static class UpdateInfo {
            private static final String MEMBER_RID_FIELD_NAME = "_id";
            private static final String MEMBER_CONFIG_FIELD_NAME = "config";
            private static final String OP_TIME_FIELD_NAME = "optime";
            private static final String MEMBER_ID_FIELD_NAME = "memberId";
            private static final String CONFIG_VERSION_FIELD_NAME = "cfgver";

            private final BsonObjectId rid;
            private final OpTime ts;
            private final long cfgVer;
            private final long memberId;

            public UpdateInfo(BsonObjectId rid, OpTime ts, long cfgVer, long memberId) {
                this.rid = rid;
                this.ts = ts;
                this.cfgVer = cfgVer;
                this.memberId = memberId;
            }

            public UpdateInfo(BsonDocument doc) throws BadValueException, TypesMismatchException, NoSuchKeyException {
                BsonReaderTool.checkOnlyHasFields(
                        "UpdateInfoArgs",
                        doc,
                        MEMBER_RID_FIELD_NAME,
                        MEMBER_CONFIG_FIELD_NAME,
                        OP_TIME_FIELD_NAME,
                        MEMBER_ID_FIELD_NAME,
                        CONFIG_VERSION_FIELD_NAME);
                ts = new OpTime(
                        BsonReaderTool.getInstant(doc, OP_TIME_FIELD_NAME)
                );
                cfgVer = BsonReaderTool.getLong(doc, CONFIG_VERSION_FIELD_NAME, -1);
                rid = BsonReaderTool.getObjectId(doc, MEMBER_RID_FIELD_NAME, null);
                memberId = BsonReaderTool.getLong(doc, MEMBER_ID_FIELD_NAME, -1);
            }

            public BsonObjectId getRid() {
                return rid;
            }

            public OpTime getTs() {
                return ts;
            }

            public long getCfgVer() {
                return cfgVer;
            }

            public long getMemberId() {
                return memberId;
            }
        }

    }
}
