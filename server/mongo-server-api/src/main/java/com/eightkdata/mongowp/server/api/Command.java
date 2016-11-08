/*
 * MongoWP - Mongo Server: API
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Command represents a MongoDB command as a function signature represents a
 * function in programming languages like C++ or Java.
 * @param <Arg>
 * @param <Result>
 */
public interface Command<Arg, Result> {
    
    public String getCommandName();

    /**
     * @return iff the command must be executed on the admin namespace
     */
    public boolean isAdminOnly();

    /**
     * @return iff can be directly executed on a slave
     */
    public boolean isSlaveOk();

    /**
     * @return iff clients can force the execution of this command on slave nodes
     * with <em>slaveOk</em> option
     */
    public boolean isSlaveOverrideOk();

    /**
     * @return iff the command execution should increment the command counter
     */
    public boolean shouldAffectCommandCounter();

    /**
     * @return iff the command can be executed while the server is in recovering state
     */
    public boolean isAllowedOnMaintenance();

    public default boolean supportsReadConcern() {
        return false;
    }

    /**
     *
     * @return iff the command can change the replication state
     */
    public boolean canChangeReplicationState();

    public Class<? extends Arg> getArgClass();

    @Nonnull
    public default Arg unmarshallArg(@Nonnull BsonDocument requestDoc) throws MongoException {
        return unmarshallArg(requestDoc, requestDoc.getFirstEntry().getKey());
    }

    @Nonnull
    public Arg unmarshallArg(@Nonnull BsonDocument requestDoc, String aliasedAs) throws MongoException;

    @Nonnull
    public BsonDocument marshallArg(Arg request, String aliasedAs) throws MarshalException;
    
    public Class<? extends Result> getResultClass();

    @Nonnull
    public Result unmarshallResult(@Nonnull BsonDocument resultDoc) throws BadValueException, TypesMismatchException, NoSuchKeyException, FailedToParseException, MongoException;

    /**
     * Translate the result to a bson document.
     *
     * The fields
     * @param result
     * @return
     * @throws com.eightkdata.mongowp.server.api.MarshalException
     * @throws MongoException
     */
    @Nullable
    public BsonDocument marshallResult(Result result) throws MarshalException;

    /**
     * Two commands are equal iff their class is the same.
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj);
}
