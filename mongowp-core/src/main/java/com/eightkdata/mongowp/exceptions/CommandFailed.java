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
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;

/**
 *
 */
public class CommandFailed extends MongoException {

  private static final long serialVersionUID = 1L;

  private final String commandName;
  private final String reason;

  public CommandFailed(String commandName, String reason) {
    super(createCustomMessage(commandName, reason), ErrorCode.COMMAND_FAILED);
    this.commandName = commandName;
    this.reason = reason;
  }

  public CommandFailed(String commandName, String reason, Throwable cause) {
    super(createCustomMessage(commandName, reason), cause, ErrorCode.COMMAND_FAILED);
    this.commandName = commandName;
    this.reason = reason;
  }

  public String getCommandName() {
    return commandName;
  }

  public String getReason() {
    return reason;
  }

  private static String createCustomMessage(String commandName, String reason) {
    return "Execution of command '" + commandName + "' failed because " + reason;
  }
}
