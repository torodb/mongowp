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
import com.eightkdata.mongowp.messages.response.ReplyMessage.Flag;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import io.netty.util.AttributeMap;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Iterator;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

/**
 *
 */
public abstract class MessageReplier {

    public abstract int getRequestId();
    
    public abstract AttributeMap getAttributeMap();

    public abstract void replyMessage(ReplyMessage replyMessage);

    private void replyMessageBuilder(ReplyMessage.Builder builder) {
        replyMessage(builder.build());
    }

    public void replyMessage(long cursorId, int startingFrom, @Nonnull BsonDocument document) {
        replyMessageBuilder(new ReplyMessage.Builder(getRequestId(), cursorId, startingFrom, document));
    }

    public void replyMessageNoCursor(BsonDocument document) {
        replyMessage(0, 0, document);
    }
    
    public void replyMessageNoCursor(Iterable<? extends BsonDocument> documents) {
        ReplyMessage.Builder builder = new ReplyMessage.Builder(getRequestId(), 0, 0);
        Iterator<? extends BsonDocument> iterator = documents.iterator();
        while (iterator.hasNext()) {
            builder.addBsonDocument(iterator.next());
        }
        replyMessageBuilder(builder);
    }

    private ReplyMessage.Builder getReplyMessageBuilder(
            long cursorId, int startingFrom, @Nonnull BsonDocument firstDocument, @Nonnull BsonDocument... documents
    ) {
        ReplyMessage.Builder builder = new ReplyMessage.Builder(
                getRequestId(), cursorId, startingFrom, firstDocument
        );
        for(BsonDocument document : documents) {
            builder.addBsonDocument(document);
        }

        return builder;
    }

    public void replyMessageMultipleDocuments(
            long cursorId, int startingFrom, @Nonnull BsonDocument firstDocument, @Nonnull BsonDocument... documents
    ) {
        replyMessageBuilder(getReplyMessageBuilder(cursorId, startingFrom, firstDocument, documents));
    }

    public void replyMessageMultipleDocuments(
            long cursorId, int startingFrom, @Nonnull Iterable<BsonDocument> documents
    ) {
    	replyMessageMultipleDocumentsWithFlags(cursorId, startingFrom, documents, EnumSet.noneOf(Flag.class));
    }

    public void replyMessageMultipleDocumentsWithFlags(
            long cursorId, int startingFrom, @Nonnull Iterable<BsonDocument> documents, @Nonnull EnumSet<ReplyMessage.Flag> flags
    ) {
    	Iterator<BsonDocument> iterator = documents.iterator();
        ReplyMessage.Builder builder = new ReplyMessage.Builder(
                getRequestId(), cursorId, startingFrom
        );
        while (iterator.hasNext()) {
            builder.addBsonDocument(iterator.next());
        }

        replyMessageBuilder(builder.setFlags(flags));
    }

    public void replyMessageWithFlags(
            long cursorId, int startingFrom, @Nonnull EnumSet<ReplyMessage.Flag> flags
    ) {
        ReplyMessage.Builder builder = new ReplyMessage.Builder(
                getRequestId(), cursorId, startingFrom
        );

        replyMessageBuilder(builder.setFlags(flags));
    }

    public void replyMessageWithFlags(
            @Nonnull EnumSet<ReplyMessage.Flag> flags, long cursorId, int startingFrom,
            @Nonnull BsonDocument firstDocument, @Nullable BsonDocument... documents
    ) {
        replyMessageBuilder(getReplyMessageBuilder(cursorId, startingFrom, firstDocument, documents).setFlags(flags));
    }

    public void replyQueryFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        BsonDocument errorDocument = new BsonDocument();
        errorDocument.put("ok", MongoWP.BSON_KO);
        errorDocument.put("$err", new BsonString(MessageFormat.format(errorMessage, args)));
        errorDocument.put("code", new BsonInt32(errorCode));

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.QUERY_FAILURE), 0, 0, errorDocument
        );
    }

    public void replyQueryFailure(@Nonnull MongoWP.ErrorCode errorCode, Object...args) {
        replyQueryFailure(errorCode.getErrorMessage(), errorCode.getErrorCode(), args);
    }

    public void replyQueryCommandFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        BsonDocument errorDocument = new BsonDocument();
        errorDocument.put("ok", MongoWP.BSON_KO);
        errorDocument.put("errmsg", new BsonString(MessageFormat.format(errorMessage, args)));
        errorDocument.put("code", new BsonInt32(errorCode));

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.QUERY_FAILURE), 0, 0, errorDocument
        );
    }

    public void replyQueryCommandFailure(@Nonnull MongoWP.ErrorCode errorCode, Object...args) {
        replyQueryCommandFailure(errorCode.getErrorMessage(), errorCode.getErrorCode(), args);
    }

    public void replyWriteFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        BsonDocument errorDocument = new BsonDocument();
        errorDocument.put("ok", MongoWP.BSON_KO);
        errorDocument.put("errmsg", new BsonString(MessageFormat.format(errorMessage, args)));
        errorDocument.put("code", new BsonInt32(errorCode));

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.QUERY_FAILURE), 0, 0, errorDocument
        );
    }

    public void replyWriteFailure(@Nonnull MongoWP.ErrorCode errorCode, Object...args) {
        replyWriteFailure(errorCode.getErrorMessage(), errorCode.getErrorCode(), args);
    }

    public void replyGetMoreFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        BsonDocument errorDocument = new BsonDocument();
        errorDocument.put("ok", MongoWP.BSON_KO);
        errorDocument.put("$err", new BsonString(MessageFormat.format(errorMessage, args)));
        errorDocument.put("code", new BsonInt32(errorCode));

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.CURSOR_NOT_FOUND), 0, 0, errorDocument
        );
    }

    public void replyGetMoreFailure(@Nonnull MongoWP.ErrorCode errorCode) {
        replyGetMoreFailure(errorCode.getErrorMessage(), errorCode.getErrorCode());
    }

    public void replyKillCursorsFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        BsonDocument errorDocument = new BsonDocument();
        errorDocument.put("ok", MongoWP.BSON_KO);
        errorDocument.put("$err", new BsonString(MessageFormat.format(errorMessage, args)));
        errorDocument.put("code", new BsonInt32(errorCode));

        replyMessageWithFlags(
                EnumSet.noneOf(ReplyMessage.Flag.class), 0, 0, errorDocument
        );
    }

    public void replyKillCursorsFailure(@Nonnull MongoWP.ErrorCode errorCode) {
        replyKillCursorsFailure(errorCode.getErrorMessage(), errorCode.getErrorCode());
    }
}
