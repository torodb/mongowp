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

package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.bson.BsonDocument;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * A class that given a bson document that represents a request, returns the command that request
 * is trying to execute.
 */
public interface CommandLibrary {

  /**
   * A brief description of the version supported by this library.
   *
   * <p/>Examples: MongoDB 3.0
   */
  public String getSupportedVersion();

  /**
   * Returns a set with the supported commands or an empty optional.
   */
  public default Optional<Set<Command>> getSupportedCommands() {
    return asMap().map(
        map -> new HashSet<>(map.values())
    );
  }

  @Nullable
  public LibraryEntry find(BsonDocument requestDocument);

  public Optional<Map<String, Command>> asMap();

  public static interface LibraryEntry {

    Command getCommand();

    String getAlias();
  }
}
