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

import com.torodb.mongowp.bson.BinarySubtype;
import com.torodb.mongowp.bson.abst.AbstractBsonBinary;
import com.torodb.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.torodb.mongowp.bson.netty.annotations.Tight;
import com.torodb.mongowp.bson.utils.NonIoByteSource;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public class NettyBsonBsonBinary extends AbstractBsonBinary {

  private static final long serialVersionUID = 6766481057628149423L;

  private final byte numericSubtype;
  private final int length;
  private final BinarySubtype subtype;
  private final NonIoByteSource byteSource;

  public NettyBsonBsonBinary(byte numericSubtype, BinarySubtype subtype,
      @Tight @ModifiesIndexes ByteBuf data) {
    this.numericSubtype = numericSubtype;
    this.subtype = subtype;
    length = data.readableBytes();
    byteSource = new NonIoByteSource(new ByteBufByteSource(data));
  }

  @Override
  public byte getNumericSubType() {
    return numericSubtype;
  }

  @Override
  public BinarySubtype getSubtype() {
    return subtype;
  }

  @Override
  public int size() {
    return length;
  }

  @Override
  public NonIoByteSource getByteSource() {
    return byteSource;
  }

}
