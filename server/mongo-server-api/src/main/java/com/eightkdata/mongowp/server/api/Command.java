/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
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

    /**
     *
     * @return iff the command can change the replication state
     */
    public boolean canChangeReplicationState();

    /**
     * Returns true if {@link #marshallResult(org.bson.BsonDocument) } produces
     * documents that already contains correction fields like <em>ok</em>,
     * <em>errmsg</em> or whatever special field used by the protocol to specify
     * errors on the execution.
     *
     * If true is returned, then a non null object is returned when
     * {@linkplain #marshallResult(java.lang.Object) } is called with the same
     * argument.
     * @param r
     * @return
     */
    public boolean isReadyToReplyResult(Result r);
    
    public Class<? extends Arg> getArgClass();

    @Nonnull
    public Arg unmarshallArg(@Nonnull BsonDocument requestDoc) throws BadValueException, TypesMismatchException, NoSuchKeyException, FailedToParseException;

    @Nonnull
    public BsonDocument marshallArg(Arg request) throws MarshalException;
    
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
    public BsonDocument marshallResult(Result result) throws MarshalException ;
    
    /**
     * Two commands are equal iff their class is the same.
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj);
}
