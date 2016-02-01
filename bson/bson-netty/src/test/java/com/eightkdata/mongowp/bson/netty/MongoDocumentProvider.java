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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import org.bson.BsonValue;

/**
 *
 */
public class MongoDocumentProvider {

    private static final String testDocumentsFileName = "test-documents.json";

    public static Collection<Object[]> readTestDocuments() throws IOException {
        try (InputStream is = MongoDocumentProvider.class.getResourceAsStream('/' + testDocumentsFileName)) {
            BsonValue value = MongoBsonUtils.read(is);

            if (!value.isDocument()) {
                throw new AssertionError("A JSON document on \"extended\" JavaScript "
                        + "format was expected, but a " + value.getBsonType()
                        + " was found");
            }
            List<Object[]> parameters = new ArrayList<>();
            for (Entry<String, BsonValue> entry : value.asDocument().entrySet()) {
                assert entry.getValue().isDocument() :
                        "/" + testDocumentsFileName
                        + " contains the illegal value for key '"
                        + entry.getKey() + "'. A document was expected but a "
                        + entry.getValue().getBsonType() + " was found";
                parameters.add(new Object[]{entry.getKey(), entry.getValue().asDocument()});
            }
            return parameters;
        }
    }

}
