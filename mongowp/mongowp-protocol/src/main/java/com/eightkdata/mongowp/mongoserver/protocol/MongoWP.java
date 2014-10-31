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


package com.eightkdata.mongowp.mongoserver.protocol;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

/**
 *
 */
public class MongoWP {
	public static final int VERSION_MAJOR = 2;
	public static final int VERSION_MINOR = 6;
	public static final int VERSION_PATCH = 5;
	public static final String VERSION_STRING = 
			VERSION_MAJOR + "." + 
			VERSION_MINOR + "." + 
			VERSION_PATCH;
	public static final ImmutableList VERSION = ImmutableList.of(
		MongoWP.VERSION_MAJOR, 
		MongoWP.VERSION_MINOR, 
		MongoWP.VERSION_PATCH 
    );
	
    public static final int MAX_WIRE_VERSION = 2;
    public static final int MIN_WIRE_VERSION = 0;
    
    /**
     * Obtained from
     * <a href="http://docs.mongodb.org/manual/reference/limits/">MongoDB Limits and Thresholds</a>.
     *
     */
    public static final int MAX_BSON_DOCUMENT_SIZE = 16 * 1024 * 1024;
    
    public static final int MAX_WRITE_BATCH_SIZE = 1000;
    
    /**
     * Obtained from
     * <a href="https://github.com/mongodb/mongo/blob/v2.6/src/mongo/util/net/message.h">mongo / src / mongo / util / net / message.h</a>.
     * Also explained in the <a href="http://docs.mongodb.org/master/reference/command/isMaster/">isMaster function</a>
     *
     */
    public static final int MAX_MESSAGE_SIZE_BYTES = 48 * 1000 * 1000;

    public static final int MESSAGE_LENGTH_FIELD_BYTES = Ints.BYTES;
    public static final int MESSAGE_HEADER_WITHOUT_LENGTH_FIELD_BYTES = Ints.BYTES + Ints.BYTES + Ints.BYTES;
    public static final int MESSAGE_HEADER_BYTES = MESSAGE_LENGTH_FIELD_BYTES + MESSAGE_HEADER_WITHOUT_LENGTH_FIELD_BYTES;

    /**
     * Obtained from
     * <a href="http://docs.mongodb.org/manual/core/cursors/">Cursors / Cursor Batches</a>.
     */
	public static final int MONGO_CURSOR_LIMIT = 101;
    
	public static final Double KO = Double.valueOf(0);
	public static final Double OK = Double.valueOf(1);

    public enum ErrorCode {
        // TODO: the following are not true Mongo error codes. Find the really used ones
    	WRONG_FIELD_TYPE(9, "Wrong type for '{0}' field, expected {1}, found {2}: {3}"),
    	MUST_RUN_ON_ADMIN(13, "{0}  may only be run against the admin database."),
        NO_SUCH_COMMAND(59, "No such command: {0}"),
        INTERNAL_ERROR(1000001, "Internal error: {0}"),
        UNIMPLEMENTED_COMMAND(1000002, "Unimplemented command: {0}"),
        UNIMPLEMENTED_FLAG(1000003, "Unimplemented flag: {0}"),
        INVALID_GET_LOG_LOG(1000004, "Invalid log option for getLog command: {0}");

        private final int errorCode;
        private final String errorMessage;

        ErrorCode(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
