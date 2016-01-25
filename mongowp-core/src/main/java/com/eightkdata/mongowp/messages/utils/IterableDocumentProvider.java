
package com.eightkdata.mongowp.messages.utils;

import com.google.common.collect.FluentIterable;
import java.util.Collections;
import org.bson.BsonDocument;

/**
 *
 */
public abstract class IterableDocumentProvider<E extends BsonDocument> extends FluentIterable<E> implements AutoCloseable {
    private static final IterableDocumentProvider<?> EMPTY
            = new SimpleIterableDocumentsProvider<>(Collections.<BsonDocument>emptyList());
    
    @SuppressWarnings("unchecked")
    public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of() {
        return (IterableDocumentProvider<E1>) EMPTY;
    }

    public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of(E1 element) {
        return new SimpleIterableDocumentsProvider<>(Collections.singleton(element));
    }
}
