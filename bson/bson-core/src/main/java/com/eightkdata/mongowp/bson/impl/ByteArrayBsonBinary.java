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
package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.BinarySubtype;
import com.eightkdata.mongowp.bson.abst.AbstractBsonBinary;
import com.eightkdata.mongowp.bson.utils.NonIoByteSource;
import com.google.common.io.ByteSource;

import java.util.Arrays;

/**
 *
 */
public class ByteArrayBsonBinary extends AbstractBsonBinary {

  private static final long serialVersionUID = 6462169816370072534L;

  private final byte numericSubType;
  private final BinarySubtype subtype;
  private final NonIoByteSource byteSource;

  public ByteArrayBsonBinary(BinarySubtype subtype, byte numericSubType, byte[] array) {
    this.subtype = subtype;
    this.numericSubType = numericSubType;
    this.byteSource = new NonIoByteSource(ByteSource.wrap(Arrays.copyOf(array, array.length)));
  }

  @Override
  public byte getNumericSubType() {
    return numericSubType;
  }

  @Override
  public BinarySubtype getSubtype() {
    return subtype;
  }

  @Override
  public int size() {
    long size = byteSource.size();
    assert size <= Integer.MAX_VALUE;
    return (int) size;
  }

  @Override
  public NonIoByteSource getByteSource() {
    return byteSource;
  }

}
