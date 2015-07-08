/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */


package com.eightkdata.mongowp.mongoserver.encoder;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.messages.response.ResponseOpCode;
import com.eightkdata.mongowp.messages.util.EnumInt32FlagsUtil;
import com.eightkdata.mongowp.mongoserver.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import org.bson.BsonDocument;

/**
 *
 */
public class ReplyMessageEncoder {
    public static void encodeMessageHeader(ByteBuf buffer, ReplyMessage message, int requestId) {
        buffer.writeInt(requestId);
        buffer.writeInt(message.getResponseTo());
        buffer.writeInt(ResponseOpCode.OP_REPLY.getOpCode());
    }

    public static void encodeMessageBody(ByteBuf buffer, ReplyMessage message) {
        buffer.writeInt(EnumInt32FlagsUtil.getInt32Flags(message.getFlags()));
        buffer.writeLong(message.getCursorId());
        buffer.writeInt(message.getStartingFrom());
        buffer.writeInt(message.getDocuments().size());

        for(BsonDocument document : message.getDocuments()) {
            ByteBufUtil.writeBsonDocument(buffer, document);
        }
    }
}
