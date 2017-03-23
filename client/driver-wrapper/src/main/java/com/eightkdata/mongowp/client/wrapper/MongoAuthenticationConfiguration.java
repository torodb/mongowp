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
package com.eightkdata.mongowp.client.wrapper;

public class MongoAuthenticationConfiguration {

  private final MongoAuthenticationMechanism mechanism;
  private final String user;
  private final String source;
  private final String password;

  public MongoAuthenticationConfiguration(MongoAuthenticationMechanism mechanism, String user,
      String source,
      String password) {
    super();
    this.mechanism = mechanism;
    this.user = user;
    this.source = source;
    this.password = password;
  }

  public MongoAuthenticationMechanism getMechanism() {
    return mechanism;
  }

  public String getUser() {
    return user;
  }

  public String getSource() {
    return source;
  }

  public String getPassword() {
    return password;
  }

  public static class Builder {

    private MongoAuthenticationMechanism mechanism;
    private String user;
    private String source;
    private String password;

    public Builder(MongoAuthenticationMechanism mechanism) {
      this.mechanism = mechanism;
    }

    public MongoAuthenticationMechanism getMechanism() {
      return mechanism;
    }

    public String getUser() {
      return user;
    }

    public Builder setUser(String user) {
      this.user = user;
      return this;
    }

    public String getSource() {
      return source;
    }

    public Builder setSource(String source) {
      this.source = source;
      return this;
    }

    public String getPassword() {
      return password;
    }

    public Builder setPassword(String password) {
      this.password = password;
      return this;
    }

    public MongoAuthenticationConfiguration build() {
      return new MongoAuthenticationConfiguration(mechanism, user, source, password);
    }
  }
}
