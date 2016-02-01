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


package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.bson.netty.NettyBsonDocumentReader;
import com.eightkdata.mongowp.bson.netty.NettyBsonReaderException;
import com.eightkdata.mongowp.bson.netty.NettyStringReader;
import com.eightkdata.mongowp.exceptions.InvalidBsonException;
import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.messages.request.InsertMessage;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.server.util.ByteBufIterableDocumentProvider;
import com.eightkdata.mongowp.server.util.EnumBitFlags;
import com.eightkdata.mongowp.server.util.EnumInt32FlagsUtil;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnegative;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class InsertMessageDecoder extends AbstractMessageDecoder<InsertMessage> {

    private final NettyStringReader stringReader;
    private final NettyBsonDocumentReader docReader;

    @Inject
    public InsertMessageDecoder(NettyStringReader stringReader, NettyBsonDocumentReader docReader) {
        this.stringReader = stringReader;
        this.docReader = docReader;
    }

    @Override
    @SuppressFBWarnings(value = {"RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"},
            justification = "Findbugs thinks ByteBuf#readerIndex(...) has no"
                    + "side effect")
    public @Nonnegative
    InsertMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidNamespaceException, InvalidBsonException {
        try {
            MyBsonContext context = new MyBsonContext(buffer);

            int flags = buffer.readInt();
            String fullCollectionName = stringReader.readCString(buffer, true);
            
            ByteBuf docBuf = buffer.slice(buffer.readerIndex(), buffer.readableBytes());
            docBuf.retain();
            
            buffer.readerIndex(buffer.writerIndex());

            ByteBufIterableDocumentProvider documents = new ByteBufIterableDocumentProvider(docBuf, docReader);

            //TODO: improve the way database and cache are pooled
            return new InsertMessage(
                    requestBaseMessage,
                    context,
                    getDatabase(fullCollectionName).intern(),
                    getCollection(fullCollectionName).intern(),
                    EnumInt32FlagsUtil.isActive(Flag.CONTINUE_ON_ERROR, flags),
                    documents
            );
        } catch (NettyBsonReaderException ex) {
            throw new InvalidBsonException(ex);
        }
    }

    private enum Flag implements EnumBitFlags {
        CONTINUE_ON_ERROR(0);

        @Nonnegative private final int flagBitPosition;

        private Flag(@Nonnegative int flagBitPosition) {
            this.flagBitPosition = flagBitPosition;
        }

        @Override
        public int getFlagBitPosition() {
            return flagBitPosition;
        }
    }
}
