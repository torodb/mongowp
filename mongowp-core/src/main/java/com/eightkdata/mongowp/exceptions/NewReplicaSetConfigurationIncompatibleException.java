/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mongowp-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
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

    public NewReplicaSetConfigurationIncompatibleException(BsonDocument newReplSetConf, BsonDocument oldReplSetConf) {
        super(ErrorCode.NEW_REPLICA_SET_CONFIGURATION_INCOMPATIBLE);
        this.newReplSetConf = newReplSetConf;
        this.oldReplSetConf = oldReplSetConf;
    }

    public NewReplicaSetConfigurationIncompatibleException(BsonDocument newReplSetConf, String customMessage) {
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
