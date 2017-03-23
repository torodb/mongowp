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
package com.torodb.mongowp.messages.request;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * This class represents a callback to clean up all BSON data that could be offheap.
 *
 * Some implementations could decide to not store documents (or part of them) on the heap. In that
 * case, they have to supply an instance of this class.
 *
 * All documents referenced on a request must be <em>alive</em> at least until this callback is
 * called.
 */
@NotThreadSafe
public interface BsonContext extends AutoCloseable {

  /**
   * @return true if documents associated with this context are still valid.
   */
  boolean isValid();

}
