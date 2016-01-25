
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.mongoserver.api.Command;
import com.eightkdata.mongowp.mongoserver.api.tools.Empty;
import org.bson.BsonDocument;

/**
 *
 */
public class EmptyCommandArgumentMarshaller {

    private EmptyCommandArgumentMarshaller() {

    }

    public static BsonDocument marshallEmptyArgument(Command<Empty, ?> command) {
        return new BsonDocument(command.getCommandName(), MongoConstants.BSON_OK);
    }

}
