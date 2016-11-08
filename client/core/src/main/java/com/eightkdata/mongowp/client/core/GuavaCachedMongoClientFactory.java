/*
 * MongoWP - Mongo Client: Core
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Mongo Client: Core - MongoWP Client core project, used by other client projects to share
        common platform independent implementations
 * Copyright © 2014 8Kdata (www.8kdata.com)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Mongo Client: Core - MongoWP Client core project, used by other client projects to share
        common platform independent implementations
 * Copyright © 2014 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Mongo Client: Core - MongoWP Client core project, used by other client projects to share
        common platform independent implementations
 * Copyright © ${project.inceptionYear} 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * Mongo Client: Core - MongoWP Client core project, used by other client projects to share
        common platform independent implementations
 * Copyright © ${project.inceptionYear} ${owner} (${email})
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.client.core;

import com.eightkdata.mongowp.MongoVersion;
import com.google.common.base.Preconditions;
import com.google.common.cache.*;
import com.google.common.net.HostAndPort;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
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
