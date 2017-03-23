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

import com.torodb.mongowp.bson.netty.annotations.Loose;
import com.torodb.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.torodb.mongowp.bson.netty.pool.StringPool;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;

import javax.inject.Inject;

/**
 *
 */
@SuppressFBWarnings(value = "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT",
    justification = "It seems FindBugs considers ByteBuf methods are not side effect")
public class PooledNettyStringReader implements NettyStringReader {

  public static final byte CSTRING_BYTE_TERMINATION = 0x00;

  private final StringPool stringPool;

  @Inject
  public PooledNettyStringReader(StringPool stringPool) {
    this.stringPool = stringPool;
  }

  /**
   * A method that reads a C-string from a ByteBuf. This method modified the internal state of the
   * ByteBuf, advancing the read pointer to the position after the cstring.
   *
   * @param buffer
   * @param likelyCacheable
   * @return The C-String as a String object or null if there was no C-String in the ByteBuf
   * @throws com.torodb.mongowp.bson.netty.NettyBsonReaderException
   */
  @Override
  public String readCString(ByteBuf buffer, boolean likelyCacheable)
      throws NettyBsonReaderException {
    int pos = buffer.bytesBefore(CSTRING_BYTE_TERMINATION);
    if (pos == -1) {
      throw new NettyBsonReaderException("A cstring was expected but no 0x00 byte was found");
    }

    String result = stringPool.fromPool(likelyCacheable, buffer.readSlice(pos));

    buffer.readByte(); // Discard the termination byte

    return result;
  }

  @Override
  /**
   * A method that skips a C-string from a ByteBuf. This method modified the internal state of the
   * ByteBuf, advancing the read pointer to the position after the cstring.
   *
   * @param buffer
   * @throws com.eightkdata.mongowp.bson.netty.NettyBsonReaderException
   */
  public void skipCString(ByteBuf buffer) throws NettyBsonReaderException {
    int bytesBefore = buffer.bytesBefore(CSTRING_BYTE_TERMINATION);
    if (bytesBefore == -1) {
      throw new NettyBsonReaderException("A cstring was expected but no 0x00 byte was found");
    }
    buffer.skipBytes(bytesBefore + 1);
  }

  @Override
  public String readString(@Loose @ModifiesIndexes ByteBuf byteBuf, boolean likelyCacheable) {
    int stringLength = byteBuf.readInt();

    String str = stringPool.fromPool(likelyCacheable,
        byteBuf.slice(byteBuf.readerIndex(), stringLength - 1));

    byteBuf.skipBytes(stringLength);

    return str;
  }

  @Override
  public ByteBuf readStringAsSlice(@Loose @ModifiesIndexes ByteBuf byteBuf) {
    int stringLength = byteBuf.readInt();
    ByteBuf result = byteBuf.readSlice(stringLength - 1);
    byte b = byteBuf.readByte(); // discard the last 0x00
    assert b == 0x00;

    return result;
  }
}
