
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import javax.annotation.Nullable;

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
            throws BadValueException, TypesMismatchException, NoSuchKeyException {
        return new CollectionCommandArgument(
                BsonReaderTool.getString(requestDoc, command.getCommandName()),
                command
        );
    }

    public BsonDocument marshall() {
        return DefaultBsonValues.newDocument(
                getCommand().getCommandName(),
                DefaultBsonValues.newString(collection)
        );
    }


}
