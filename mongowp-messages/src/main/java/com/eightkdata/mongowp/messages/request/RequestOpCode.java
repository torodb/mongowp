/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
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

    private static final Map<Integer,RequestOpCode> OP_CODES_MAP = new HashMap<Integer,RequestOpCode>(values().length);
    static {
        for(RequestOpCode o : values()) {
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
