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
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.google.common.base.Preconditions;

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
