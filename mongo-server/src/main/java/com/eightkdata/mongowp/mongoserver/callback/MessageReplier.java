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


package com.eightkdata.mongowp.mongoserver.callback;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import com.eightkdata.mongowp.messages.utils.SimpleIterableDocumentsProvider;
import io.netty.util.AttributeMap;
import java.util.Collections;
import javax.annotation.Nonnull;
import org.bson.BsonDocument;

/**
 *
 */
public abstract class MessageReplier {

    public abstract int getRequestId();

    public abstract AttributeMap getAttributeMap();

    public abstract void replyMessage(ReplyMessage replyMessage);

    public void replyMessageNoFlags(int cursorId, int startingFrom, IterableDocumentProvider<?> documents) {
        replyMessage(
                new ReplyMessage(
                        getRequestId(),
                        false,
                        false,
                        false,
                        false,
                        cursorId,
                        startingFrom,
                        documents
                )
        );
    }

    public void replyMessage(long cursorId, int startingFrom, @Nonnull BsonDocument document) {
        replyMessageNoFlags(startingFrom, startingFrom, new SimpleIterableDocumentsProvider<>(Collections.singleton(document)));
    }

    public void replyMessageNoCursor(BsonDocument document) {
        replyMessage(0, 0, document);
    }

    public void replyMessageNoCursor(Iterable<BsonDocument> documents) {
        replyMessageNoFlags(0, 0, new SimpleIterableDocumentsProvider(documents));
    }
}
