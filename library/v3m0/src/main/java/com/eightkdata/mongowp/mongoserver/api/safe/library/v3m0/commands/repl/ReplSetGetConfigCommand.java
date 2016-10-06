
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonReaderTool;

/**
 * Returns the current replica set configuration.
 */
public class ReplSetGetConfigCommand extends AbstractCommand<Empty, ReplicaSetConfig> {

    public static final ReplSetGetConfigCommand INSTANCE = new ReplSetGetConfigCommand();
    private static final DocField CONFIG_FIELD = new DocField("config");

    private ReplSetGetConfigCommand() {
        super("replSetGetConfig");
    }

    @Override
    public Class<? extends Empty> getArgClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallArg(BsonDocument requestDoc) {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallArg(Empty request) {
        return EmptyCommandArgumentMarshaller.marshallEmptyArgument(this);
    }

    @Override
    public Class<? extends ReplicaSetConfig> getResultClass() {
        return ReplicaSetConfig.class;
    }

    @Override
    public BsonDocument marshallResult(ReplicaSetConfig reply) {
        return DefaultBsonValues.newDocument("config", reply.toBSON());
    }

    @Override
    public ReplicaSetConfig unmarshallResult(BsonDocument resultDoc) throws
            MongoException, UnsupportedOperationException {
        if (!resultDoc.get("ok").equals(MongoConstants.BSON_OK)) {
            throw new BadValueException("It is not defined how to parse errors "
                    + "from " + getCommandName());
        }
        return ReplicaSetConfig.fromDocument(
                BsonReaderTool.getDocument(resultDoc, CONFIG_FIELD)
        );
    }

}
