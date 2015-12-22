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


package com.eightkdata.mongowp.mongoserver.decoder;

import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnegative;
import javax.inject.Singleton;

import com.eightkdata.mongowp.messages.request.KillCursorsMessage;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.mongoserver.exception.InvalidMessageException;

/**
 *
 */
@Singleton
public class KillCursorsMessageDecoder implements MessageDecoder<KillCursorsMessage> {
    @Override
    public @Nonnegative
    KillCursorsMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidMessageException {
    	buffer.skipBytes(4);
        int numberOfCursors = buffer.readInt();
        long[] cursorIds = new long[numberOfCursors];
        for (int index = 0; index < numberOfCursors; index++) {
        	cursorIds[index] = buffer.readLong();
        }

        return new KillCursorsMessage(
                requestBaseMessage, numberOfCursors, cursorIds
        );
    }
}
