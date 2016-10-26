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

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.exceptions.BadValueException;

/**
 *
 */
public enum ValidationAction {
    WARN,
    ERROR_V;
    
    public static ValidationAction parse(String str) throws BadValueException {
        switch (str) {
            case "": return ERROR_V;
            case "warn": return WARN;
            case "error": return ERROR_V;
            default: throw new BadValueException("Invalid validation action: " + str);
        }
    }

}
