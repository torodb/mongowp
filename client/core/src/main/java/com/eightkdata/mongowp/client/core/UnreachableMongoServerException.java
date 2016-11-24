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

package com.eightkdata.mongowp.client.core;

import com.google.common.net.HostAndPort;

/**
 *
 */
public class UnreachableMongoServerException extends Exception {

  private static final long serialVersionUID = 1L;

  private final HostAndPort hostAndPort;

  public UnreachableMongoServerException(HostAndPort hostAndPort) {
    this.hostAndPort = hostAndPort;
  }

  public UnreachableMongoServerException(HostAndPort hostAndPort, String message) {
    super(message);
    this.hostAndPort = hostAndPort;
  }

  public UnreachableMongoServerException(HostAndPort hostAndPort, String message, Throwable cause) {
    super(message, cause);
    this.hostAndPort = hostAndPort;
  }

  public UnreachableMongoServerException(HostAndPort hostAndPort, Throwable cause) {
    super(cause);
    this.hostAndPort = hostAndPort;
  }

  public HostAndPort getHostAndPort() {
    return hostAndPort;
  }
}
