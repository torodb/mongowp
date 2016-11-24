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

package com.eightkdata.mongowp.bson.netty;

import static org.junit.Assert.*;

import com.eightkdata.mongowp.bson.org.bson.utils.MongoBsonTranslator;
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

    com.eightkdata.mongowp.bson.BsonDocument wpDocument = MongoBsonTranslator.translate(mongoDoc);

    writer.writeInto(byteBuf, wpDocument);

    org.bson.BsonDocument read = MongoBsonUtils.read(byteBuf);

    assertEquals(mongoDoc, read);
  }

}
