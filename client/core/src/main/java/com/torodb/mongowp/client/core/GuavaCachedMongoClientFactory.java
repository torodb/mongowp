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
package com.torodb.mongowp.client.core;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import com.google.common.net.HostAndPort;
import com.torodb.mongowp.MongoVersion;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCachedMongoClientFactory implements CachedMongoClientFactory {

  private final MongoClientFactory factory;
  private final Cache<HostAndPort, CachedMongoClient> cachedClients;

  public GuavaCachedMongoClientFactory(MongoClientFactory factory,
      Duration entryDuration) {
    Preconditions.checkArgument(entryDuration.getSeconds() > 0,
        "Cache duration must be higher than 0 seconds");
    this.factory = factory;
    cachedClients = CacheBuilder.newBuilder()
        .expireAfterAccess(entryDuration.getSeconds(), TimeUnit.SECONDS)
        .removalListener(this::onRemoval)
        .build();
  }

  @Override
  public Optional<MongoClient> getCachedClient(HostAndPort address) {
    return Optional.of(cachedClients.getIfPresent(address));
  }

  @Override
  public MongoClient createClient(HostAndPort address) throws
      UnreachableMongoServerException {
    try {
      return cachedClients.get(address, () -> {
        return new CachedMongoClient(factory.createClient(address));
      });
    } catch (ExecutionException ex) {
      Throwable cause = ex.getCause();
      if (cause instanceof UnreachableMongoServerException) {
        throw (UnreachableMongoServerException) cause;
      }
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void invalidate(HostAndPort address) {
    cachedClients.invalidate(address);
  }

  @Override
  public void invalidateAll() {
    cachedClients.invalidateAll();
  }

  private void onRemoval(RemovalNotification<HostAndPort, CachedMongoClient> notification) {
    notification.getValue().delegate.close();
  }

  private static class CachedMongoClient implements MongoClient {

    private final MongoClient delegate;

    public CachedMongoClient(MongoClient delegate) {
      this.delegate = delegate;
    }

    @Override
    public HostAndPort getAddress() {
      return delegate.getAddress();
    }

    @Override
    public MongoVersion getMongoVersion() {
      return delegate.getMongoVersion();
    }

    @Override
    public MongoConnection openConnection() {
      return delegate.openConnection();
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
      return false;
    }

    @Override
    public boolean isRemote() {
      return delegate.isRemote();
    }
  }

}
