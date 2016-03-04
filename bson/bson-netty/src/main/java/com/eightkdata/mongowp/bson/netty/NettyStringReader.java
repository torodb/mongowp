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

import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import io.netty.buffer.ByteBuf;

import static com.eightkdata.mongowp.bson.netty.PooledNettyStringReader.*;

/**
 *
 */
public interface NettyStringReader {


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
    public String readCString(ByteBuf buffer, boolean likelyCacheable) throws NettyBsonReaderException;

    /**
     * A method that skips a C-string from a ByteBuf.
     * This method modified the internal state of the ByteBuf, advancing the read pointer to the position after the
     * cstring.
     *
     * @param buffer
     * @throws com.eightkdata.mongowp.bson.netty.NettyBsonReaderException
     */
    public void skipCString(ByteBuf buffer) throws NettyBsonReaderException;

    public String readString(@Loose @ModifiesIndexes ByteBuf byteBuf, boolean likelyCacheable);

    public ByteBuf readStringAsSlice(@Loose @ModifiesIndexes ByteBuf byteBuf);
}
