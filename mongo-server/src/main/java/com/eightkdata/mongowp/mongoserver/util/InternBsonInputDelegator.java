
package com.eightkdata.mongowp.mongoserver.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.bson.io.BsonInput;
import org.bson.types.ObjectId;

/**
 * This {@link BsonInput} delegates on another one but
 * {@linkplain String#intern() interns} all generated strings.
 */
public class InternBsonInputDelegator implements BsonInput {

//    private static final LoadingCache<String, String> cache = CacheBuilder.newBuilder()
//            .initialCapacity(100_000)
//            .maximumWeight(100_000 * 80)
//            .weigher(new Weigher<String, String>() {
//                @Override
//                public int weigh(String key, String value) {
//                    return key.length();
//                }
//            })
//            .build(new CacheLoader<String, String>() {
//                @Override
//                public String load(String key) {
//                    return key;
//                }
//            }
//            );
    private final BsonInput delegate;

    public InternBsonInputDelegator(BsonInput delegate) {
        this.delegate = delegate;
    }

    /**
     * A predicate that decides if a string should be interned or not.
     *
     * There are several valid heuristics: key fields (which are read with
     * {@link #readCString()}) are good candidates to be interned, as they
     * tend to be repeated. But some string values are used as enum literals,
     * so they are usually repeated.
     *
     * The chosen heuristic is that all not too long keys are interned.
     * @param str
     * @return
     */
    private boolean isInternable(String str, boolean isKey) {
        if (isKey) {
            return str.length() < 80;
        }
        else {
            return false;
        }
    }

    @Override
    public int getPosition() {
        return delegate.getPosition();
    }

    @Override
    public byte readByte() {
        return delegate.readByte();
    }

    @Override
    public void readBytes(byte[] bytes) {
        delegate.readBytes(bytes);
    }

    @Override
    public void readBytes(byte[] bytes, int offset, int length) {
        delegate.readBytes(bytes, offset, length);
    }

    @Override
    public long readInt64() {
        return delegate.readInt64();
    }

    @Override
    public double readDouble() {
        return delegate.readDouble();
    }

    @Override
    public int readInt32() {
        return delegate.readInt32();
    }

    @Override
    public String readString() {
        String originalString = delegate.readString();
        String result;
        if (isInternable(originalString, false)) {
//            result = cache.getUnchecked(originalString);
            result = originalString.intern();
        }
        else {
            result = originalString;
        }
        return result;
    }

    @Override
    public ObjectId readObjectId() {
        return delegate.readObjectId();
    }

    @Override
    public String readCString() {
        String originalString = delegate.readCString();
        String result;
        if (isInternable(originalString, true)) {
//            result = cache.getUnchecked(originalString);
            result = originalString.intern();
        }
        else {
            result = originalString;
        }
        return result;
    }

    @Override
    public void skipCString() {
        delegate.skipCString();
    }

    @Override
    public void skip(int numBytes) {
        delegate.skip(numBytes);
    }

    @Override
    public void mark(int readLimit) {
        delegate.mark(readLimit);
    }

    @Override
    public void reset() {
        delegate.reset();
    }

    @Override
    public boolean hasRemaining() {
        return delegate.hasRemaining();
    }

    @Override
    public void close() {
        delegate.close();
    }

}
