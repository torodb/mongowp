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
package com.eightkdata.mongowp.messages.request;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public enum RequestOpCode {
  OP_MSG(1000),
  OP_UPDATE(2001),
  OP_INSERT(2002),
  RESERVED(2003),
  OP_QUERY(2004),
  OP_GET_MORE(2005),
  OP_DELETE(2006),
  OP_KILL_CURSORS(2007);

  private final int opCode;

  private RequestOpCode(int opCode) {
    this.opCode = opCode;
  }

  public int getOpCode() {
    return opCode;
  }

  private static final Map<Integer, RequestOpCode> OP_CODES_MAP =
      new HashMap<Integer, RequestOpCode>(values().length);

  static {
    for (RequestOpCode o : values()) {
      OP_CODES_MAP.put(o.opCode, o);
    }
  }

  public static RequestOpCode getByOpcode(int opCode) {
    return OP_CODES_MAP.get(opCode);
  }

  public boolean canReply() {
    return this.equals(OP_QUERY) || this.equals(OP_GET_MORE);
  }
}
