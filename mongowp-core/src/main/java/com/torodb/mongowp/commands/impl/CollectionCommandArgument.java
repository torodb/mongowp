/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.commands.impl;

import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.utils.DefaultBsonValues;
import com.torodb.mongowp.commands.Command;
import com.torodb.mongowp.exceptions.BadValueException;
import com.torodb.mongowp.exceptions.NoSuchKeyException;
import com.torodb.mongowp.exceptions.TypesMismatchException;
import com.torodb.mongowp.utils.BsonReaderTool;

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
