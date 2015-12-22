
package com.eightkdata.mongowp.mongoserver.util;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import org.bson.BsonSerializationException;
import org.bson.io.BsonOutput;
import org.bson.types.ObjectId;

import static java.lang.String.format;

/**
 *
 */
@SuppressFBWarnings(
        value="RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
        justification = "It seems FindBugs considers ByteBuf methods are not side effect"
)
public class ByteBufBsonOutputAdaptor implements BsonOutput {

    private final ByteBuf buffer;
    private boolean closed = false;

    public ByteBufBsonOutputAdaptor(ByteBuf buffer) {
        this.buffer = buffer;
    }

    @Override
    public int getPosition() {
        ensureOpen();
        return buffer.writerIndex();
    }

    @Override
    public int getSize() {
        ensureOpen();
        return buffer.writerIndex();
    }

    @Override
    public void truncateToPosition(int newPosition) {
        ensureOpen();
        if (newPosition > buffer.writerIndex() || newPosition < 0) {
            throw new IllegalArgumentException();
        }
        buffer.writerIndex(newPosition);
    }

    @Override
    public void writeBytes(byte[] bytes) {
        ensureOpen();
        buffer.writeBytes(bytes);
    }

    @Override
    public void writeBytes(byte[] bytes, int offset, int length) {
        ensureOpen();
        buffer.writeBytes(bytes, offset, length);
    }

    @Override
    public void writeByte(int value) {
        ensureOpen();
        buffer.writeByte(value);
    }

    @Override
    public void writeCString(String value) {
        ensureOpen();
        writeCharacters(value, true);
    }

    @Override
    public void writeString(String value) {
        ensureOpen();
        writeInt32(0); // making space for size
        int strLen = writeCharacters(value, false);
        writeInt32(getPosition() - strLen - 4, strLen);
    }

    @Override
    public void writeDouble(double value) {
        writeInt64(Double.doubleToRawLongBits(value));
    }

    @Override
    public void writeInt32(int value) {
        ensureOpen();
        buffer.writeInt(value);
    }

    @Override
    public void writeInt32(int position, int value) {
        ensureOpen();
        buffer.markWriterIndex()
                .writerIndex(position)
                .writeInt(value)
                .resetWriterIndex();
    }

    @Override
    public void writeInt64(long value) {
        ensureOpen();
        buffer.writeLong(value);
    }

    @Override
    public void writeObjectId(ObjectId value) {
        writeBytes(value.toByteArray());
    }

    @Override
    public void close() {
        closed = true;
    }

    private void ensureOpen() {
        if (closed) {
            throw new IllegalStateException("The output is closed");
        }
    }

    private int writeCharacters(final String str, final boolean checkForNullCharacters) {
        int len = str.length();
        int total = 0;

        for (int i = 0; i < len;/*i gets incremented*/) {
            int c = Character.codePointAt(str, i);

            if (checkForNullCharacters && c == 0x0) {
                throw new BsonSerializationException(format("BSON cstring '%s' is not valid because it contains a null character "
                                                            + "at index %d", str, i));
            }
            if (c < 0x80) {
                writeByte((byte) c);
                total += 1;
            } else if (c < 0x800) {
                writeByte((byte) (0xc0 + (c >> 6)));
                writeByte((byte) (0x80 + (c & 0x3f)));
                total += 2;
            } else if (c < 0x10000) {
                writeByte((byte) (0xe0 + (c >> 12)));
                writeByte((byte) (0x80 + ((c >> 6) & 0x3f)));
                writeByte((byte) (0x80 + (c & 0x3f)));
                total += 3;
            } else {
                writeByte((byte) (0xf0 + (c >> 18)));
                writeByte((byte) (0x80 + ((c >> 12) & 0x3f)));
                writeByte((byte) (0x80 + ((c >> 6) & 0x3f)));
                writeByte((byte) (0x80 + (c & 0x3f)));
                total += 4;
            }

            i += Character.charCount(c);
        }

        writeByte((byte) 0);
        total++;
        return total;
    }
}
