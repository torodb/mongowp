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

package com.eightkdata.mongowp.server.api;

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
