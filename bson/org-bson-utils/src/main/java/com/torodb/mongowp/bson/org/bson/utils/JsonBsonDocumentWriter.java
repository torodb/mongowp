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
import com.torodb.mongowp.bson.utils.BsonDocumentWriter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

/**
 *
 */
public class JsonBsonDocumentWriter implements BsonDocumentWriter<StringBuilder> {

  private JsonBsonDocumentWriter() {
  }

  @Override
  public void writeInto(StringBuilder sink, BsonDocument doc) {
    String json = MongoBsonTranslator.translate(doc)
        .toJson(new JsonWriterSettings(JsonMode.STRICT));

    sink.append(json);
  }

  public String writeIntoString(BsonDocument doc) {
    return MongoBsonTranslator.translate(doc)
        .toJson(new JsonWriterSettings(JsonMode.STRICT));
  }

  public static JsonBsonDocumentWriter getInstance() {
    return JsonBsonDocumentWriterHolder.INSTANCE;
  }

  private static class JsonBsonDocumentWriterHolder {

    private static final JsonBsonDocumentWriter INSTANCE = new JsonBsonDocumentWriter();
  }

  @SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
  private Object readResolve() {
    return JsonBsonDocumentWriter.getInstance();
  }

}
