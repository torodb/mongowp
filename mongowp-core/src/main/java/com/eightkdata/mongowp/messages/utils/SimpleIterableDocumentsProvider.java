
package com.eightkdata.mongowp.messages.utils;

import java.util.Iterator;
import org.bson.BsonDocument;

/**
 *
 */
public class SimpleIterableDocumentsProvider<E extends BsonDocument> extends IterableDocumentProvider<E> {

    private final Iterable<E> documents;

    public SimpleIterableDocumentsProvider(Iterable<E> documents) {
        this.documents = documents;
    }

    @Override
    public Iterator<E> iterator() {
        return documents.iterator();
    }

    @Override
    public void close() {
    }
}
