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

package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandLibrary;
import com.eightkdata.mongowp.server.api.CommandLibrary.LibraryEntry;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GroupedCommandLibrary implements CommandLibrary {

  private final String supportedVersion;
  private final ImmutableList<CommandLibrary> subLibraries;

  public GroupedCommandLibrary(String supportedVersion,
      ImmutableList<CommandLibrary> subLibraries) {
    this.supportedVersion = supportedVersion;
    this.subLibraries = subLibraries;
  }

  @Override
  public String getSupportedVersion() {
    return supportedVersion;
  }

  @Override
  public LibraryEntry find(BsonDocument requestDocument) {
    for (CommandLibrary subLibrary : subLibraries) {
      LibraryEntry found = subLibrary.find(requestDocument);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  @Override
  public Optional<Map<String, Command>> asMap() {
    HashMap<String, Command> map = new HashMap<>();

    for (CommandLibrary subLibrary : subLibraries) {
      Map<String, Command> subEntries = subLibrary.asMap().orElse(null);
      if (subEntries == null) {
        return Optional.empty();
      }
      map.putAll(subEntries);
    }
    return Optional.of(map);
  }

}
