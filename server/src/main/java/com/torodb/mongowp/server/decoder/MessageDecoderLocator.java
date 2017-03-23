/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.server.decoder;

import com.torodb.mongowp.messages.request.RequestOpCode;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;

/**
 *
 */
@ThreadSafe
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
