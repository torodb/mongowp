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
