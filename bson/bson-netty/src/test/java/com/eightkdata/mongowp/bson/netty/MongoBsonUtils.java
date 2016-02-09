/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bson-netty. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.impl.*;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.bson.*;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.io.BsonInput;
import org.bson.io.BsonOutput;
import org.bson.json.JsonReader;
import org.bson.types.ObjectId;


/**
 *
 */
public class MongoBsonUtils {

    private static final BsonDocumentCodec BSON_CODEC = new BsonDocumentCodec();

    private MongoBsonUtils() {
    }

    public static void write(org.bson.BsonDocument mongoDoc, ByteBuf sink) {
        BsonWriter writer = new BsonBinaryWriter(new ByteBufBsonOutputAdaptor(sink));

        EncoderContext context = EncoderContext.builder().build();

        BSON_CODEC.encode(writer, mongoDoc, context);

    }

    public static BsonDocument read(ByteBuf source) {
        BsonReader reader = new BsonBinaryReader(new ByteBufBsonInputAdaptor(source));

        DecoderContext context = DecoderContext.builder().build();
        return BSON_CODEC.decode(reader, context);
    }

    public static BsonValue read(InputStream is) throws IOException {
        String allText = CharStreams.toString(new BufferedReader(new InputStreamReader(is, Charsets.UTF_8)));
        JsonReader reader = new JsonReader(allText);

        DecoderContext context = DecoderContext.builder().build();

        return BSON_CODEC.decode(reader, context);
    }

    @SuppressFBWarnings(
            value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
            justification
            = "It seems FindBugs considers ByteBuf methods are not side effect"
    )
    public static class ByteBufBsonOutputAdaptor implements BsonOutput {

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

            for (int i = 0; i < len;/*
                     * i gets incremented
                     */) {
                int c = Character.codePointAt(str, i);

                if (checkForNullCharacters && c == 0x0) {
                    throw new BsonSerializationException(String.format("BSON cstring '%s' is not valid because it contains a null character "
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

    @SuppressFBWarnings(
            value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
            justification
            = "It seems FindBugs considers ByteBuf methods are not side effect"
    )
    public static class ByteBufBsonInputAdaptor implements BsonInput {

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
            if (strSize < 0) {
                throw new AssertionError("Expected a CString but no '0' character was found");
            }
            byte[] bytes = new byte[strSize];
            readBytes(bytes);
            readByte();  // read the trailing null byte

            return new String(bytes, UTF8_CHARSET);
        }

        @Override
        public void skipCString() {
            int nextNull
                    = buffer.indexOf(buffer.readerIndex(), buffer.capacity(), (byte) 0);
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


}
