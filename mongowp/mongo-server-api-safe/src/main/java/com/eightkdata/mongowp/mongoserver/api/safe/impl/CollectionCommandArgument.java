
package com.eightkdata.mongowp.mongoserver.api.safe.impl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonInvalidOperationException;
import org.bson.BsonString;

/**
 *
 */
public class CollectionCommandArgument {

    private final Command command;
    private final String collection;

    public CollectionCommandArgument(String collection, Command command) {
        this.command = command;
        this.collection = collection;
    }

    @Nullable
    public String getCollection() {
        return collection;
    }

    public Command getCommand() {
        return command;
    }

    public static CollectionCommandArgument unmarshall(
                BsonDocument requestDoc,
                Command command)
                throws BadValueException {
        try {
            return new CollectionCommandArgument(
                    requestDoc.getString(command.getCommandName()).getValue(),
                    command
            );
        } catch (BsonInvalidOperationException ex) {
            throw new BadValueException(
                    "The request document does not contain a string value for "
                            + "the key "+ command.getCommandName()
            );
        }
    }

    public BsonDocument marshall() {
        return new BsonDocument().append(getCommand().getCommandName(), new BsonString(collection));
    }


}
