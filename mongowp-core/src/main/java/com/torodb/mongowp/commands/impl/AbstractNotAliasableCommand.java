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

import com.google.common.base.Preconditions;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.commands.MarshalException;
import com.torodb.mongowp.exceptions.MongoException;

/**
 *
 */
public abstract class AbstractNotAliasableCommand<A, R> extends AbstractCommand<A, R> {

  protected AbstractNotAliasableCommand(String commandName) {
    super(commandName);
  }

  protected boolean supportsAlias() {
    return false;
  }

  @Override
  public abstract A unmarshallArg(BsonDocument requestDoc) throws MongoException;

  @Override
  public final A unmarshallArg(BsonDocument requestDoc, String aliasedAs) throws
      MongoException {
    Preconditions.checkArgument(aliasedAs.equalsIgnoreCase(getCommandName()),
        "The command %s cannot be aliased, but %s has be recived as "
        + "alias", getCommandName(), aliasedAs);
    return unmarshallArg(requestDoc);
  }

  protected abstract BsonDocument marshallArg(A request) throws MarshalException;

  @Override
  public final BsonDocument marshallArg(A request, String aliasedAs) throws
      MarshalException {
    Preconditions.checkArgument(aliasedAs.equals(getCommandName()),
        "The command %s cannot be aliased, but %s has be recived as "
        + "alias", getCommandName(), aliasedAs);
    return marshallArg(request);
  }
}
