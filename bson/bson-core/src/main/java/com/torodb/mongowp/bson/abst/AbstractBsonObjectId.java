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
package com.torodb.mongowp.bson.abst;

import com.torodb.mongowp.bson.BsonObjectId;
import com.torodb.mongowp.bson.BsonType;
import com.torodb.mongowp.bson.BsonValue;
import com.torodb.mongowp.bson.BsonValueVisitor;
import com.torodb.mongowp.bson.utils.BsonTypeComparator;

import java.time.Instant;

public abstract class AbstractBsonObjectId extends AbstractBsonValue<BsonObjectId>
    implements BsonObjectId {

  private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();

  @Override
  public Class<? extends BsonObjectId> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonObjectId getValue() {
    return this;
  }

  @Override
  public BsonType getType() {
    return BsonType.OBJECT_ID;
  }

  @Override
  public BsonObjectId asObjectId() {
    return this;
  }

  @Override
  public boolean isObjectId() {
    return true;
  }

  protected byte[] getBytesUnsafe() {
    return toByteArray();
  }

  @Override
  public Instant getTimestamp() {
    return Instant.ofEpochSecond(getUnsignedTimestamp().longValue());
  }

  @Override
  public byte[] toByteArray() {
    byte[] result = new byte[12];

    int timestamp = getUnsignedTimestamp().intValue();
    result[0] = getForthByte(timestamp);
    result[1] = getThirdByte(timestamp);
    result[2] = getSecondByte(timestamp);
    result[3] = getFirstByte(timestamp);

    int machineId = getMachineIdentifier();
    assert getForthByte(machineId) == 0;
    result[4] = getThirdByte(machineId);
    result[5] = getSecondByte(machineId);
    result[6] = getFirstByte(machineId);

    int processId = getProcessId();
    assert getForthByte(processId) == 0;
    assert getThirdByte(processId) == 0;
    result[7] = getSecondByte(processId);
    result[8] = getFirstByte(processId);

    int counter = getCounter();
    assert getForthByte(counter) == 0;
    result[9] = getThirdByte(counter);
    result[10] = getSecondByte(counter);
    result[11] = getFirstByte(counter);

    return result;
  }

  @Override
  public String toHexString() {
    StringBuilder sb = new StringBuilder(24);
    for (byte b : getBytesUnsafe()) {
      sb.append(HEX_CODE[(b >> 4) & 0xF]);
      sb.append(HEX_CODE[(b & 0xF)]);
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return "0x" + toHexString();
  }

  @Override
  public int compareTo(BsonValue<?> obj) {
    if (obj == this) {
      return 0;
    }
    int diff = BsonTypeComparator.INSTANCE.compare(getType(), obj.getType());
    if (diff != 0) {
      return diff;
    }

    if (obj.isDbPointer()) {
      return -1;
    }

    assert obj instanceof BsonObjectId;
    BsonObjectId other = obj.asObjectId();

    byte[] otherBytes;
    if (obj instanceof AbstractBsonObjectId) {
      otherBytes = ((AbstractBsonObjectId) obj).getBytesUnsafe();
    } else {
      otherBytes = other.toByteArray();
    }
    byte[] thisBytes = getBytesUnsafe();
    for (int i = 0; i < 12; i++) {
      diff = (thisBytes[i] & 0xFF) - (otherBytes[i] & 0xFF);

      if (diff != 0) {
        return diff;
      }
    }
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonObjectId)) {
      return false;
    }
    return compareTo((BsonObjectId) obj) == 0;
  }

  @Override
  public int hashCode() {
    byte[] bytes = getBytesUnsafe();

    return bytesAsInt(bytes[2], bytes[3], bytes[10], bytes[11]);
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

  protected static byte getForthByte(final int b3) {
    return (byte) (b3 >> 24);
  }

  protected static byte getThirdByte(final int b2) {
    return (byte) (b2 >> 16);
  }

  protected static byte getSecondByte(final int b1) {
    return (byte) (b1 >> 8);
  }

  protected static byte getFirstByte(final int b0) {
    return (byte) (b0);
  }

  protected int bytesAsInt(byte b3, byte b2, byte b1, byte b0) {
    return ((0xFF & b3) << 24) | ((0xFF & b2) << 16) | ((0xFF & b1) << 8) | (0xFF & b0);
  }
}
