/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eightkdata.mongowp.server.callback;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.messages.request.BsonContext;
import com.eightkdata.mongowp.messages.request.EmptyBsonContext;
import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.messages.utils.IterableDocumentProvider;
import io.netty.util.AttributeMap;

import java.util.Collections;

import javax.annotation.Nonnull;

public abstract class MessageReplier {

  public abstract int getRequestId();

  public abstract AttributeMap getAttributeMap();

  public abstract void replyMessage(ReplyMessage replyMessage);

  public void replyMessage(long cursorId, int startingFrom, @Nonnull BsonDocument document) {
    replyMessageNoFlags(EmptyBsonContext.getInstance(), startingFrom, startingFrom,
        IterableDocumentProvider.of(Collections.singleton(document)));
  }

  public void replyMessageNoFlags(BsonContext dataContext, int cursorId, int startingFrom,
      IterableDocumentProvider<?> documents) {
    replyMessage(
        new ReplyMessage(
            dataContext,
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

  public void replyMessageNoCursor(BsonDocument document) {
    replyMessage(0, 0, document);
  }

  public void replyMessageNoCursor(Iterable<BsonDocument> documents) {
    replyMessageNoFlags(
        EmptyBsonContext.getInstance(),
        0,
        0,
        IterableDocumentProvider.of(documents));
  }
}
