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

import static org.junit.Assert.*;

import com.torodb.mongowp.bson.org.bson.utils.MongoBsonTranslator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bson.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Collection;

/**
 *
 * @author gortiz
 */
@RunWith(Parameterized.class)
public class NettyBsonDocumentWriterTest {

  private final ByteBuf byteBuf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

  private final NettyBsonDocumentWriter writer = new NettyBsonDocumentWriter();

  @Parameters(name = "{0}")
  public static Collection<Object[]> documents() throws IOException {
    return MongoDocumentProvider.readTestDocuments();
  }

  @Parameter(0)
  public String name;

  @Parameter(1)
  public BsonDocument mongoDoc;

  @Before
  public void setUp() {
    byteBuf.clear();
  }

  @Test
  public void writeTest() throws NettyBsonReaderException {
    assert mongoDoc != null : "A null document parameter has been injected";

    com.torodb.mongowp.bson.BsonDocument wpDocument = MongoBsonTranslator.translate(mongoDoc);

    writer.writeInto(byteBuf, wpDocument);

    org.bson.BsonDocument read = MongoBsonUtils.read(byteBuf);

    assertEquals(mongoDoc, read);
  }

}
