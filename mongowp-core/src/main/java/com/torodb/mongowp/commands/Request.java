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
package com.torodb.mongowp.commands;

import java.net.InetAddress;
import java.time.Duration;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class Request {

  private final String database;
  @Nullable
  private final ExternalClientInfo externalClientInfo;
  private final boolean slaveOk;
  private final Duration timeout;

  public Request(String database, @Nullable ExternalClientInfo externalClientInfo,
      boolean slaveOk, @Nullable Duration timeout) {
    this.database = database;
    this.externalClientInfo = externalClientInfo;
    this.slaveOk = slaveOk;
    this.timeout = timeout;
  }

  public String getDatabase() {
    return database;
  }

  @Nullable
  public ExternalClientInfo getExternalClientInfo() {
    return externalClientInfo;
  }

  public boolean isSlaveOk() {
    return slaveOk;
  }

  @Nullable
  public Duration getTimeout() {
    return timeout;
  }

  public static class ExternalClientInfo {

    @Nonnull
    private final InetAddress clientAddress;
    @Nonnegative
    private final int clientPort;

    public ExternalClientInfo(InetAddress clientAddress, int clientPort) {
      this.clientAddress = clientAddress;
      this.clientPort = clientPort;
    }

    public InetAddress getClientAddress() {
      return clientAddress;
    }

    public int getClientPort() {
      return clientPort;
    }
  }
}
