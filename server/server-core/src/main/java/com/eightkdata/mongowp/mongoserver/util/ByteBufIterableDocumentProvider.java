
package com.eightkdata.mongowp.mongoserver.util;

import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.bson.BsonBinaryReader;
import org.bson.BsonDocument;
import org.bson.BsonReader;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.DecoderContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public final class ByteBufIterableDocumentProvider extends IterableDocumentProvider<BsonDocument> {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ByteBufIterableDocumentProvider.class);
    private boolean closed = false;
    private final ByteBuf byteBuf;

    /**
     *
     * @param byteBuf must exactly contain a document list. It will be
     * {@linkplain ByteBuf#retain() retain} by this object until it is closed
     */
    public ByteBufIterableDocumentProvider(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public Iterator<BsonDocument> iterator() {
        return new MyIterator(byteBuf.duplicate());
    }

    /**
     * {@linkplain ByteBuf#release() Releases} the underlaying ByteBuf.
     *
     * After this method is called, this class cannot be iterated and already
     * created iterators will be unusable.
     */
    @Override
    public void close() {
        if (!closed) {
            closed = true;
            byteBuf.release();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!closed) {
            close();
            LOGGER.warn("{} has not been properly closed!", this);
        }
    }

    private static final class MyIterator implements Iterator<BsonDocument> {

        private static final BsonDocumentCodec BSON_CODEC = new BsonDocumentCodec();
        private final DecoderContext context;
        private final InternBsonInputDelegator inputAdaptor;
        private final ByteBuf buff;

        private MyIterator(ByteBuf buffer) {
            this.buff = buffer;

            inputAdaptor = new InternBsonInputDelegator(
                    new ByteBufBsonInputAdaptor(buffer)
            );
            

            context = DecoderContext.builder().build();
        }

        @Override
        public boolean hasNext() {
            return buff.isReadable();
        }

        @Override
        public BsonDocument next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            BsonReader reader = new BsonBinaryReader(inputAdaptor);
            return BSON_CODEC.decode(reader, context);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported");
        }

    }


}
