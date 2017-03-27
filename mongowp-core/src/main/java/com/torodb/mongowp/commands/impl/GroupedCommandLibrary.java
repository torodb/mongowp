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
package com.torodb.mongowp.commands.impl;

import com.google.common.collect.ImmutableList;
import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.commands.Command;
import com.torodb.mongowp.commands.CommandLibrary;
import com.torodb.mongowp.commands.CommandLibrary.LibraryEntry;

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
