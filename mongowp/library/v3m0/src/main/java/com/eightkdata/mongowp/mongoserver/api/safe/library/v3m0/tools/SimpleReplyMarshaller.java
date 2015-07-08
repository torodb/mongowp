
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import org.bson.BsonDocument;

/**
 *
 */
public class SimpleReplyMarshaller {
    private static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");
    private static final BsonField<Double> OK_FIELD = BsonField.create("ok");

    private SimpleReplyMarshaller() {}


    public static BsonDocument marshall(SimpleReply reply) {
        BsonDocumentBuilder builder = new BsonDocumentBuilder();

        marshall(reply, builder);
        return builder.build();
    }

    public static void marshall(SimpleReply reply, BsonDocumentBuilder builder) {
        String finalErrorMessage = reply.getErrorMessage();
        if (finalErrorMessage != null) {
            builder.append(ERR_MSG_FIELD, finalErrorMessage);
            builder.append(OK_FIELD, MongoWP.KO);
        }
        else {
            builder.append(OK_FIELD, MongoWP.OK);
        }
    }

}
