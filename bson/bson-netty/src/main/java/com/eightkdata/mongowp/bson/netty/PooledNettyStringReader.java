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
 * along with bson. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 *
 */

package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.pool.StringPool;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import javax.inject.Inject;

/**
 *
 */
@SuppressFBWarnings(
        value="RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
        justification = "It seems FindBugs considers ByteBuf methods are not side effect"
)
public class PooledNettyStringReader implements NettyStringReader {
    public static final byte CSTRING_BYTE_TERMINATION = 0x00;

    private final StringPool stringPool;

    @Inject
    public PooledNettyStringReader(StringPool stringPool) {
        this.stringPool = stringPool;
    }

    /**
     * A method that reads a C-string from a ByteBuf.
     * This method modified the internal state of the ByteBuf, advancing the read pointer to the position after the
     * cstring.
     *
     * @param buffer
     * @param likelyCacheable
     * @return The C-String as a String object or null if there was no C-String in the ByteBuf
     * @throws com.eightkdata.mongowp.bson.netty.NettyBsonReaderException
     */
    @Override
    public String readCString(ByteBuf buffer, boolean likelyCacheable) throws NettyBsonReaderException {
        int pos = buffer.bytesBefore(CSTRING_BYTE_TERMINATION);
        if(pos == -1) {
            throw new NettyBsonReaderException("A cstring was expected but no 0x00 byte was found");
        }

        String result = stringPool.fromPool(likelyCacheable, buffer.readSlice(pos));

        buffer.readByte();  // Discard the termination byte

        return result;
    }

    @Override
    /**
     * A method that skips a C-string from a ByteBuf.
     * This method modified the internal state of the ByteBuf, advancing the read pointer to the position after the
     * cstring.
     *
     * @param buffer
     * @throws com.eightkdata.mongowp.bson.netty.NettyBsonReaderException
     */
    public void skipCString(ByteBuf buffer) throws NettyBsonReaderException {
        int bytesBefore = buffer.bytesBefore(CSTRING_BYTE_TERMINATION);
        if(bytesBefore == -1) {
            throw new NettyBsonReaderException("A cstring was expected but no 0x00 byte was found");
        }
        buffer.skipBytes(bytesBefore + 1);
    }

    @Override
    public String readString(@Loose @ModifiesIndexes ByteBuf byteBuf, boolean likelyCacheable) {
        int stringLength = byteBuf.readInt();
        
        String str = stringPool.fromPool(
                likelyCacheable,
                byteBuf.slice(byteBuf.readerIndex(), stringLength - 1)
        );

        byteBuf.skipBytes(stringLength);

        return str;
    }

    @Override
    public ByteBuf readStringAsSlice(@Loose @ModifiesIndexes ByteBuf byteBuf) {
        int stringLength = byteBuf.readInt();
        ByteBuf result = byteBuf.readSlice(stringLength - 1);
        byte b = byteBuf.readByte(); //discard the last 0x00
        assert b == 0x00;

        return result;
    }
}
