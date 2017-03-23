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
