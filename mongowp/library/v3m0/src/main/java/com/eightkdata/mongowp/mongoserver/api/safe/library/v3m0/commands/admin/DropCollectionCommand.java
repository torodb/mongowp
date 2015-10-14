
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.CollectionCommandArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.Empty;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import org.bson.BsonDocument;

/**
 *
 */
public class DropCollectionCommand extends AbstractCommand<CollectionCommandArgument, Empty> {

    public static final DropCollectionCommand INSTANCE = new DropCollectionCommand();

    private DropCollectionCommand() {
        super("dropCollection");
    }

    @Override
    public Class<? extends CollectionCommandArgument> getArgClass() {
        return CollectionCommandArgument.class;
    }

    @Override
    public CollectionCommandArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return CollectionCommandArgument.unmarshall(requestDoc, this);
    }

    @Override
    public BsonDocument marshallArg(CollectionCommandArgument request) {
        return request.marshall();
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
    public Empty unmarshallResult(BsonDocument replyDoc) {
        return Empty.getInstance();
    }

}
