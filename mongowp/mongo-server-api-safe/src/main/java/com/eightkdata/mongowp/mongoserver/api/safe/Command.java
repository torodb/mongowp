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
package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import org.bson.BsonDocument;

/**
 * A Command represents a MongoDB command as a function signature represents a
 * function in programming languages like C++ or Java.
 * @param <Arg>
 * @param <Rep>
 */
public interface Command<Arg extends CommandArgument, Rep extends CommandReply> {
    
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
    
    public Class<? extends Arg> getArgClass();
    
    public Arg unmarshallArg(BsonDocument requestDoc) throws MongoServerException;
    
    public BsonDocument marshallArg(Arg request) throws MongoServerException, UnsupportedOperationException;
    
    public Class<? extends Rep> getReplyClass();
    
    public Rep unmarshallReply(BsonDocument replyDoc) throws MongoServerException, UnsupportedOperationException;
    
    public BsonDocument marshallReply(Rep reply) throws MongoServerException;
    
    /**
     * Two commands are equal iff their class is the same.
     * 
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj);
}
