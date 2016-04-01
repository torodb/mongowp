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
 * along with v3m0. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.eightkdata.mongowp.WriteConcern;
import com.eightkdata.mongowp.WriteConcern.WType;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;

/**
 *
 */
public class CustomWriteConcernUtils {

    private CustomWriteConcernUtils() {}

    public static void checkIfWriteConcernCanBeSatisfied(ReplicaSetConfig replSetConfig, WriteConcern wc) {
        if (wc.getWType() == WType.TEXT && !wc.getWString().equals("majority")) {
            replSetConfig.getCustomWriteConcernTagPattern(patternName)
            StatusWith<ReplicaSetTagPattern> tagPatternStatus = findCustomWriteMode(writeConcern.wMode);
            if (!tagPatternStatus.isOK()) {
                return tagPatternStatus.getStatus();
            }

            ReplicaSetTagMatch matcher(tagPatternStatus.getValue());
            for (size_t j = 0; j < _members.size(); ++j) {
                const MemberConfig& memberConfig = _members[j];
                for (MemberConfig::TagIterator it = memberConfig.tagsBegin();
                     it != memberConfig.tagsEnd();
                     ++it) {
                    if (matcher.update(*it)) {
                        return Status::OK();
                    }
                }
            }
            // Even if all the nodes in the set had a given write it still would not satisfy this
            // write concern mode.
            return Status(ErrorCodes::CannotSatisfyWriteConcern,
                          str::stream() << "Not enough nodes match write concern mode \""
                                        << writeConcern.wMode << "\"");
        } else {
            int nodesRemaining = writeConcern.wNumNodes;
            for (size_t j = 0; j < _members.size(); ++j) {
                if (!_members[j].isArbiter()) {  // Only count data-bearing nodes
                    --nodesRemaining;
                    if (nodesRemaining <= 0) {
                        return Status::OK();
                    }
                }
            }
            return Status(ErrorCodes::CannotSatisfyWriteConcern, "Not enough data-bearing nodes");
        }


    }

}
