
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.HandshakeCommand.HandshakeArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberConfig;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import javax.annotation.Nullable;

/**
 *
 */
public class HandshakeCommand extends AbstractCommand<HandshakeArgument, Empty>{

    public static final HandshakeCommand INSTANCE = new HandshakeCommand();

    private static final String RID_FIELD_NAME = "handshake";
    private static final String OLD_MEMBER_CONFIG_FIELD_NAME = "config";
    private static final String MEMBER_ID_FIELD_NAME = "member";
    private static final String CONFIG_FIELD_NAME = "config";

    protected HandshakeCommand() {
        super(RID_FIELD_NAME);
    }

    @Override
    public Class<? extends HandshakeArgument> getArgClass() {
        return HandshakeArgument.class;
    }

    @Override
    public HandshakeArgument unmarshallArg(BsonDocument requestDoc)
            throws BadValueException, TypesMismatchException, NoSuchKeyException {

        BsonReaderTool.checkOnlyHasFields(
                "HandshakeArgs",
                requestDoc,
                RID_FIELD_NAME,
                OLD_MEMBER_CONFIG_FIELD_NAME,
                MEMBER_ID_FIELD_NAME
        );
        
        BsonObjectId rid = BsonReaderTool.getObjectId(requestDoc, RID_FIELD_NAME);
        Long memberId;
        if (!requestDoc.containsKey(MEMBER_ID_FIELD_NAME)) {
            memberId = null;
        }
        else {
            memberId = BsonReaderTool.getLong(requestDoc, MEMBER_ID_FIELD_NAME);
        }
        BsonDocument configBson = BsonReaderTool.getDocument(
                requestDoc,
                CONFIG_FIELD_NAME,
                null
        );
        MemberConfig memberConfig = null;
        if (configBson != null) {
            memberConfig = MemberConfig.fromDocument(configBson);
        }

        return new HandshakeArgument(rid, memberId, memberConfig);
    }

    @Override
    public BsonDocument marshallArg(HandshakeArgument request) {
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

    public static class HandshakeArgument {
        private final BsonObjectId rid;
        private final Long memberId;
        /**
         * This is not used on MongoDB 3.0.0 and higher, but it is required in
         * older versions.
         */
        private final @Nullable MemberConfig config;

        public HandshakeArgument(
                BsonObjectId rid,
                @Nullable Long memberId,
                @Nullable MemberConfig config) {
            this.rid = rid;
            this.memberId = memberId;
            this.config = config;
        }

        public BsonObjectId getRid() {
            return rid;
        }

        public Long getMemberId() {
            return memberId;
        }

        public MemberConfig getConfig() {
            return config;
        }
    }

}
