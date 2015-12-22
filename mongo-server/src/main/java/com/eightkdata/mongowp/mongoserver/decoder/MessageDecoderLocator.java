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

import java.util.EnumMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.eightkdata.mongowp.messages.request.RequestOpCode;

/**
 *
 */
@NotThreadSafe
public enum MessageDecoderLocator {
    OP_QUERY(RequestOpCode.OP_QUERY, new QueryMessageDecoder()),
    OP_GET_MORE(RequestOpCode.OP_GET_MORE, new GetMoreMessageDecoder()),
    OP_KILL_CURSORS(RequestOpCode.OP_KILL_CURSORS, new KillCursorsMessageDecoder()),
    OP_INSERT(RequestOpCode.OP_INSERT, new InsertMessageDecoder()),
    OP_UPDATE(RequestOpCode.OP_UPDATE, new UpdateMessageDecoder()),
    OP_DELETE(RequestOpCode.OP_DELETE, new DeleteMessageDecoder());

    @Nonnull private RequestOpCode opCode;
    @Nonnull private MessageDecoder<?> messageDecoder;

    private MessageDecoderLocator(@Nonnull RequestOpCode opCode, @Nonnull MessageDecoder<?> messageDecoder) {
        this.opCode = opCode;
        this.messageDecoder = messageDecoder;
    }

    private static EnumMap<RequestOpCode, MessageDecoder<?>> OP_CODES_MAP = new EnumMap<RequestOpCode, MessageDecoder<?>>(
            RequestOpCode.class
    );
    static {
        for(MessageDecoderLocator value : values()) {
            OP_CODES_MAP.put(value.opCode, value.messageDecoder);
        }
    }

    public static MessageDecoder<?> getByOpCode(@Nonnull RequestOpCode requestOpCode) {
        return OP_CODES_MAP.get(requestOpCode);
    }
}
