
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.WhatsMyUriCommand.WhatsMyUriReply;
import com.eightkdata.mongowp.server.api.impl.AbstractNotAliasableCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;

/**
 *
 */
public class WhatsMyUriCommand extends AbstractNotAliasableCommand<Empty, WhatsMyUriReply>{

    public static final WhatsMyUriCommand INSTANCE = new WhatsMyUriCommand();

    private WhatsMyUriCommand() {
        super("whatsmyuri");
    }

    @Override
    public Class<? extends Empty> getArgClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallArg(Empty request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends WhatsMyUriReply> getResultClass() {
        return WhatsMyUriReply.class;
    }

    @Override
    public BsonDocument marshallResult(WhatsMyUriReply reply) {

        return new BsonDocumentBuilder(2)
                .append(WhatsMyUriReply.YOU_FIELD, reply.getHost() + ":" + reply.getPort())
                .build();
    }

    @Override
    public WhatsMyUriReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    public static class WhatsMyUriReply {

        private static final StringField YOU_FIELD = new StringField("you");

        private final String host;
        private final int port;

        public WhatsMyUriReply(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }

}
