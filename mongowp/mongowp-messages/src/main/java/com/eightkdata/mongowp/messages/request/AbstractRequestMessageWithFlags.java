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

import com.eightkdata.mongowp.messages.util.EnumBitFlags;
import com.eightkdata.mongowp.messages.util.EnumInt32FlagsUtil;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 */
public abstract class AbstractRequestMessageWithFlags<T extends Enum<T> & EnumBitFlags> extends AbstractRequestMessage {
    @Nonnull protected final EnumSet<T> flags;

    protected AbstractRequestMessageWithFlags(@Nonnull RequestBaseMessage requestBaseMessage, EnumSet<T> flags) {
        super(requestBaseMessage);
        this.flags = flags;
    }

    protected AbstractRequestMessageWithFlags(
            @Nonnull RequestBaseMessage requestBaseMessage, @Nonnull Class<T> enumClass, int enumMask, int flags
    ) {
        this(requestBaseMessage, EnumInt32FlagsUtil.getActiveFlags(enumClass, enumMask, flags));
    }

    public boolean isFlagSet(T flag) {
        return flags.contains(flag);
    }

    @Nonnull
    public Set<T> getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
