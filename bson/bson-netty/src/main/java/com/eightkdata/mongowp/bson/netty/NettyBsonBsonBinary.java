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

import com.eightkdata.mongowp.bson.BinarySubtype;
import com.eightkdata.mongowp.bson.abst.AbstractBsonBinary;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import com.eightkdata.mongowp.bson.utils.NonIoByteSource;
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
