/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bson-netty. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.bson.netty.pool;

import com.eightkdata.mongowp.bson.netty.annotations.ConservesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import io.netty.buffer.ByteBuf;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
 */
@ThreadSafe
public abstract class StringPoolPolicy {

    public abstract boolean apply(boolean likelyCacheable, @Tight
            @ConservesIndexes ByteBuf input);

    public StringPoolPolicy and(StringPoolPolicy other) {
        return new AndStringPoolPolicy(this, other);
    }

    public StringPoolPolicy or(StringPoolPolicy other) {
        return new OrStringPoolPolicy(this, other);
    }
}
