/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.torodb.mongowp.bson.netty;

import org.bson.BsonValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 */
public class MongoDocumentProvider {

  private static final String testDocumentsFileName = "test-documents.json";

  public static Collection<Object[]> readTestDocuments() throws IOException {
    try (InputStream is =
        MongoDocumentProvider.class.getResourceAsStream('/' + testDocumentsFileName)) {
      BsonValue value = MongoBsonUtils.read(is);

      if (!value.isDocument()) {
        throw new AssertionError("A JSON document on \"extended\" JavaScript "
            + "format was expected, but a " + value.getBsonType() + " was found");
      }
      List<Object[]> parameters = new ArrayList<>();
      for (Entry<String, BsonValue> entry : value.asDocument().entrySet()) {
        assert entry.getValue().isDocument() : "/" + testDocumentsFileName
            + " contains the illegal value for key '" + entry.getKey()
            + "'. A document was expected but a " + entry.getValue().getBsonType() + " was found";
        parameters.add(new Object[]{entry.getKey(), entry.getValue().asDocument()});
      }
      return parameters;
    }
  }

}
