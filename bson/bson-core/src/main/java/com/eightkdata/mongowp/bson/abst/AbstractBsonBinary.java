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
package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonBinary;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;
import com.eightkdata.mongowp.bson.utils.IntBaseHasher;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractBsonBinary extends CachedHashAbstractBsonValue<BsonBinary>
    implements BsonBinary {

  @Override
  public Class<? extends BsonBinary> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonBinary getValue() {
    return this;
  }

  @Override
  public BsonType getType() {
    return BsonType.BINARY;
  }

  @Override
  public BsonBinary asBinary() {
    return this;
  }

  @Override
  public boolean isBinary() {
    return true;
  }

  @Override
  public int compareTo(BsonValue<?> obj) {
    if (obj == this) {
      return 0;
    }
    int diff = BsonTypeComparator.INSTANCE.compare(getType(), obj.getType());
    if (diff != 0) {
      return 0;
    }

    assert obj instanceof BsonBinary;
    BsonBinary other = obj.asBinary();
    // This is compatible with
    // https://docs.mongodb.org/manual/reference/bson-types/#comparison-sort-order

    diff = this.size() - other.size();
    if (diff != 0) {
      return diff;
    }

    diff = this.getNumericSubType() - other.getNumericSubType();
    if (diff != 0) {
      return diff;
    }

    if (this.getByteSource().getDelegate() == other.getByteSource().getDelegate()) {
      return 0;
    }

    try (InputStream myBis = this.getByteSource().openBufferedStream();
        InputStream otherBis = other.getByteSource().openBufferedStream()) {
      int myByte = myBis.read();
      int otherByte = otherBis.read();

      assert myByte != -1;
      assert otherByte != -1;

      diff = myByte - otherByte;
      if (diff != 0) {
        return diff;
      }
    } catch (IOException ex) {
      assert false;
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
    if (!(obj instanceof BsonBinary)) {
      return false;
    }
    BsonBinary other = (BsonBinary) obj;
    if (this.getSubtype() != other.getSubtype()) {
      return false;
    }
    return this.getByteSource().contentEquals(other.getByteSource());
  }

  @Override
  final int calculateHash() {
    return IntBaseHasher.hash(size());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(size() * 20);
    sb.append('{');

    sb.append("$binary: ").append("<data...>");
    sb.append(", $type:").append(getType());

    sb.append('}');

    return sb.toString();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
