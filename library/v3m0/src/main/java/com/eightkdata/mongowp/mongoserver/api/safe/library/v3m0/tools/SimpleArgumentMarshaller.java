
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

/**
 *
 */
public class SimpleArgumentMarshaller {
    private SimpleArgumentMarshaller() {}

    public static BsonDocument marshall(Command<?, ?> command) {
        return new BsonDocument(command.getCommandName(), new BsonInt32(1));
    }

}
