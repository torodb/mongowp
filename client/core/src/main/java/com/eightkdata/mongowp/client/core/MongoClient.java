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

import com.eightkdata.mongowp.MongoVersion;
import com.google.common.net.HostAndPort;

import java.io.Closeable;

import javax.annotation.Nullable;

/**
 *
 */
public interface MongoClient extends Closeable {

  /**
   * Returns the {@link HostAndPort} of the server or null if it is a local client.
   *
   * @return
   */
  @Nullable
  public HostAndPort getAddress();

  public MongoVersion getMongoVersion();

  public MongoConnection openConnection();

  @Override
  public void close();

  public boolean isClosed();

  public boolean isRemote();
}
