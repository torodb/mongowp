/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.messages.request;

import java.net.InetAddress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public abstract class AbstractRequestMessage implements RequestMessage {

  @Nonnull
  private final RequestBaseMessage requestBaseMessage;
  @Nonnull
  private final BsonContext dataContext;

  protected AbstractRequestMessage(
      @Nonnull RequestBaseMessage requestBaseMessage,
      @Nonnull BsonContext dataContext) {
    this.requestBaseMessage = requestBaseMessage;
    this.dataContext = dataContext;
  }

  @Nonnull
  public RequestBaseMessage getBaseMessage() {
    return requestBaseMessage;
  }

  @Nullable
  public InetAddress getClientAddress() {
    return requestBaseMessage.getClientAddress();
  }

  @Nullable
  public String getClientAddressString() {
    return requestBaseMessage.getClientAddressString();
  }

  public int getClientPort() {
    return requestBaseMessage.getClientPort();
  }

  public int getRequestId() {
    return requestBaseMessage.getRequestId();
  }

  BsonContext getDataContext() {
    return dataContext;
  }

  @Override
  public void close() throws Exception {
    dataContext.close();
  }

  @Override
  public String toString() {
    return "clientAddress=" + requestBaseMessage.getClientAddressString() + ", clientPort="
        + getClientPort() + ", requestId=" + getRequestId();
  }
}
