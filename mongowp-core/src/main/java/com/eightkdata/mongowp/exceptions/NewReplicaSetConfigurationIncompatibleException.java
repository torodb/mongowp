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
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.bson.BsonDocument;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class NewReplicaSetConfigurationIncompatibleException extends MongoException {

  private static final long serialVersionUID = -2709675821399927014L;

  @Nonnull
  private final BsonDocument newReplSetConf;
  @Nullable
  private final BsonDocument oldReplSetConf;

  public NewReplicaSetConfigurationIncompatibleException(BsonDocument newReplSetConf) {
    this(newReplSetConf, (BsonDocument) null);
  }

  public NewReplicaSetConfigurationIncompatibleException(BsonDocument newReplSetConf,
      BsonDocument oldReplSetConf) {
    super(ErrorCode.NEW_REPLICA_SET_CONFIGURATION_INCOMPATIBLE);
    this.newReplSetConf = newReplSetConf;
    this.oldReplSetConf = oldReplSetConf;
  }

  public NewReplicaSetConfigurationIncompatibleException(BsonDocument newReplSetConf,
      String customMessage) {
    this(newReplSetConf, null, customMessage);
  }

  public NewReplicaSetConfigurationIncompatibleException(
      BsonDocument replicaSetConfDoc,
      BsonDocument olBsonDocument,
      String customMessage) {
    super(customMessage, ErrorCode.NEW_REPLICA_SET_CONFIGURATION_INCOMPATIBLE);
    this.newReplSetConf = replicaSetConfDoc;
    this.oldReplSetConf = olBsonDocument;
  }

  @Nonnull
  public BsonDocument getNewReplSetConf() {
    return newReplSetConf;
  }

  @Nullable
  public BsonDocument getOldReplSetConf() {
    return oldReplSetConf;
  }

}
