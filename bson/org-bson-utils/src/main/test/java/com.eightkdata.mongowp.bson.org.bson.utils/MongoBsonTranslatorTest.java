/*
 * MongoWP
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.bson.org.bson.utils;

import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class MongoBsonTranslatorTest {

    @Parameterized.Parameters(name = "{index}: Collection {0}")
    public static Collection<Object[]> data() {

        Collection<Object[]> allTests = Arrays.asList(new Object[][]{
                {"ARRAY", new org.bson.BsonArray()},
                {"BINARY", new org.bson.BsonBinary(new BigInteger("123456789123abcdef456789", 16).toByteArray())},
                {"BOOLEAN", org.bson.BsonBoolean.TRUE},
                {"DATE_TIME", new org.bson.BsonDateTime(1482000000)},
                {"DB_POINTER", new org.bson.BsonDbPointer("namespace", new org.bson.types.ObjectId("123456789123abcdef456789"))},
                {"DECIMAL128", new org.bson.BsonDecimal128( new org.bson.types.Decimal128(18283340L))},
                {"DOCUMENT", new org.bson.BsonDocument("key",new org.bson.BsonInt32(456))},
                {"DOUBLE", new org.bson.BsonDouble(2.3)},
                {"INT32", new org.bson.BsonInt32(2)},
                {"INT64", new org.bson.BsonInt64(2L)},
                {"JAVASCRIPT", new org.bson.BsonJavaScript("alert(\"hello\");")},
                {"JAVASCRIPT_WITH_SCOPE", new org.bson.BsonJavaScriptWithScope("alert(\"hello\");", new BsonDocument("key",new org.bson.BsonInt32(456)))},
                {"MAX_KEY", new org.bson.BsonMaxKey()},
                {"MIN_KEY", new org.bson.BsonMinKey()},
                {"NULL", new org.bson.BsonNull()},
                {"OBJECT_ID", new org.bson.BsonObjectId(new org.bson.types.ObjectId("123456789123abcdef456789"))},
                {"REGULAR_EXPRESSION", new org.bson.BsonRegularExpression("hello", "is")},
                {"STRING", new org.bson.BsonString("hello")},
                {"SYMBOL", new org.bson.BsonSymbol("hello")},
                {"TIMESTAMP", new org.bson.BsonTimestamp()},
                {"UNDEFINED", new org.bson.BsonUndefined()}
        });

        Arrays.asList(org.bson.BsonType.values()).forEach(
                type -> assertTrue(type + " type is never tested",
                        MongoBsonTranslator.deprecatedTypes.contains(type) ||
                        allTests.stream().anyMatch(
                        toTest -> type.name().compareTo((String)toTest[0]) == 0
                        )
                )
        );

        return allTests;
    }

    @Test
    public void test(){
        org.bson.BsonDocument doc = new BsonDocument();
        doc.append(collName,value);

        //We test the idempotency of moving between BsonSpec and our Bson model
        //Testing also the idempotency of moving between our Bson model and BsonSpec is the reason
        //of doing 4 translations instead of 2
        try {
            BsonDocument finalDoc = MongoBsonTranslator.translate(
                    MongoBsonTranslator.translate(
                            MongoBsonTranslator.translate(
                                    MongoBsonTranslator.translate(doc)
                    ))
            );
            assertTrue(doc.equals(finalDoc));
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue("Execution aborted due to exception",false);
        }

    }


    @Parameterized.Parameter(0)
    private String collName;

    @Parameterized.Parameter(1)
    private BsonValue value;

}