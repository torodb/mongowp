/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mongo-server-api. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.google.common.base.Preconditions;

/**
 *
 */
public abstract class AbstractNotAliasableCommand<Arg, Result> extends AbstractCommand<Arg, Result> {

    protected AbstractNotAliasableCommand(String commandName) {
        super(commandName);
    }

    protected boolean supportsAlias() {
        return false;
    }

    @Override
    public abstract Arg unmarshallArg(BsonDocument requestDoc) throws MongoException;

    protected abstract BsonDocument marshallArg(Arg request) throws MarshalException;

    @Override
    public final Arg unmarshallArg(BsonDocument requestDoc, String aliasedAs) throws
            MongoException {
        Preconditions.checkArgument(aliasedAs.equalsIgnoreCase(getCommandName()),
                "The command %s cannot be aliased, but %s has be recived as "
                        + "alias", getCommandName(), aliasedAs);
        return unmarshallArg(requestDoc);
    }

    @Override
    public final BsonDocument marshallArg(Arg request, String aliasedAs) throws
            MarshalException {
        Preconditions.checkArgument(aliasedAs.equals(getCommandName()),
                "The command %s cannot be aliased, but %s has be recived as "
                        + "alias", getCommandName(), aliasedAs);
        return marshallArg(request);
    }
}
