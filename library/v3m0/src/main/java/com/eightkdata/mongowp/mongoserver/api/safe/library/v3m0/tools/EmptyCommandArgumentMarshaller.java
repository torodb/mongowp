
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.Empty;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import org.bson.BsonDocument;

/**
 *
 */
public class EmptyCommandArgumentMarshaller {

    private EmptyCommandArgumentMarshaller() {

    }

    public static BsonDocument marshallEmptyArgument(Command<Empty, ?> command) {
        return new BsonDocument(command.getCommandName(), MongoWP.BSON_OK);
    }

}
