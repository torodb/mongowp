
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.*;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.server.api.tools.Empty;

/**
 *
 */
public class DropDatabaseCommand extends AbstractCommand<Empty, Empty> {

    public static final DropDatabaseCommand INSTANCE = new DropDatabaseCommand();

    private DropDatabaseCommand() {
        super("dropDatabase");
    }

    @Override
    public Class<? extends Empty> getArgClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallArg(BsonDocument requestDoc) throws BadValueException,
            TypesMismatchException, NoSuchKeyException, FailedToParseException {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallArg(Empty request) throws MarshalException {
        return EmptyCommandArgumentMarshaller.marshallEmptyArgument(this);
    }

    @Override
    public Class<? extends Empty> getResultClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallResult(BsonDocument resultDoc) throws
            BadValueException, TypesMismatchException, NoSuchKeyException,
            FailedToParseException, MongoException {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallResult(Empty result) throws MarshalException {
        return null;
    }

}
