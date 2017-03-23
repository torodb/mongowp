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
package com.eightkdata.mongowp.bson.netty;

import com.eightkdata.mongowp.bson.abst.AbstractBsonString;
import com.eightkdata.mongowp.bson.impl.StringBsonString;
import com.eightkdata.mongowp.bson.netty.annotations.ModifiesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Tight;
import com.google.common.base.Charsets;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.netty.buffer.ByteBuf;

import java.io.ObjectStreamException;

/**
 *
 */
@SuppressFBWarnings(value = {"SE_BAD_FIELD", "SE_NO_SERIALVERSIONID"},
    justification = "writeReplace is used")
public class NettyBsonString extends AbstractBsonString {

  private static final long serialVersionUID = 7152154519026299154L;

  @Tight
  private final ByteBuf byteBuf;

  public NettyBsonString(@Tight @ModifiesIndexes ByteBuf byteBuf) {
    this.byteBuf = byteBuf;
  }

  @Override
  public String getValue() {
    return getString(byteBuf);
  }

  private String getString(@Tight ByteBuf byteBuf) {
    int lenght = getStringLenght(byteBuf);
    byte[] bytes = new byte[lenght];
    byteBuf.getBytes(0, bytes);
    return new String(bytes, Charsets.UTF_8);
  }

  private int getStringLenght(@Tight ByteBuf byteBuf) {
    return byteBuf.readableBytes();
  }

  private Object writeReplace() throws ObjectStreamException {
    return new StringBsonString(getValue());
  }
}
