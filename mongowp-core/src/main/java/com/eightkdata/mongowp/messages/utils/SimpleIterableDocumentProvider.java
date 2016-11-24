/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.messages.utils;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.google.common.collect.FluentIterable;

/**
 *
 * @param <E>
 */
class SimpleIterableDocumentProvider<E extends BsonDocument> extends IterableDocumentProvider<E> {

  private final FluentIterable<E> documents;

  SimpleIterableDocumentProvider(Iterable<E> documents) {
    this.documents = FluentIterable.from(documents);
  }

  @Override
  public FluentIterable<E> getIterable(AllocationType algorithm) {
    return documents;
  }
}
