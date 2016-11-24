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

package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;

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
