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

import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.messages.request.RequestMessage;
import com.eightkdata.mongowp.mongoserver.exception.InvalidMessageException;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public interface MessageDecoder<T extends RequestMessage> {
    /**
     * Decodes a message from a ByteBuf, positioned just before the body's content beginning
     * @param buffer
     * @return
     * @throws com.eightkdata.mongowp.mongoserver.exception.InvalidMessageException If the message contains invalid fields
     * that do not match with a real T message
     */
    public T decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidMessageException;
}
