/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.bson.BsonDocument;
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
