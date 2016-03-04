
package com.eightkdata.mongowp.messages.utils;

import com.eightkdata.mongowp.annotations.Ethereal;
import com.eightkdata.mongowp.annotations.Material;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.google.common.collect.FluentIterable;
import java.util.Collections;

/**
 * This class is a factory that creates stream like iterables of type BsonDocument.
 *
 * The generated iterables can be configured to use different {@linkplain AllocationType heap allocations}.
 *
 * @param <E>
 */
public abstract class IterableDocumentProvider<E extends BsonDocument> {
    private static final IterableDocumentProvider<?> EMPTY
            = new SimpleIterableDocumentProvider<>(Collections.<BsonDocument>emptyList());

    @Ethereal("my-own-context")
    public abstract FluentIterable<E> getIterable(BsonDocumentReader.AllocationType algorithm);

    @Material
    @SuppressWarnings("unchecked")
    public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of() {
        return (IterableDocumentProvider<E1>) EMPTY;
    }

    @Ethereal("argument-context")
    public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of(@Ethereal("whatever") E1 element) {
        return new SimpleIterableDocumentProvider<>(Collections.singleton(element));
    }

    @Ethereal("argument-context")
    public static <E1 extends BsonDocument> IterableDocumentProvider<E1> of(@Ethereal("whatever") Iterable<E1> elements) {
        return new SimpleIterableDocumentProvider<>(elements);
    }
}
