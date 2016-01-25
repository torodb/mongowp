
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import org.bson.BsonDocument;

/**
 *
 */
public class HandshakeReplSetUpdatePositionCommand extends HandshakeCommand {

    @Override
    public HandshakeArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {

        assert requestDoc.containsKey("handshake") : "A document with key 'handshake0 was expected";

        HandshakeArgument result = super.unmarshallArg(requestDoc.getDocument("handshake"));

        if (result.getMemberId() == null) {
            throw new NoSuchKeyException(
                    "replSetUpdatePosition handshake was missing 'member' field"
            );
        }

        return result;
    }


}
