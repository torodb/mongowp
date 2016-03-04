
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.tools.Empty;

/**
 *
 */
public class EmptyCommandArgumentMarshaller {

    private EmptyCommandArgumentMarshaller() {

    }

    public static BsonDocument marshallEmptyArgument(Command<Empty, ?> command) {
        return DefaultBsonValues.newDocument(command.getCommandName(), MongoConstants.BSON_OK);
    }

}
