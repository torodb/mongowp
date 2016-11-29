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

package com.eightkdata.mongowp.client.wrapper;

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
        new ArrayList<MongoAuthenticationConfiguration>();

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

    public MongoClientConfiguration build() {
      return new MongoClientConfiguration(hostAndPort, socketFactory,
          sslEnabled, sslAllowInvalidHostnames,
          ImmutableList.copyOf(authenticationConfigurations));
    }
  }
}
