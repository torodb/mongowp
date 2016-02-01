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


package com.eightkdata.mongowp.server.encoder;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.netty.NettyBsonDocumentWriter;
import com.eightkdata.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.messages.response.ResponseOpCode;
import com.eightkdata.mongowp.server.util.EnumBitFlags;
import com.eightkdata.mongowp.server.util.EnumInt32FlagsUtil;
import com.google.common.collect.FluentIterable;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import java.util.EnumSet;
import javax.annotation.Nonnegative;
import javax.inject.Inject;

/**
 *
 */
@SuppressFBWarnings(
        value="RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
        justification = "It seems FindBugs considers ByteBuf methods are not side effect"
)
public class ReplyMessageEncoder {

    private final NettyBsonDocumentWriter writer;

    @Inject
    public ReplyMessageEncoder(NettyBsonDocumentWriter writer) {
        this.writer = writer;
    }

    public void encodeMessageHeader(ByteBuf buffer, ReplyMessage message, int requestId) {
        buffer.writeInt(requestId);
        buffer.writeInt(message.getResponseTo());
        buffer.writeInt(ResponseOpCode.OP_REPLY.getOpCode());
    }

    public void encodeMessageBody(ByteBuf buffer, ReplyMessage message) {
        FluentIterable<? extends BsonDocument> docs = message.getDocuments().getIterable(AllocationType.HEAP);

        buffer.writeInt(EnumInt32FlagsUtil.getInt32Flags(extractFlags(message)));
        buffer.writeLong(message.getCursorId());
        buffer.writeInt(message.getStartingFrom());
        buffer.writeInt(docs.size());

        for(BsonDocument document : docs) {
            writer.writeInto(buffer, document);
        }
    }

    private EnumSet<Flag> extractFlags(ReplyMessage message) {
        EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
        if (message.isCursorNotFound()) {
            flags.add(Flag.CURSOR_NOT_FOUND);
        }
        if (message.isQueryFailure()) {
            flags.add(Flag.QUERY_FAILURE);
        }
        if (message.isShardConfigStale()) {
            flags.add(Flag.SHARD_CONFIG_STALE);
        }
        if (message.isAwaitCapable()) {
            flags.add(Flag.AWAIT_CAPABLE);
        }
        return flags;
    }
    
    private enum Flag implements EnumBitFlags {
        CURSOR_NOT_FOUND(0),
        QUERY_FAILURE(1),
        SHARD_CONFIG_STALE(2),
        AWAIT_CAPABLE(3);

        @Nonnegative
        private final int flagBitPosition;

        private Flag(@Nonnegative int flagBitPosition) {
            this.flagBitPosition = flagBitPosition;
        }

        @Override
        public int getFlagBitPosition() {
            return flagBitPosition;
        }
    }
}
