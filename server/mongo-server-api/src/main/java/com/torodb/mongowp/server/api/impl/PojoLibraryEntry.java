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
package com.torodb.mongowp.server.api.impl;

import com.torodb.mongowp.server.api.Command;
import com.torodb.mongowp.server.api.CommandLibrary.LibraryEntry;

/**
 *
 */
public class PojoLibraryEntry implements LibraryEntry {

  private final String alias;
  private final Command<?, ?> command;

  public PojoLibraryEntry(String alias, Command<?, ?> command) {
    this.alias = alias;
    this.command = command;
  }

  @Override
  public Command getCommand() {
    return command;
  }

  @Override
  public String getAlias() {
    return alias;
  }

}
