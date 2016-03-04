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


package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.messages.request.RequestOpCode;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import static java.lang.Thread.State.*;

/**
 *
 */
@Singleton
public class MessageDecoderLocator {

    private final EnumMap<RequestOpCode, MessageDecoder<?>> decoderMap;

    @Inject
    public MessageDecoderLocator(
            DeleteMessageDecoder deleteDecoder,
            GetMoreMessageDecoder getMoreDecoder,
            InsertMessageDecoder insertDecoder,
            KillCursorsMessageDecoder killCursorsDecoder,
            QueryMessageDecoder queryDecoder,
            UpdateMessageDecoder updateDecoder
    ) {
        this.decoderMap = new EnumMap<>(RequestOpCode.class);

        decoderMap.put(RequestOpCode.OP_DELETE, deleteDecoder);
        decoderMap.put(RequestOpCode.OP_GET_MORE, getMoreDecoder);
        decoderMap.put(RequestOpCode.OP_INSERT, insertDecoder);
        decoderMap.put(RequestOpCode.OP_KILL_CURSORS, killCursorsDecoder);
        decoderMap.put(RequestOpCode.OP_QUERY, queryDecoder);
        decoderMap.put(RequestOpCode.OP_UPDATE, updateDecoder);

        checkDecoderMap(decoderMap);
    }

    public MessageDecoder<?> getByOpCode(@Nonnull RequestOpCode requestOpCode) {
        return decoderMap.get(requestOpCode);
    }

    private static void checkDecoderMap(Map<RequestOpCode, MessageDecoder<?>> decoderMap) {
        Set<RequestOpCode> opsWithoutDecoder = EnumSet.of(RequestOpCode.OP_MSG, RequestOpCode.RESERVED);
        for (RequestOpCode value : RequestOpCode.values()) {
            if (opsWithoutDecoder.contains(value)) {
                continue;
            }
            if (!decoderMap.containsKey(value)) {
                throw new AssertionError("There is no decoder for operation " + value);
            }
        }
    }
}
