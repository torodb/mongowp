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

import io.netty.buffer.ByteBuf;

/**
 *
 */
public class NeverStringPoolPolicy extends StringPoolPolicy {

    private NeverStringPoolPolicy() {
    }

    @Override
    public boolean apply(boolean likelyCacheable, ByteBuf input) {
        return false;
    }

    @Override
    public StringPoolPolicy or(StringPoolPolicy other) {
        return other;
    }

    @Override
    public StringPoolPolicy and(StringPoolPolicy other) {
        return this;
    }

    @Override
    public String toString() {
        return "never";
    }
    
    public static NeverStringPoolPolicy getInstance() {
        return NeverStringPoolPolicyHolder.INSTANCE;
    }

    private static class NeverStringPoolPolicyHolder {
        private static final NeverStringPoolPolicy INSTANCE = new NeverStringPoolPolicy();
    }

    //@edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
    private Object readResolve()  {
        return NeverStringPoolPolicy.getInstance();
    }
 }
