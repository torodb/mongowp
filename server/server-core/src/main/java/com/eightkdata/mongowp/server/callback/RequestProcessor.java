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


package com.eightkdata.mongowp.server.callback;

import com.eightkdata.mongowp.messages.request.*;
import io.netty.util.AttributeMap;
import javax.annotation.Nonnull;

/**
 *
 */
public interface RequestProcessor {
	public void onChannelActive(@Nonnull AttributeMap attributeMap);
	public void onChannelInactive(@Nonnull AttributeMap attributeMap);
    public void queryMessage(@Nonnull QueryMessage queryMessage, @Nonnull MessageReplier messageReplier) throws Exception;
    public void getMore(@Nonnull GetMoreMessage getMoreMessage, @Nonnull MessageReplier messageReplier) throws Exception;
    public void killCursors(@Nonnull KillCursorsMessage killCursorsMessage, @Nonnull MessageReplier messageReplier) throws Exception;
    public void insert(@Nonnull InsertMessage insertMessage, @Nonnull MessageReplier messageReplier) throws Exception;
    public void update(@Nonnull UpdateMessage updateMessage, @Nonnull MessageReplier messageReplier) throws Exception;
    public void delete(@Nonnull DeleteMessage deleteMessage, @Nonnull MessageReplier messageReplier) throws Exception;
    public boolean handleError(@Nonnull RequestOpCode requestOpCode, @Nonnull MessageReplier messageReplier, @Nonnull Throwable throwable) throws Exception;
}
