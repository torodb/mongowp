
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ApplyOpsCommand.ApplyOpsArgument.Precondition;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.base.Preconditions;

/**
 *
 */
public class HandshakeReplSetUpdatePositionCommand extends HandshakeCommand {

    @Override
    public HandshakeArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {

        BsonDocument doc = BsonReaderTool.getDocument(requestDoc, "handshake");

        HandshakeArgument result = super.unmarshallArg(doc);

        if (result.getMemberId() == null) {
            throw new NoSuchKeyException(
                    "replSetUpdatePosition handshake was missing 'member' field"
            );
        }

        return result;
    }


}
