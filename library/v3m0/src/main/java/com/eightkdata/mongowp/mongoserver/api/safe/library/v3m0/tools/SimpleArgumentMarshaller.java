
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.server.api.Command;

/**
 *
 */
public class SimpleArgumentMarshaller {
    private SimpleArgumentMarshaller() {}

    public static BsonDocument marshall(Command<?, ?> command) {
        return DefaultBsonValues.newDocument(command.getCommandName(), DefaultBsonValues.newInt(1));
    }

}
