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

import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import com.torodb.mongowp.client.core.MongoClientFactory;
import com.torodb.mongowp.client.core.UnreachableMongoServerException;

/**
 *
 */
public class MongoClientWrapperFactory implements MongoClientFactory {

  private final MongoClientConfiguration mongoClientConfiguration;

  @Inject
  public MongoClientWrapperFactory(MongoClientConfiguration mongoClientConfiguration) {
    this.mongoClientConfiguration = mongoClientConfiguration;
  }

  @Override
  public MongoClientWrapper createClient(HostAndPort address)
      throws UnreachableMongoServerException {
    return new MongoClientWrapper(mongoClientConfiguration.builder(address).build());
  }

}
