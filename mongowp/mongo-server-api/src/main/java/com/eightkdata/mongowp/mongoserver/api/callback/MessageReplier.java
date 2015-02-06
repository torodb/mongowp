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


package com.eightkdata.mongowp.mongoserver.api.callback;

import com.eightkdata.mongowp.messages.response.ReplyMessage;
import com.eightkdata.mongowp.messages.response.ReplyMessage.Flag;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.nettybson.api.BSONDocument;
import com.eightkdata.nettybson.mongodriver.MongoBSONDocument;
import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class MessageReplier {
	/**
	 * The connectionId must be unique for each connection
	 */
    public static final AttributeKey<Integer> CONNECTION_ID = AttributeKey.valueOf("connectionId");
	/**
	 * The requestId must be unique for each request
	 */
    public static final AttributeKey<Integer> REQUEST_ID = AttributeKey.valueOf("requestId");

    private final ChannelHandlerContext channelHandlerContext;
    private final int requestId;

    public MessageReplier(@Nonnull ChannelHandlerContext channelHandlerContext) {
        Preconditions.checkNotNull(channelHandlerContext);

        this.channelHandlerContext = channelHandlerContext;
        this.requestId = channelHandlerContext.attr(REQUEST_ID).get();
    }
    
    public int getConnectionId() {
    	return channelHandlerContext.attr(CONNECTION_ID).get();
    }
    
    public AttributeMap getAttributeMap() {
    	return channelHandlerContext;
    }

    private void replyMessage(ReplyMessage replyMessage) {
        channelHandlerContext.writeAndFlush(replyMessage);
    }

    private void replyMessageBuilder(ReplyMessage.Builder builder) {
        replyMessage(builder.build());
    }

    public void replyMessage(long cursorId, int startingFrom, @Nonnull BSONDocument document) {
        replyMessageBuilder(new ReplyMessage.Builder(requestId, cursorId, startingFrom, document));
    }

    public void replyMessageNoCursor(BSONDocument document) {
        replyMessage(0, 0, document);
    }
    
    public void replyMessageNoCursor(Iterable<BSONDocument> documents) {
        ReplyMessage.Builder builder = new ReplyMessage.Builder(requestId, 0, 0);
        Iterator<BSONDocument> iterator = documents.iterator();
        while (iterator.hasNext()) {
            builder.addBSONDocument(iterator.next());
        }
        replyMessageBuilder(builder);
    }

    private ReplyMessage.Builder getReplyMessageBuilder(
            long cursorId, int startingFrom, @Nonnull BSONDocument firstDocument, @Nonnull BSONDocument... documents
    ) {
        ReplyMessage.Builder builder = new ReplyMessage.Builder(
                requestId, cursorId, startingFrom, firstDocument
        );
        for(BSONDocument document : documents) {
            builder.addBSONDocument(document);
        }

        return builder;
    }

    public void replyMessageMultipleDocuments(
            long cursorId, int startingFrom, @Nonnull BSONDocument firstDocument, @Nonnull BSONDocument... documents
    ) {
        replyMessageBuilder(getReplyMessageBuilder(cursorId, startingFrom, firstDocument, documents));
    }

    public void replyMessageMultipleDocuments(
            long cursorId, int startingFrom, @Nonnull Iterable<BSONDocument> documents
    ) {
    	replyMessageMultipleDocumentsWithFlags(cursorId, startingFrom, documents, EnumSet.noneOf(Flag.class));
    }

    public void replyMessageMultipleDocumentsWithFlags(
            long cursorId, int startingFrom, @Nonnull Iterable<BSONDocument> documents, @Nonnull EnumSet<ReplyMessage.Flag> flags
    ) {
    	Iterator<BSONDocument> iterator = documents.iterator();
        ReplyMessage.Builder builder = new ReplyMessage.Builder(
                requestId, cursorId, startingFrom
        );
        while (iterator.hasNext()) {
            builder.addBSONDocument(iterator.next());
        }

        replyMessageBuilder(builder.setFlags(flags));
    }

    public void replyMessageWithFlags(
            long cursorId, int startingFrom, @Nonnull EnumSet<ReplyMessage.Flag> flags
    ) {
        ReplyMessage.Builder builder = new ReplyMessage.Builder(
                requestId, cursorId, startingFrom
        );

        replyMessageBuilder(builder.setFlags(flags));
    }

    public void replyMessageWithFlags(
            @Nonnull EnumSet<ReplyMessage.Flag> flags, long cursorId, int startingFrom,
            @Nonnull BSONDocument firstDocument, @Nullable BSONDocument... documents
    ) {
        replyMessageBuilder(getReplyMessageBuilder(cursorId, startingFrom, firstDocument, documents).setFlags(flags));
    }

    public void replyQueryFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        Map<String,Object> errorDocumentMap = new HashMap<String, Object>(2);
        errorDocumentMap.put("ok", MongoWP.KO);
        errorDocumentMap.put("$err", MessageFormat.format(errorMessage, args));
        errorDocumentMap.put("code", errorCode);

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.QUERY_FAILURE), 0, 0, new MongoBSONDocument(errorDocumentMap)
        );
    }

    public void replyQueryFailure(@Nonnull MongoWP.ErrorCode errorCode, Object...args) {
        replyQueryFailure(errorCode.getErrorMessage(), errorCode.getErrorCode(), args);
    }

    public void replyQueryCommandFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        Map<String,Object> errorDocumentMap = new HashMap<String, Object>(2);
        errorDocumentMap.put("ok", MongoWP.KO);
        errorDocumentMap.put("errmsg", MessageFormat.format(errorMessage, args));
        errorDocumentMap.put("code", errorCode);

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.QUERY_FAILURE), 0, 0, new MongoBSONDocument(errorDocumentMap)
        );
    }

    public void replyQueryCommandFailure(@Nonnull MongoWP.ErrorCode errorCode, Object...args) {
        replyQueryCommandFailure(errorCode.getErrorMessage(), errorCode.getErrorCode(), args);
    }

    public void replyWriteFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        Map<String,Object> errorDocumentMap = new HashMap<String, Object>(2);
        errorDocumentMap.put("ok", MongoWP.KO);
        errorDocumentMap.put("errmsg", MessageFormat.format(errorMessage, args));
        errorDocumentMap.put("code", errorCode);

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.QUERY_FAILURE), 0, 0, new MongoBSONDocument(errorDocumentMap)
        );
    }

    public void replyWriteFailure(@Nonnull MongoWP.ErrorCode errorCode, Object...args) {
        replyWriteFailure(errorCode.getErrorMessage(), errorCode.getErrorCode(), args);
    }

    public void replyGetMoreFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        Map<String,Object> errorDocumentMap = new HashMap<String, Object>(2);
        errorDocumentMap.put("ok", MongoWP.KO);
        errorDocumentMap.put("$err", MessageFormat.format(errorMessage, args));
        errorDocumentMap.put("code", errorCode);

        replyMessageWithFlags(
                EnumSet.of(ReplyMessage.Flag.CURSOR_NOT_FOUND), 0, 0, new MongoBSONDocument(errorDocumentMap)
        );
    }

    public void replyGetMoreFailure(@Nonnull MongoWP.ErrorCode errorCode) {
        replyGetMoreFailure(errorCode.getErrorMessage(), errorCode.getErrorCode());
    }

    public void replyKillCursorsFailure(@Nonnull String errorMessage, @Nonnegative int errorCode, Object...args) {
        Map<String,Object> errorDocumentMap = new HashMap<String, Object>(2);
        errorDocumentMap.put("ok", MongoWP.KO);
        errorDocumentMap.put("$err", MessageFormat.format(errorMessage, args));
        errorDocumentMap.put("code", errorCode);

        replyMessageWithFlags(
                EnumSet.noneOf(ReplyMessage.Flag.class), 0, 0, new MongoBSONDocument(errorDocumentMap)
        );
    }

    public void replyKillCursorsFailure(@Nonnull MongoWP.ErrorCode errorCode) {
        replyKillCursorsFailure(errorCode.getErrorMessage(), errorCode.getErrorCode());
    }
}
