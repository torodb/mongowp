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
package com.torodb.mongowp.client.wrapper;

import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;

import java.util.ArrayList;
import java.util.List;

import javax.net.SocketFactory;

public class MongoClientConfiguration {

  private final HostAndPort hostAndPort;
  private final SocketFactory socketFactory;
  private final boolean sslEnabled;
  private final boolean sslAllowInvalidHostnames;
  private final ImmutableList<MongoAuthenticationConfiguration> authenticationConfigurations;

  public MongoClientConfiguration(HostAndPort hostAndPort, SocketFactory socketFactory,
      boolean sslEnabled, boolean sslAllowInvalidHostnames,
      ImmutableList<MongoAuthenticationConfiguration> authenticationConfiguration) {
    super();
    this.hostAndPort = hostAndPort.withDefaultPort(27017);
    this.socketFactory = socketFactory;
    this.sslEnabled = sslEnabled;
    this.sslAllowInvalidHostnames = sslAllowInvalidHostnames;
    this.authenticationConfigurations = authenticationConfiguration;
  }

  public static MongoClientConfiguration unsecure(HostAndPort hostAndPort) {
    return new MongoClientConfiguration(hostAndPort, null, false, false, ImmutableList.of());
  }

  public HostAndPort getHostAndPort() {
    return hostAndPort;
  }

  public SocketFactory getSocketFactory() {
    return socketFactory;
  }

  public boolean isSslEnabled() {
    return sslEnabled;
  }

  public boolean isSslAllowInvalidHostnames() {
    return sslAllowInvalidHostnames;
  }

  public ImmutableList<MongoAuthenticationConfiguration> getAuthenticationConfigurations() {
    return authenticationConfigurations;
  }

  public Builder builder(HostAndPort hostAndPort) {
    Builder builder = new Builder(hostAndPort);
    builder.socketFactory = socketFactory;
    builder.sslEnabled = sslEnabled;
    builder.sslAllowInvalidHostnames = sslAllowInvalidHostnames;
    builder.authenticationConfigurations.addAll(authenticationConfigurations);

    return builder;
  }

  @Override
  public String toString() {
    return hostAndPort.toString();
  }

  public static class Builder {

    private HostAndPort hostAndPort;
    private SocketFactory socketFactory;
    private boolean sslEnabled;
    private boolean sslAllowInvalidHostnames;
    private List<MongoAuthenticationConfiguration> authenticationConfigurations =
        new ArrayList<>();

    public Builder(HostAndPort hostAndPort) {
      this.hostAndPort = hostAndPort;
    }

    public HostAndPort getHostAndPort() {
      return hostAndPort;
    }

    public SocketFactory getSocketFactory() {
      return socketFactory;
    }

    public Builder setSocketFactory(SocketFactory socketFactory) {
      this.socketFactory = socketFactory;
      return this;
    }

    public boolean isSslEnabled() {
      return sslEnabled;
    }

    public Builder setSslEnabled(boolean sslEnabled) {
      this.sslEnabled = sslEnabled;
      return this;
    }

    public boolean isSslAllowInvalidHostnames() {
      return sslAllowInvalidHostnames;
    }

    public Builder setSslAllowInvalidHostnames(boolean sslAllowInvalidHostnames) {
      this.sslAllowInvalidHostnames = sslAllowInvalidHostnames;
      return this;
    }

    public ImmutableList<MongoAuthenticationConfiguration> getAuthenticationConfiguration() {
      return ImmutableList.copyOf(authenticationConfigurations);
    }

    public Builder addAuthenticationConfiguration(
        MongoAuthenticationConfiguration authenticationConfiguration) {
      this.authenticationConfigurations.add(authenticationConfiguration);
      return this;
    }

    @SuppressWarnings("checkstyle:JavadocMethod")
    public MongoClientConfiguration build() {
      return new MongoClientConfiguration(hostAndPort, socketFactory,
          sslEnabled, sslAllowInvalidHostnames,
          ImmutableList.copyOf(authenticationConfigurations));
    }
  }
}
