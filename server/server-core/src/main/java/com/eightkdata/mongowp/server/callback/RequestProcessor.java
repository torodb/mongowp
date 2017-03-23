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

import com.eightkdata.mongowp.messages.request.DeleteMessage;
import com.eightkdata.mongowp.messages.request.GetMoreMessage;
import com.eightkdata.mongowp.messages.request.InsertMessage;
import com.eightkdata.mongowp.messages.request.KillCursorsMessage;
import com.eightkdata.mongowp.messages.request.QueryMessage;
import com.eightkdata.mongowp.messages.request.RequestOpCode;
import com.eightkdata.mongowp.messages.request.UpdateMessage;
import io.netty.util.AttributeMap;

import javax.annotation.Nonnull;

public interface RequestProcessor {

  public void onChannelActive(@Nonnull AttributeMap attributeMap);

  public void onChannelInactive(@Nonnull AttributeMap attributeMap);

  public void queryMessage(@Nonnull QueryMessage queryMessage,
      @Nonnull MessageReplier messageReplier) throws Exception;

  public void getMore(@Nonnull GetMoreMessage getMoreMessage, 
      @Nonnull MessageReplier messageReplier) throws Exception;

  public void killCursors(@Nonnull KillCursorsMessage killCursorsMessage,
      @Nonnull MessageReplier messageReplier) throws Exception;

  public void insert(@Nonnull InsertMessage insertMessage, @Nonnull MessageReplier messageReplier)
      throws Exception;

  public void update(@Nonnull UpdateMessage updateMessage, @Nonnull MessageReplier messageReplier)
      throws Exception;

  public void delete(@Nonnull DeleteMessage deleteMessage, @Nonnull MessageReplier messageReplier)
      throws Exception;

  public boolean handleError(@Nonnull RequestOpCode requestOpCode,
      @Nonnull MessageReplier messageReplier, @Nonnull Throwable throwable) throws Exception;
}
