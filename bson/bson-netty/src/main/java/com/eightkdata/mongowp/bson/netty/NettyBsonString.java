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

import com.eightkdata.mongowp.bson.abst.AbstractBsonString;
import com.eightkdata.mongowp.bson.impl.StringBsonString;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import com.google.common.base.Charsets;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import java.io.ObjectStreamException;

/**
 *
 */
@SuppressFBWarnings(value = {"SE_BAD_FIELD", "SE_NO_SERIALVERSIONID"},
        justification = "writeReplace is used")
public class NettyBsonString extends AbstractBsonString {

    private static final long serialVersionUID = 7152154519026299154L;

    @Tight private final ByteBuf byteBuf;

    public NettyBsonString(@Tight @ModifiesIndexes ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    @Override
    public String getValue() {
        return getString(byteBuf);
    }

    private String getString(@Tight ByteBuf byteBuf) {
        int lenght = getStringLenght(byteBuf);
        byte[] bytes = new byte[lenght];
        byteBuf.getBytes(0, bytes);
        return new String(bytes, Charsets.UTF_8);
    }

    private int getStringLenght(@Tight ByteBuf byteBuf) {
        return byteBuf.readableBytes();
    }

    private Object writeReplace() throws ObjectStreamException {
        return new StringBsonString(getValue());
    }
}
