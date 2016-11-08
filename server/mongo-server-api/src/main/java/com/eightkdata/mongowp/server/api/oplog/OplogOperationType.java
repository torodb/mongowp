/*
 * MongoWP - Mongo Server: API
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.server.api.oplog;

/**
 *
 */
public enum OplogOperationType {

    INSERT("i"),
    UPDATE("u"),
    DELETE("d"),
    DB_CMD("c"),
    /**
     * declares presence of a database ({@link OplogOperation#namespace} is set
     * to the db name + '.')
     */
    DB("db"),
    /**
     * used for changes in the database or collections which don't result in a
     * change in the stored data or as a <em>keep alive</em> operation
     */
    NOOP("n");
    private final String oplogName;

    private OplogOperationType(String oplogName) {
        this.oplogName = oplogName;
    }

    public String getOplogName() {
        return oplogName;
    }

    public static OplogOperationType fromOplogName(String name) throws IllegalArgumentException {
        for (OplogOperationType value : OplogOperationType.values()) {
            if (value.getOplogName().equals(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("There is no oplog type whose name is '"
                + name + '\'');
    }
}
