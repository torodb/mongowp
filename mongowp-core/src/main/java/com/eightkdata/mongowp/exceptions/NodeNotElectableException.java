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

/**
 *
 */
public class NodeNotElectableException extends MongoException {

    private static final long serialVersionUID = -4393269068299293234L;

    private final int id;
    private final int configVersion;
    private final String replSetName;

    public NodeNotElectableException(int id, int configVersion, String replSetName) {
        super(ErrorCode.NODE_NOT_ELECTABLE);
        this.id = id;
        this.configVersion = configVersion;
        this.replSetName = replSetName;
    }

    public NodeNotElectableException(int id, int configVersion, String replSetName, String customMessage) {
        super(customMessage, ErrorCode.NODE_NOT_ELECTABLE);
        this.id = id;
        this.configVersion = configVersion;
        this.replSetName = replSetName;
    }
    
    public int getId() {
        return id;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public String getReplSetName() {
        return replSetName;
    }

}
