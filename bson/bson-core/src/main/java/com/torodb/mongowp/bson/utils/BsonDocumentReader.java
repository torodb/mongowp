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
package com.torodb.mongowp.bson.utils;

import com.torodb.mongowp.bson.BsonDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A class that reads documents from a <em>source</em>.
 *
 * @param <SourceT> the class of the source from this reader will read the documents
 */
public interface BsonDocumentReader<SourceT> {

  /**
   * Reads a document from the input source.
   *
   * <p>
   * A heap allocation can be specified. The document returned will comply that allocation or one
   * more premisive than that if the reader doesn't support that heap allocation.
   *
   * <p>
   * {@link AllocationType#HEAP} must be always supported.
   */
  @Nonnull
  public BsonDocument readDocument(AllocationType allocationType, SourceT source)
      throws BsonDocumentReaderException;

  public static enum AllocationType {
    /**
     * Returns documents that are totally stored on the heap.
     */
    HEAP(null),
    /**
     * Returns documents whose metadata is on the heap but heavy values reside on the underlaying
     * {@link ByteBuf}.
     */
    OFFHEAP_VALUES(HEAP),
    /**
     * Returns light documents whose data and metadata reside on the underlaying {@link ByteBuf}
     * until the entry is accesed.
     */
    OFFHEAP(OFFHEAP_VALUES);

    private final AllocationType lessRestrictive;

    private AllocationType(AllocationType previous) {
      this.lessRestrictive = previous;
    }

    @Nullable
    public AllocationType getLessRestrictive() {
      return lessRestrictive;
    }
  }
}
