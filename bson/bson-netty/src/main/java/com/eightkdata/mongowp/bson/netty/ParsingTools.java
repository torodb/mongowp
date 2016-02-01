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

package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.BinarySubtype;
import com.eightkdata.mongowp.bson.BsonRegex.Options;
import com.eightkdata.mongowp.bson.BsonType;
import com.google.common.primitives.UnsignedBytes;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.eightkdata.mongowp.bson.BsonType.*;

/**
 *
 */
class ParsingTools {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ParsingTools.class);

    private static final byte FIRST_USER_DEFINED = UnsignedBytes.parseUnsignedByte("80", 16);

    /**
     * Translate a byte to the {@link BsonType} it represents, as specified on
     * the <a href="http://bsonspec.org/spec.html">BSON Spec</a>
     *
     * @param typeByte
     * @return
     * @throws NettyBsonReaderException
     */
    @Nonnull
    protected static BsonType getBsonType(byte typeByte) throws NettyBsonReaderException {
        switch (typeByte) {
            case 0x01: return DOUBLE;
            case 0x02: return STRING;
            case 0x03: return DOCUMENT;
            case 0x04: return ARRAY;
            case 0x05: return BINARY;
            case 0x06: return UNDEFINED;
            case 0x07: return OBJECT_ID;
            case 0x08: return BOOLEAN;
            case 0x09: return DATETIME;
            case 0x0A: return NULL;
            case 0x0B: return REGEX;
            case 0x0C: return DB_POINTER;
            case 0x0D: return JAVA_SCRIPT;
            case 0x0E: return DEPRECTED;
            case 0x0F: return JAVA_SCRIPT_WITH_SCOPE;
            case 0x10: return INT32;
            case 0x11: return TIMESTAMP;
            case 0x12: return INT64;
            case UnsignedBytes.MAX_VALUE: return MIN;
            case 0x7F: return MAX;
        }
        throw new NettyBsonReaderException("It is not defined the type associated with the byte " + UnsignedBytes.toString(typeByte, 16));
    }

    protected static byte getByte(BsonType bsonType) throws NettyBsonReaderException {
        switch (bsonType) {
            case DOUBLE: return 0x01;
            case STRING: return 0x02;
            case DOCUMENT: return 0x03;
            case ARRAY: return 0x04;
            case BINARY: return 0x05;
            case UNDEFINED: return 0x06;
            case OBJECT_ID: return 0x07;
            case BOOLEAN: return 0x08;
            case DATETIME: return 0x09;
            case NULL: return 0x0A;
            case REGEX: return 0x0B;
            case DB_POINTER: return 0x0C;
            case JAVA_SCRIPT: return 0x0D;
            case DEPRECTED: return 0x0E;
            case JAVA_SCRIPT_WITH_SCOPE: return 0x0F;
            case INT32: return 0x10;
            case TIMESTAMP: return 0x11;
            case INT64: return 0x12;
            case MIN: return UnsignedBytes.MAX_VALUE;
            case MAX: return 0x7F;
        }
        throw new NettyBsonReaderException("It is not defined the byte associated with the type " + bsonType);
    }


    protected static BinarySubtype getBinarySubtype(byte readByte) {
        switch (readByte) {
            case 0x00:
                return BinarySubtype.GENERIC;
            case 0x01:
                return BinarySubtype.FUNCTION;
            case 0x02:
                return BinarySubtype.OLD_BINARY;
            case 0x03:
                return BinarySubtype.OLD_UUID;
            case 0x04:
                return BinarySubtype.UUID;
            case 0x05:
                return BinarySubtype.MD5;
            default: {
                if (UnsignedBytes.compare(readByte, FIRST_USER_DEFINED) >= 0) {
                    return BinarySubtype.USER_DEFINED;
                }
                else {
                    throw new AssertionError("Unrecognized binary type 0x" + UnsignedBytes.toString(readByte, 16));
                }
            }
        }
    }

    protected static EnumSet<Options> parseRegexOptions(String optionsStr) {
        EnumSet<Options> result = EnumSet.noneOf(Options.class);

        if (optionsStr.isEmpty()) {
            return result;
        }

        int i = 0;
        if (optionsStr.charAt(i) == 'i') {
            result.add(Options.CASE_INSENSITIVE);

            i++;
            if (i >= optionsStr.length()) {
                return result;
            }
        }
        if (optionsStr.charAt(i) == 'l') {
            result.add(Options.LOCALE_DEPENDENT);

            i++;
            if (i >= optionsStr.length()) {
                return result;
            }
        }
        if (optionsStr.charAt(i) == 'm') {
            result.add(Options.MULTILINE_MATCHING);

            i++;
            if (i >= optionsStr.length()) {
                return result;
            }
        }
        if (optionsStr.charAt(i) == 's') {
            result.add(Options.DOTALL_MODE);

            i++;
            if (i >= optionsStr.length()) {
                return result;
            }
        }
        if (optionsStr.charAt(i) == 'u') {
            result.add(Options.UNICODE);

            i++;
            if (i >= optionsStr.length()) {
                return result;
            }
        }
        if  (optionsStr.charAt(i) == 'x') {
            result.add(Options.VERBOSE_MODE);

            i++;
            if (i >= optionsStr.length()) {
                return result;
            }
        }

        assert i < optionsStr.length();
        LOGGER.warn("Unexpected regex options '{}'", optionsStr.substring(i));

        return result;
    }

}
