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
package com.torodb.mongowp.bson.org.bson.utils;

import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.utils.BsonDocumentReader;
import com.torodb.mongowp.bson.utils.BsonDocumentReaderException;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bson.json.JsonParseException;

/**
 *
 */
public class JsonBsonDocumentReader implements BsonDocumentReader<String> {

  private JsonBsonDocumentReader() {
  }

  @Override
  public BsonDocument readDocument(AllocationType allocationType, String source) throws
      BsonDocumentReaderException {
    try {
      org.bson.BsonDocument doc = org.bson.BsonDocument.parse(source);
      return MongoBsonTranslator.translate(doc);
    } catch (JsonParseException ex) {
      throw new BsonDocumentReaderException(ex);
    }
  }

  public static JsonBsonDocumentReader getInstance() {
    return JsonBsonDocumentReaderHolder.INSTANCE;
  }

  private static class JsonBsonDocumentReaderHolder {

    private static final JsonBsonDocumentReader INSTANCE = new JsonBsonDocumentReader();
  }

  @SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
  private Object readResolve() {
    return JsonBsonDocumentReader.getInstance();
  }

}
