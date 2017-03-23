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
