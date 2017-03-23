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
import com.eightkdata.mongowp.messages.request.RequestMessage;

import javax.annotation.Nonnull;

/**
 *
 */
public abstract class AbstractMessageDecoder<T extends RequestMessage>
    implements MessageDecoder<T> {

  @Nonnull
  protected String getDatabase(String namespace) throws InvalidNamespaceException {
    int firstDotIndex = getAndCheckFirstDot(namespace);
    if (firstDotIndex == namespace.length()) { //if there is no dot
      return namespace; //then all namespace is the database
    }
    return namespace.substring(0, getAndCheckFirstDot(namespace));
  }

  @Nonnull
  protected String getCollection(String namespace) throws InvalidNamespaceException {
    int firstDotIndex = getAndCheckFirstDot(namespace);
    if (firstDotIndex == namespace.length()) { //if there is no dot
      throw new InvalidNamespaceException( //then there throw InvalidNamespaceException
          namespace,
          "Does not have collection part"
      );
    }
    return namespace.substring(firstDotIndex + 1);
  }

  private int getAndCheckFirstDot(String namespace) throws InvalidNamespaceException {
    int firstDot = namespace.indexOf('.');
    if (firstDot == 0) {
      throw new InvalidNamespaceException(namespace, "The first character shall not be a dot");
    }
    if (firstDot < 0) {
      return namespace.length();
    }
    if (firstDot == namespace.length() - 1) {
      throw new InvalidNamespaceException(namespace, "The last character shall not be the first "
          + "dot");
    }
    return firstDot;
  }
}
