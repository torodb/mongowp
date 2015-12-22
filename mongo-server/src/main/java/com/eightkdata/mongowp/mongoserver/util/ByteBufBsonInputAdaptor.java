
package com.eightkdata.mongowp.mongoserver.util;

import com.google.common.base.Charsets;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;
import org.bson.io.BsonInput;
import org.bson.types.ObjectId;

/**
 *
 */
@SuppressFBWarnings(
        value="RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
        justification = "It seems FindBugs considers ByteBuf methods are not side effect"
)
public class ByteBufBsonInputAdaptor implements BsonInput {
    private static final Charset UTF8_CHARSET = Charsets.UTF_8;

    
    private final ByteBuf buffer;
    private boolean closed;
    private int mark = -1;

    public ByteBufBsonInputAdaptor(ByteBuf byteBuf) {
        this.buffer = byteBuf;
        closed = false;
    }

    @Override
    public int getPosition() {
        return buffer.readerIndex();
    }

    @Override
    public byte readByte() {
        ensureOpen();
        return buffer.readByte();
    }

    @Override
    public void readBytes(byte[] bytes) {
        ensureOpen();
        buffer.readBytes(bytes);
    }

    @Override
    public void readBytes(byte[] bytes, int offset, int length) {
        ensureOpen();
        buffer.readBytes(bytes, offset, length);
    }

    @Override
    public long readInt64() {
        ensureOpen();
        return buffer.readLong();
    }

    @Override
    public double readDouble() {
        ensureOpen();
        return buffer.readDouble();
    }

    @Override
    public int readInt32() {
        ensureOpen();
        return buffer.readInt();
    }

    @Override
    public String readString() {
        ensureOpen();
        int size = readInt32();
        byte[] bytes = new byte[size];
        readBytes(bytes);
        return new String(bytes, 0, size - 1, UTF8_CHARSET);
    }

    @Override
    public ObjectId readObjectId() {
        ensureOpen();
        byte[] bytes = new byte[12];
        readBytes(bytes);
        return new ObjectId(bytes);
    }

    @Override
    public String readCString() {
        ensureOpen();

        int strSize = buffer.bytesBefore((byte) 0);
        if (strSize < 0 ) {
            throw new AssertionError("Expected a CString but no '0' character was found");
        }
        byte[] bytes = new byte[strSize];
        readBytes(bytes);
        readByte();  // read the trailing null byte

        return new String(bytes, UTF8_CHARSET);
    }

    @Override
    public void skipCString() {
        int nextNull = buffer.indexOf(buffer.readerIndex(), buffer.capacity(), (byte) 0);
        if (nextNull == -1) {
            throw new AssertionError("Expected a CString but no '0' character found");
        }
        buffer.readerIndex(nextNull + 1);
    }

    @Override
    public void skip(int numBytes) {
        ensureOpen();
        buffer.readerIndex(buffer.readerIndex() + numBytes);
    }

    @Override
    public void mark(int readLimit) {
        ensureOpen();
        mark = buffer.readerIndex();
    }

    @Override
    public void reset() {
        ensureOpen();
        if (mark == -1) {
            throw new IllegalStateException("Mark not set");
        }
        buffer.readerIndex(mark);
    }

    @Override
    public boolean hasRemaining() {
        ensureOpen();
        return buffer.readableBytes() > 0;
    }

    @Override
    public void close() {
        closed = true;
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("Input is closed");
        }
    }

}
