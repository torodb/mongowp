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
package com.torodb.mongowp.commands.pojos;

import com.google.common.base.Preconditions;
import com.torodb.mongowp.bson.annotations.NotMutable;
import com.torodb.mongowp.commands.pojos.MongoCursor.Batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class CollectionBatch<T> implements Batch<T> {

  private final List<T> list;
  private final Iterator<? extends T> it;
  private final int batchSize;
  private final long fetchTime;
  private boolean closed = false;

  public CollectionBatch(@NotMutable Collection<? extends T> collection, long fetchTime) {
    if (collection instanceof List) {
      this.list = Collections.unmodifiableList((List<? extends T>) collection);
    } else {
      this.list = Collections.unmodifiableList(new ArrayList<>(collection));
    }
    this.it = collection.iterator();
    this.batchSize = collection.size();
    this.fetchTime = fetchTime;
  }

  @Override
  public T next() {
    Preconditions.checkState(!closed, "This batch has been closed");
    return it.next();
  }

  @Override
  public boolean hasNext() {
    Preconditions.checkState(!closed, "This batch has been closed");
    return it.hasNext();
  }

  @Override
  public int getBatchSize() {
    return batchSize;
  }

  @Override
  public long getFetchTime() {
    return fetchTime;
  }

  @Override
  public List<T> asList() {
    Preconditions.checkState(!closed, "This batch has been closed");
    return list;
  }

  @Override
  public void close() {
    if (!closed) {
      closed = true;
    }
  }

}
