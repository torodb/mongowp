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
import io.netty.buffer.ByteBuf;

/**
 *
 */
public interface NettyStringReader {

  /**
   * A method that reads a C-string from a ByteBuf. This method modified the internal state of the
   * ByteBuf, advancing the read pointer to the position after the cstring.
   *
   * @param buffer
   * @param likelyCacheable
   * @return The C-String as a String object or null if there was no C-String in the ByteBuf
   * @throws com.torodb.mongowp.bson.netty.NettyBsonReaderException
   */
  public String readCString(ByteBuf buffer, boolean likelyCacheable)
      throws NettyBsonReaderException;

  /**
   * A method that skips a C-string from a ByteBuf. This method modified the internal state of the
   * ByteBuf, advancing the read pointer to the position after the cstring.
   *
   * @param buffer
   * @throws com.torodb.mongowp.bson.netty.NettyBsonReaderException
   */
  public void skipCString(ByteBuf buffer) throws NettyBsonReaderException;

  public String readString(@Loose @ModifiesIndexes ByteBuf byteBuf, boolean likelyCacheable);

  public ByteBuf readStringAsSlice(@Loose @ModifiesIndexes ByteBuf byteBuf);
}
