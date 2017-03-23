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
package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.messages.request.RequestMessage;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public interface MessageDecoder<T extends RequestMessage> {

  /**
   * Decodes a message from a ByteBuf, positioned just before the body's content beginning
   *
   * @param buffer
   * @param requestBaseMessage
   * @return
   * @throws MongoException            If it was impossible to decode the message
   * @throws InvalidNamespaceException If the message expected a namespace but an invalid namespace
   *                                   is provided
   *
   */
  public T decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws MongoException,
      InvalidNamespaceException;
}
