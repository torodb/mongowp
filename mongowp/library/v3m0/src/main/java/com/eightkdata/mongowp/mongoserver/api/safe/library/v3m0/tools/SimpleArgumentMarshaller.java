
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

/**
 *
 */
public class SimpleArgumentMarshaller {
    private SimpleArgumentMarshaller() {}

    public static BsonDocument marshall(SimpleArgument arg) {
        return new BsonDocument(arg.getCommand().getCommandName(), new BsonInt32(1));
    }

}
