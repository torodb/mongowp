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


package com.eightkdata.mongowp.server.api.deprecated;

import com.eightkdata.mongowp.server.callback.MessageReplier;
import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 *
 */
public class QueryCommandProcessorCaller {
	@Nonnull private final String database;
    @Nonnull protected final MessageReplier messageReplier;

    @Inject
    public QueryCommandProcessorCaller(@Nonnull String database, @Nonnull MessageReplier messageReplier) {
    	this.database = database;
        this.messageReplier = messageReplier;
    }
    
    @Nonnull
    public String getDatabase() {
		return database;
	}
}
