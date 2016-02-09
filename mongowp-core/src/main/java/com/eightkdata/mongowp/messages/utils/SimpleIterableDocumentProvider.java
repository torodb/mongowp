
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
