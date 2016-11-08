/*
 * MongoWP - MongoWP: Bson
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
package com.eightkdata.mongowp.bson.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class IntBaseHasher {
    private static final Logger LOGGER = LogManager.getLogger(IntBaseHasher.class);
    private static final HashFunction FUNCTION = Hashing.goodFastHash(32);

    public static int hash(int lenght) {
        int hash = FUNCTION
                .newHasher()
                .putInt(lenght)
                .hash()
                .asInt();
        if (hash == 0) {
            LOGGER.warn("Hash function returns 0");
            hash = 1;
        }
        return hash;
    }

    private IntBaseHasher() {
    }

}
