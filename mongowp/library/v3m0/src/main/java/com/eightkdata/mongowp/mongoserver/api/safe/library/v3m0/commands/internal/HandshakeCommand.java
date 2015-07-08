
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.HandshakeCommand.HandshakeArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.types.ObjectId;

/**
 *
 */
public class HandshakeCommand extends AbstractCommand<HandshakeArgument, SimpleReply>{

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
    public HandshakeArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {

        BsonReaderTool.checkOnlyHasFields(
                "HandshakeArgs",
                requestDoc,
                RID_FIELD_NAME,
                OLD_MEMBER_CONFIG_FIELD_NAME,
                MEMBER_ID_FIELD_NAME
        );
        
        ObjectId rid = BsonReaderTool.getObjectId(requestDoc, RID_FIELD_NAME);
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

        return new HandshakeArgument(this, rid, memberId, memberConfig);
    }

    @Override
    public Class<? extends SimpleReply> getReplyClass() {
        return SimpleReply.class;
    }

    @Override
    public BsonDocument marshallReply(SimpleReply reply) throws
            MongoServerException {
        return SimpleReplyMarshaller.marshall(reply);
    }

    public static class HandshakeArgument extends SimpleArgument {
        private final ObjectId rid;
        private final Long memberId;
        /**
         * This is not used on MongoDB 3.0.0 and higher, but it is required in
         * older versions.
         */
        private final @Nullable MemberConfig config;

        public HandshakeArgument(
                HandshakeCommand command,
                ObjectId rid,
                Long memberId,
                @Nullable MemberConfig config) {
            super(command);
            this.rid = rid;
            this.memberId = memberId;
            this.config = config;
        }

        public ObjectId getRid() {
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
