
package com.eightkdata.mongowp.mongoserver.api.tools;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import java.text.MessageFormat;
import java.util.Collections;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

/**
 *
 */
public class ReplyBuilder {

    private ReplyBuilder() {}

    public static ReplyMessage createStandardErrorReply(int requestId, MongoWP.ErrorCode errorCode, Object... args) {
        return createStandardErrorReplyWithMessage(
                requestId,
                errorCode,
                MessageFormat.format(errorCode.getErrorMessage(), args)
        );
    }

    public static ReplyMessage createStandardErrorReplyWithMessage(int requestId, MongoWP.ErrorCode errorCode, String errorMessage) {

        BsonDocument errorDocument = new BsonDocument();
        errorDocument.put("ok", MongoWP.BSON_KO);
        errorDocument.put("errmsg", new BsonString(errorMessage));
        errorDocument.put("code", new BsonInt32(errorCode.getErrorCode()));

        return new ReplyMessage(requestId, 0, 0, Collections.singletonList(errorDocument));
    }

}
