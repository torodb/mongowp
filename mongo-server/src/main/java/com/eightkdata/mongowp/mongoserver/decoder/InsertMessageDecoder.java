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


package com.eightkdata.mongowp.mongoserver.decoder;

import com.eightkdata.mongowp.messages.request.InsertMessage;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.mongoserver.exception.InvalidMessageException;
import com.eightkdata.mongowp.mongoserver.util.ByteBufUtil;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.inject.Singleton;
import org.bson.BsonDocument;

/**
 *
 */
@Singleton
public class InsertMessageDecoder implements MessageDecoder<InsertMessage> {
    @Override
    public @Nonnegative
    InsertMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage) throws InvalidMessageException {
        int flags = buffer.readInt();
        String fullCollectionName = ByteBufUtil.readCString(buffer);
        List<BsonDocument> documents = Lists.newArrayList();
        while(buffer.readableBytes() > 0) {
        	documents.add(ByteBufUtil.readBsonDocument(buffer));
        }

        return new InsertMessage(
                requestBaseMessage, flags, fullCollectionName, documents
        );
    }
}
