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

import javax.net.SocketFactory;

public class MongoClientConfiguration {

  private final HostAndPort hostAndPort;
  private final MongoClientConfigurationProperties properties;

  public MongoClientConfiguration(HostAndPort hostAndPort, 
      MongoClientConfigurationProperties properties) {
    super();
    this.hostAndPort = hostAndPort.withDefaultPort(27017);
    this.properties = properties;
  }

  public HostAndPort getHostAndPort() {
    return hostAndPort;
  }

  public SocketFactory getSocketFactory() {
    return properties.getSocketFactory();
  }

  public boolean isSslEnabled() {
    return properties.isSslEnabled();
  }

  public boolean isSslAllowInvalidHostnames() {
    return properties.isSslAllowInvalidHostnames();
  }

  public ImmutableList<MongoAuthenticationConfiguration> getAuthenticationConfigurations() {
    return properties.getAuthenticationConfigurations();
  }

  public Builder builder(HostAndPort hostAndPort) {
    Builder builder = new Builder(hostAndPort);
    builder.properties = properties;

    return builder;
  }

  @Override
  public String toString() {
    return hostAndPort.toString();
  }

  public static class Builder {

    private HostAndPort hostAndPort;
    private MongoClientConfigurationProperties properties;

    public Builder(HostAndPort hostAndPort) {
      this.hostAndPort = hostAndPort;
    }

    public Builder setProperties(MongoClientConfigurationProperties base) {
      this.properties = base;
      return this;
    }

    @SuppressWarnings("checkstyle:JavadocMethod")
    public MongoClientConfiguration build() {
      return new MongoClientConfiguration(hostAndPort, properties);
    }
  }
}
