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

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonBinary;
import com.eightkdata.mongowp.bson.BsonBoolean;
import com.eightkdata.mongowp.bson.BsonDateTime;
import com.eightkdata.mongowp.bson.BsonDbPointer;
import com.eightkdata.mongowp.bson.BsonDecimal128;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonDouble;
import com.eightkdata.mongowp.bson.BsonInt32;
import com.eightkdata.mongowp.bson.BsonInt64;
import com.eightkdata.mongowp.bson.BsonJavaScript;
import com.eightkdata.mongowp.bson.BsonJavaScriptWithScope;
import com.eightkdata.mongowp.bson.BsonMax;
import com.eightkdata.mongowp.bson.BsonMin;
import com.eightkdata.mongowp.bson.BsonNull;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.bson.BsonRegex;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.BsonTimestamp;
import com.eightkdata.mongowp.bson.BsonUndefined;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.impl.ByteArrayBsonBinary;
import com.eightkdata.mongowp.bson.impl.ByteArrayBsonObjectId;
import com.eightkdata.mongowp.bson.impl.DefaultBsonDbPointer;
import com.eightkdata.mongowp.bson.impl.DefaultBsonJavaScript;
import com.eightkdata.mongowp.bson.impl.DefaultBsonJavaScriptWithCode;
import com.eightkdata.mongowp.bson.impl.DefaultBsonRegex;
import com.eightkdata.mongowp.bson.impl.DefaultBsonTimestamp;
import com.eightkdata.mongowp.bson.impl.FalseBsonBoolean;
import com.eightkdata.mongowp.bson.impl.ListBsonArray;
import com.eightkdata.mongowp.bson.impl.LongBsonDateTime;
import com.eightkdata.mongowp.bson.impl.LongsBsonDecimal128;
import com.eightkdata.mongowp.bson.impl.MapBasedBsonDocument;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonDouble;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonInt32;
import com.eightkdata.mongowp.bson.impl.PrimitiveBsonInt64;
import com.eightkdata.mongowp.bson.impl.SimpleBsonMax;
import com.eightkdata.mongowp.bson.impl.SimpleBsonMin;
import com.eightkdata.mongowp.bson.impl.SimpleBsonNull;
import com.eightkdata.mongowp.bson.impl.SimpleBsonUndefined;
import com.eightkdata.mongowp.bson.impl.StringBsonDeprecated;
import com.eightkdata.mongowp.bson.impl.StringBsonString;
import com.eightkdata.mongowp.bson.impl.TrueBsonBoolean;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.google.common.primitives.UnsignedBytes;
import io.netty.buffer.ByteBuf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

/**
 *
 */
@Immutable
public class DefaultNettyBsonLowLevelReader extends NettyBsonLowLevelReader {

  @Inject
  public DefaultNettyBsonLowLevelReader(NettyStringReader stringReader) {
    super(stringReader);
  }

  @Override
  BsonArray readArray(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException {
    int length = byteBuf.readInt();
    int significantLenght = length - 4 - 1;

    ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

    byte b = byteBuf.readByte();
    assert b == 0x00;

    ArrayList<BsonValue<?>> list = new ArrayList<>();
    while (significantSlice.readableBytes() > 0) {
      list.add(readArrayEntry(significantSlice));
    }
    return new ListBsonArray(list);
  }

  @Override
  BsonBinary readBinary(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    int length = byteBuf.readInt();
    byte subtype = byteBuf.readByte();
    byte[] content = new byte[length];

    byteBuf.readBytes(content);

    return new ByteArrayBsonBinary(ParsingTools.getBinarySubtype(subtype), subtype, content);
  }

  @Override
  BsonDateTime readDateTime(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return new LongBsonDateTime(byteBuf.readLong());
  }

  @Override
  BsonDbPointer readDbPointer(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    String str = getStringReader().readString(byteBuf, false);

    byte[] bytes = new byte[12];
    byteBuf.readBytes(bytes);

    return new DefaultBsonDbPointer(str, new ByteArrayBsonObjectId(bytes));
  }

  @Override
  BsonValue<?> readDeprecated(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return new StringBsonDeprecated(getStringReader().readString(byteBuf, false));
  }

  @Override
  BsonDocument readDocument(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    int length = byteBuf.readInt();
    int significantLenght = length - 4 - 1;

    ByteBuf significantSlice = byteBuf.readSlice(significantLenght);

    byte b = byteBuf.readByte();
    assert b == 0x00;

    LinkedHashMap<String, BsonValue<?>> values = new LinkedHashMap<>();
    while (significantSlice.readableBytes() > 0) {
      Entry<?> entry = readDocumentEntry(significantSlice);
      values.put(entry.getKey(), entry.getValue());
    }
    return new MapBasedBsonDocument(values);
  }

  @Override
  BsonDocument readJavaScriptScope(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    return readDocument(byteBuf);
  }

  @Override
  BsonDouble readDouble(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return PrimitiveBsonDouble.newInstance(byteBuf.readDouble());
  }

  @Override
  BsonDecimal128 readDecimal128(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return LongsBsonDecimal128.newInstance(new BigDecimal(byteBuf.readDouble()));
  }
  
  
  @Override
  BsonBoolean readBoolean(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException {
    byte readByte = byteBuf.readByte();
    if (readByte == 0x00) {
      return FalseBsonBoolean.getInstance();
    }
    if (readByte == 0x01) {
      return TrueBsonBoolean.getInstance();
    }
    throw new NettyBsonReaderException("Unexpected boolean byte. 0x00 or "
        + "0x01 was expected, but 0x" + UnsignedBytes.toString(readByte, 16) + " was read");
  }

  @Override
  BsonInt32 readInt32(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return PrimitiveBsonInt32.newInstance(byteBuf.readInt());
  }

  @Override
  BsonInt64 readInt64(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return PrimitiveBsonInt64.newInstance(byteBuf.readLong());
  }

  @Override
  BsonJavaScript readJavaScript(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return new DefaultBsonJavaScript(getStringReader().readString(byteBuf, false));
  }

  @Override
  BsonJavaScriptWithScope readJavaScriptWithScope(@Loose @ModifiesIndexes ByteBuf byteBuf)
      throws NettyBsonReaderException {
    byteBuf.readInt();
    String js = getStringReader().readString(byteBuf, false);
    BsonDocument scope = readJavaScriptScope(byteBuf);

    return new DefaultBsonJavaScriptWithCode(js, scope);
  }

  @Override
  BsonMax readMax(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return SimpleBsonMax.getInstance();
  }

  @Override
  BsonMin readMin(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return SimpleBsonMin.getInstance();
  }

  @Override
  BsonNull readNull(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return SimpleBsonNull.getInstance();
  }

  @Override
  BsonObjectId readObjectId(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    byte[] bytes = new byte[12];
    byteBuf.readBytes(bytes);

    return new ByteArrayBsonObjectId(bytes);
  }

  @Override
  BsonRegex readRegex(@Loose @ModifiesIndexes ByteBuf byteBuf) throws NettyBsonReaderException {
    String pattern = getStringReader().readCString(byteBuf, false);
    String options = getStringReader().readCString(byteBuf, true);

    EnumSet<BsonRegex.Options> optionsSet = ParsingTools.parseRegexOptions(options);

    return new DefaultBsonRegex(optionsSet, pattern);
  }

  @Override
  BsonString readString(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return new StringBsonString(getStringReader().readString(byteBuf, false));
  }

  @Override
  BsonTimestamp readTimestamp(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    int ordinal = byteBuf.readInt();
    int seconds = byteBuf.readInt();
    return new DefaultBsonTimestamp(seconds, ordinal);
  }

  @Override
  BsonUndefined readUndefined(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    return SimpleBsonUndefined.getInstance();
  }

}
