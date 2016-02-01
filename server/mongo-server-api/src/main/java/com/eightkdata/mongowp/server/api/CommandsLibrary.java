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
import java.util.Set;
import javax.annotation.Nullable;

/**
 *
 */
public interface CommandsLibrary {

    /**
     * A brief description of the version supported by this library.
     *
     * Examples: MongoDB 3.0
     * @return
     */
    public String getSupportedVersion();

    /**
     *
     * @return a set with the supported commands or null if the supported commands
     * are not known
     */
    @Nullable
    public Set<Command> getSupportedCommands();

    @Nullable
    public Command find(BsonDocument requestDocument) ;
}
