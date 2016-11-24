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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class RequestBaseMessage {

  @Nullable
  private final InetAddress clientAddress;
  @Nonnegative
  private final int clientPort;
  private final int requestId;

  public RequestBaseMessage(@Nullable InetAddress clientAddress, int clientPort, int requestId) {
    this.clientAddress = clientAddress;
    this.clientPort = clientPort;
    this.requestId = requestId;
  }

  @Nullable
  public InetAddress getClientAddress() {
    return clientAddress;
  }

  @Nonnull
  public String getClientAddressString() {
    return clientAddress != null ? clientAddress.getHostAddress() : "null";
  }

  public int getClientPort() {
    return clientPort;
  }

  public int getRequestId() {
    return requestId;
  }
}
