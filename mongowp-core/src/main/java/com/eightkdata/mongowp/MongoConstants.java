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


package com.eightkdata.mongowp;

import com.eightkdata.mongowp.bson.BsonDouble;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonDouble;
import com.google.common.primitives.Ints;

/**
 *
 */
public class MongoConstants {

    //TODO: MAX_MESSAGE_SIZE_BYTES is a value that the server could change, so it should be configurable
    public static final int MAX_MESSAGE_SIZE_BYTES = 48 * 1000 * 1000;
    public static final int MESSAGE_LENGTH_FIELD_BYTES = Ints.BYTES;
    public static final int MESSAGE_HEADER_WITHOUT_LENGTH_FIELD_BYTES = Ints.BYTES + Ints.BYTES + Ints.BYTES;
    public static final int MESSAGE_HEADER_BYTES = MESSAGE_LENGTH_FIELD_BYTES + MESSAGE_HEADER_WITHOUT_LENGTH_FIELD_BYTES;
    
	public static final Double KO = (double) 0;
	public static final Double OK = (double) 1;

    public static final BsonDouble BSON_KO = PrimitiveBsonDouble.newInstance(0);
	public static final BsonDouble BSON_OK = PrimitiveBsonDouble.newInstance(1);

    private MongoConstants() {
    }

}
