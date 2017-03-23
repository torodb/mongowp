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

import static org.junit.Assert.assertEquals;

import com.torodb.mongowp.bson.netty.pool.AlwaysFalseStringPool;
import com.torodb.mongowp.bson.org.bson.utils.MongoBsonTranslator;
import com.torodb.mongowp.bson.utils.BsonDocumentReader.AllocationType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bson.BsonDocument;
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
public class NettyBsonDocumentReaderTest {

  private final ByteBuf byteBuf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

  private static final NettyStringReader STRING_READER =
      new PooledNettyStringReader(AlwaysFalseStringPool.getInstance());
  private final NettyBsonDocumentReader reader =
      new NettyBsonDocumentReader(new DefaultNettyBsonLowLevelReader(STRING_READER),
          new OffHeapNettyBsonLowLevelReader(STRING_READER),
          new OffHeapValuesNettyBsonLowLevelReader(STRING_READER));

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

  private void test(AllocationType allocationType) throws NettyBsonReaderException, IOException {
    assert mongoDoc != null : "A null document parameter has been injected";

    MongoBsonUtils.write(mongoDoc, byteBuf);

    com.torodb.mongowp.bson.BsonDocument wpDocument =
        reader.readDocument(allocationType, byteBuf);

    BsonDocument written = MongoBsonTranslator.translate(wpDocument);

    assertEquals(mongoDoc, written);

  }

  @Test
  public void readHeapTest() throws NettyBsonReaderException, IOException {
    test(AllocationType.HEAP);
  }

  @Test
  public void readOffHeapTest() throws NettyBsonReaderException, IOException {
    test(AllocationType.OFFHEAP);
  }

  @Test
  public void readOffHeapValuesTest() throws NettyBsonReaderException, IOException {
    test(AllocationType.OFFHEAP_VALUES);
  }
}
