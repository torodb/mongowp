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
package com.eightkdata.mongowp;

import com.eightkdata.mongowp.annotations.Material;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.exceptions.FailedToParseException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.IntField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class WriteConcern {

  private static final Logger LOGGER = LogManager.getLogger(WriteConcern.class);
  private static final WriteConcern ACKNOWLEDGED = new IntWriteConcern(SyncMode.NONE, 0, 1);
  private static final WriteConcern UNACKNOWLEDGED = new IntWriteConcern(SyncMode.NONE, 0, 0);

  private final SyncMode syncMode;
  private final int timeout;

  WriteConcern(@Nonnull SyncMode syncMode, @Nonnegative int timeout) {
    this.syncMode = syncMode;
    this.timeout = timeout;
  }

  @Nonnull 
  public SyncMode getSyncMode() {
    return syncMode;
  }

  /**
   *
   * @return the timeout in millis
   */
  @Nonnegative
  public int getTimeout() {
    return timeout;
  }

  public abstract int getWInt() throws UnsupportedOperationException;

  @Nonnull
  public abstract String getWString() throws UnsupportedOperationException;

  @Nonnull
  public abstract WType getWType();

  public static WriteConcern unacknowledged() {
    return UNACKNOWLEDGED;
  }

  public static WriteConcern acknowledged() {
    return ACKNOWLEDGED;
  }

  /**
   * Write operations wait until flush data to disk.
   */
  public static WriteConcern fsync() {
    return new IntWriteConcern(SyncMode.FSYNC, 0, 0);
  }

  /**
   * Write operations wait until flush journa to disk.
   */
  public static WriteConcern journal() {
    return new IntWriteConcern(SyncMode.JOURNAL, 0, 0);
  }

  public static WriteConcern majority() {
    return with(SyncMode.NONE, 0, "majority");
  }

  public static WriteConcern with(SyncMode syncMode) {
    return new IntWriteConcern(syncMode, 0, 1);
  }

  public static WriteConcern with(SyncMode syncMode, int timeout) {
    return new IntWriteConcern(syncMode, timeout, 1);
  }

  public static WriteConcern with(SyncMode syncMode, int timeout, int w) {
    return new IntWriteConcern(syncMode, timeout, w);
  }

  public static WriteConcern with(SyncMode syncMode, int timeout, String w) {
    return new StringWriteConcern(syncMode, timeout, w);
  }

  public static WriteConcern fromDocument(@Nullable BsonDocument doc, WriteConcern defaultValue) {
    if (doc == null || doc.isEmpty()) {
      return defaultValue;
    }
    try {
      return fromDocument(doc);
    } catch (FailedToParseException ex) {
      return defaultValue;
    }
  }

  public static WriteConcern fromDocument(@Nonnull BsonDocument doc) throws FailedToParseException {
    //Same behaviour as mongo/src/mongo/db/write_concern_options.cpp#parse(BSONObj)
    if (doc.isEmpty()) {
      throw new FailedToParseException("write concern object cannot be empty");
    }

    boolean j;
    boolean fsync;

    try {
      j = BsonReaderTool.getBooleanOrNumeric(doc, "j", false);
    } catch (TypesMismatchException ex) {
      throw new FailedToParseException("j must be numeric or boolean value");
    }
    try {
      fsync = BsonReaderTool.getBooleanOrNumeric(doc, "fsync", false);
    } catch (TypesMismatchException ex) {
      throw new FailedToParseException("fsync must be numeric or a boolean value");
    }

    SyncMode syncMode;

    if (j) {
      syncMode = SyncMode.JOURNAL;
      if (fsync) {
        throw new FailedToParseException("fsync and j options cannot be used together");
      }
    } else if (fsync) {
      syncMode = SyncMode.FSYNC;
    } else {
      syncMode = SyncMode.NONE;
    }

    int timeout;
    try {
      timeout = BsonReaderTool.getNumeric(doc, "timeout").intValue();
    } catch (NoSuchKeyException ex) {
      timeout = 0;
    } catch (TypesMismatchException ex) {
      LOGGER.warn("Unexpected write concern timeout '{}'. Using the default value", doc.get(
          "timeout"));
      timeout = 0;
    }

    BsonValue<?> wValue = doc.get("w");
    if (wValue == null) {
      return new IntWriteConcern(syncMode, timeout, 1);
    }
    if (wValue.isNumber()) {
      return new IntWriteConcern(syncMode, timeout, wValue.asNumber().intValue());
    }
    if (wValue.isString()) {
      return new StringWriteConcern(syncMode, timeout, wValue.asString().getValue());
    }
    throw new FailedToParseException("w has to be a number or a string");
  }

  private static final StringField W_TEXT_FIELD = new StringField("w");
  private static final IntField W_INT_FIELD = new IntField("w");
  private static final BooleanField FSYNC_FIELD = new BooleanField("fsync");
  private static final BooleanField JOURNAL_FIELD = new BooleanField("j");
  private static final IntField TIMEOUT_FIELD = new IntField("timeout");

  @Material
  public BsonDocument toDocument() {
    BsonDocumentBuilder builder = new BsonDocumentBuilder(3);
    if (getWType().equals(WType.TEXT)) {
      builder.append(W_TEXT_FIELD, getWString());
    } else {
      builder.append(W_INT_FIELD, getWInt());
    }
    switch (getSyncMode()) {
      case FSYNC:
        builder.append(FSYNC_FIELD, true);
        break;
      case JOURNAL:
        builder.append(JOURNAL_FIELD, true);
        break;
      default:
    }

    builder.append(TIMEOUT_FIELD, getTimeout());

    return builder.build();
  }

  @Override
  public String toString() {
    return toDocument().toString();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + Objects.hashCode(this.syncMode);
    hash = 29 * hash + this.timeout;
    switch (getWType()) {
      case INT:
        hash = 29 * hash + getWInt();
        break;
      case TEXT:
        hash = 29 * hash + getWString().hashCode();
        break;
      default:
    }
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final WriteConcern other = (WriteConcern) obj;
    if (this.timeout != other.timeout) {
      return false;
    }
    if (this.syncMode != other.syncMode) {
      return false;
    }
    if (this.getWType() != other.getWType()) {
      return false;
    }
    switch (this.getWType()) {
      case INT:
        return this.getWInt() == other.getWInt();
      case TEXT:
        return this.getWString().equals(other.getWString());
      default:
        throw new AssertionError("Unexpected WType");
    }
  }

  public static enum SyncMode {
    NONE,
    FSYNC,
    JOURNAL
  }

  public static enum WType {
    INT,
    TEXT;
  }
}
