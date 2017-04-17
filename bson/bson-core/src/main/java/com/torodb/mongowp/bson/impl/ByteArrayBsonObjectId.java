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
package com.torodb.mongowp.bson.impl;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedInteger;
import com.torodb.mongowp.bson.abst.AbstractBsonObjectId;

import java.util.Arrays;

/**
 *
 */
public class ByteArrayBsonObjectId extends AbstractBsonObjectId {

  private static final long serialVersionUID = -8871610748330532954L;

  private final byte[] bytes;

  public ByteArrayBsonObjectId(byte[] bytes) {
    Preconditions.checkArgument(bytes.length >= 12, "The given array of bytes is too short");
    this.bytes = Arrays.copyOf(bytes, 12);
  }

  @Override
  protected byte[] getBytesUnsafe() {
    return bytes;
  }

  @Override
  public byte[] toByteArray() {
    return Arrays.copyOf(bytes, 12);
  }

  @Override
  public UnsignedInteger getUnsignedTimestamp() {
    return UnsignedInteger.fromIntBits(getIntTimestamp());
  }

  private int getIntTimestamp() {
    return bytesAsInt(bytes[0], bytes[1], bytes[2], bytes[3]);
  }

  @Override
  public int getMachineIdentifier() {
    return bytesAsInt((byte) 0, bytes[4], bytes[5], bytes[6]);
  }

  @Override
  public int getProcessId() {
    return bytesAsInt((byte) 0, (byte) 0, bytes[7], bytes[8]);
  }

  @Override
  public int getCounter() {
    return bytesAsInt((byte) 0, bytes[9], bytes[10], bytes[11]);
  }

}
