/*
 * MongoWP - MongoWP: Core
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
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
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
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
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
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
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
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
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

package com.eightkdata.mongowp.messages.request;

import java.util.Arrays;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 */
public class KillCursorsMessage extends AbstractRequestMessage {

    public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_KILL_CURSORS;

    @Override
    public RequestOpCode getOpCode() {
        return REQUEST_OP_CODE;
    }

    @Nonnegative private final int numberOfCursors;
    @Nonnull private final long[] cursorIds;

    public KillCursorsMessage(
            @Nonnull RequestBaseMessage requestBaseMessage, 
            int numberOfCursors, @Nonnull long[] cursorIds
    ) {
    	super(requestBaseMessage, EmptyBsonContext.getInstance());
        this.numberOfCursors = numberOfCursors;
        this.cursorIds = Arrays.copyOf(cursorIds, cursorIds.length);
    }

    public int getNumberOfCursors() {
        return numberOfCursors;
    }

    @Nonnull
    public long[] getCursorIds() {
        return Arrays.copyOf(cursorIds, cursorIds.length);
    }

    @Override
    public String toString() {
        return "KillCursorsMessage{" + super.toString() +
                ", numberOfCursors=" + numberOfCursors +
                ", cursorIds=" + Arrays.toString(cursorIds) +
                '}';
    }

}